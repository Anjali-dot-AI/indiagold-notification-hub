package com.indiagold.notificationhub.service;

import com.indiagold.notificationhub.channel.NotificationChannelSender;
import com.indiagold.notificationhub.dto.ChannelResult;
import com.indiagold.notificationhub.dto.NotificationRequest;
import com.indiagold.notificationhub.dto.NotificationResponse;
import com.indiagold.notificationhub.exception.ResourceNotFoundException;
import com.indiagold.notificationhub.model.*;
import com.indiagold.notificationhub.repository.NotificationLogRepository;
import com.indiagold.notificationhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationDispatcherService {

    private final UserRepository userRepository;
    private final NotificationLogRepository notificationLogRepository;

    // Channel -> the sender responsible for it. Built once from whatever NotificationChannelSender
    // beans Spring finds in the application context - this is the key trick that makes the
    // dispatcher "closed for modification, open for extension": adding a new channel sender
    // class means it automatically appears here with zero changes to this service.
    private final Map<Channel, NotificationChannelSender> sendersByChannel;

    public NotificationDispatcherService(UserRepository userRepository,
                                          NotificationLogRepository notificationLogRepository,
                                          List<NotificationChannelSender> senders) {
        this.userRepository = userRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.sendersByChannel = senders.stream()
                .collect(Collectors.toMap(NotificationChannelSender::getChannelType, Function.identity()));
    }

    public NotificationResponse dispatch(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));

        List<ChannelResult> results = new ArrayList<>();

        for (Channel requestedChannel : request.getChannels()) {

            // --- Preference check happens BEFORE any dispatch attempt ---
            if (!user.getOptedInChannels().contains(requestedChannel)) {
                results.add(logAndBuildResult(user.getId(), requestedChannel, DeliveryStatus.SKIPPED,
                        request.getTitle(), "User has not opted into this channel"));
                continue;
            }

            NotificationChannelSender sender = sendersByChannel.get(requestedChannel);
            try {
                sender.send(user, request.getTitle(), request.getBody());
                results.add(logAndBuildResult(user.getId(), requestedChannel, DeliveryStatus.SUCCESS,
                        request.getTitle(), null));
            } catch (Exception ex) {
                results.add(logAndBuildResult(user.getId(), requestedChannel, DeliveryStatus.FAILED,
                        request.getTitle(), ex.getMessage()));
            }
        }

        return new NotificationResponse(user.getId(), results);
    }

    private ChannelResult logAndBuildResult(Long userId, Channel channel, DeliveryStatus status,
                                             String title, String detail) {
        notificationLogRepository.save(new NotificationLog(userId, channel, status, title, detail));
        return new ChannelResult(channel, status, detail);
    }
}
