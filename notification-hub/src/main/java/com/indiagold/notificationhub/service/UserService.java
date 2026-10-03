package com.indiagold.notificationhub.service;

import com.indiagold.notificationhub.dto.UserRequest;
import com.indiagold.notificationhub.exception.ResourceNotFoundException;
import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import com.indiagold.notificationhub.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(UserRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber(),
                request.getDeviceToken(),
                request.getOptedInChannels()
        );
        return userRepository.save(user);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updatePreferences(Long id, Set<Channel> optedInChannels) {
        User user = getUserOrThrow(id);
        user.setOptedInChannels(optedInChannels);
        return userRepository.save(user);
    }
}
