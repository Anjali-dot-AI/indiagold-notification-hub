package com.indiagold.notificationhub.config;

import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import com.indiagold.notificationhub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Seeds a couple of demo users on startup so the API can be tested immediately
 * without first having to create users manually.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;

    public DataSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            // User 1: opted into everything
            userRepository.save(new User(
                    "Rahul Sharma", "rahul@example.com", "9876543210", "device-token-rahul-123",
                    Set.of(Channel.EMAIL, Channel.SMS, Channel.PUSH, Channel.IN_APP)
            ));

            // User 2: opted OUT of SMS specifically - good for demonstrating the preference check
            userRepository.save(new User(
                    "Priya Verma", "priya@example.com", "9123456780", "device-token-priya-456",
                    Set.of(Channel.EMAIL, Channel.PUSH, Channel.IN_APP)
            ));

            // User 3: has no phone number at all - good for demonstrating a FAILED dispatch
            userRepository.save(new User(
                    "Amit Singh", "amit@example.com", null, "device-token-amit-789",
                    Set.of(Channel.EMAIL, Channel.SMS, Channel.IN_APP)
            ));

            System.out.println(">>> Seeded 3 demo users (ids 1, 2, 3) with different channel preferences");
        }
    }
}
