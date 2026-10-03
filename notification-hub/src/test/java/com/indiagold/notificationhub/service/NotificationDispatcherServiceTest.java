package com.indiagold.notificationhub.service;

import com.indiagold.notificationhub.channel.NotificationChannelSender;
import com.indiagold.notificationhub.dto.ChannelResult;
import com.indiagold.notificationhub.dto.NotificationRequest;
import com.indiagold.notificationhub.dto.NotificationResponse;
import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.DeliveryStatus;
import com.indiagold.notificationhub.model.User;
import com.indiagold.notificationhub.repository.NotificationLogRepository;
import com.indiagold.notificationhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pure unit tests (no Spring context loaded - fast) for the dispatcher's core logic:
 * preference checking, routing, and success/failure/skip status recording.
 * All dependencies (repositories, channel senders) are mocked with Mockito.
 */
class NotificationDispatcherServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificationLogRepository notificationLogRepository;

    @Mock
    private NotificationChannelSender emailSender;

    @Mock
    private NotificationChannelSender smsSender;

    private NotificationDispatcherService dispatcherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(emailSender.getChannelType()).thenReturn(Channel.EMAIL);
        when(smsSender.getChannelType()).thenReturn(Channel.SMS);

        dispatcherService = new NotificationDispatcherService(
                userRepository,
                notificationLogRepository,
                List.of(emailSender, smsSender)
        );
    }

    @Test
    void shouldSkipChannelUserHasNotOptedInto() {
        // User opted into EMAIL only, NOT SMS
        User user = new User("Test User", "test@example.com", "9999999999", null, Set.of(Channel.EMAIL));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        NotificationRequest request = buildRequest(1L, List.of(Channel.SMS));

        NotificationResponse response = dispatcherService.dispatch(request);

        ChannelResult result = response.getResults().get(0);
        assertEquals(DeliveryStatus.SKIPPED, result.getStatus());
        verify(smsSender, never()).send(any(), any(), any()); // SMS sender must NEVER be called
    }

    @Test
    void shouldDispatchSuccessfullyWhenUserHasOptedIn() {
        User user = new User("Test User", "test@example.com", "9999999999", null, Set.of(Channel.EMAIL));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(emailSender).send(eq(user), anyString(), anyString());

        NotificationRequest request = buildRequest(1L, List.of(Channel.EMAIL));

        NotificationResponse response = dispatcherService.dispatch(request);

        assertEquals(DeliveryStatus.SUCCESS, response.getResults().get(0).getStatus());
        verify(emailSender, times(1)).send(eq(user), anyString(), anyString());
    }

    @Test
    void shouldRecordFailedStatusWhenSenderThrowsException() {
        User user = new User("Test User", "test@example.com", "9999999999", null, Set.of(Channel.EMAIL));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doThrow(new RuntimeException("Simulated provider outage"))
                .when(emailSender).send(eq(user), anyString(), anyString());

        NotificationRequest request = buildRequest(1L, List.of(Channel.EMAIL));

        NotificationResponse response = dispatcherService.dispatch(request);

        ChannelResult result = response.getResults().get(0);
        assertEquals(DeliveryStatus.FAILED, result.getStatus());
        assertEquals("Simulated provider outage", result.getDetail());
    }

    @Test
    void shouldHandleMultipleChannelsWithMixedOutcomes() {
        // Opted into EMAIL and SMS
        User user = new User("Test User", "test@example.com", "9999999999", null,
                Set.of(Channel.EMAIL, Channel.SMS));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(emailSender).send(eq(user), anyString(), anyString());
        doThrow(new RuntimeException("No phone number")).when(smsSender).send(eq(user), anyString(), anyString());

        // Request EMAIL (opted-in, succeeds), SMS (opted-in, fails) - both get attempted
        NotificationRequest request = buildRequest(1L, List.of(Channel.EMAIL, Channel.SMS));

        NotificationResponse response = dispatcherService.dispatch(request);

        assertEquals(2, response.getResults().size());
        assertEquals(DeliveryStatus.SUCCESS, response.getResults().get(0).getStatus());
        assertEquals(DeliveryStatus.FAILED, response.getResults().get(1).getStatus());
    }

    @Test
    void shouldLogEveryDispatchAttemptRegardlessOfOutcome() {
        User user = new User("Test User", "test@example.com", "9999999999", null, Set.of(Channel.EMAIL));
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        NotificationRequest request = buildRequest(1L, List.of(Channel.EMAIL, Channel.SMS)); // SMS not opted-in

        dispatcherService.dispatch(request);

        // Both the successful EMAIL attempt and the SKIPPED SMS attempt must be logged for auditing
        verify(notificationLogRepository, times(2)).save(any());
    }

    private NotificationRequest buildRequest(Long userId, List<Channel> channels) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("Test Title");
        request.setBody("Test Body");
        request.setChannels(channels);
        return request;
    }
}
