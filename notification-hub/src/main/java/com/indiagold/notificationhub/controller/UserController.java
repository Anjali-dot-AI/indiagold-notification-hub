package com.indiagold.notificationhub.controller;

import com.indiagold.notificationhub.dto.UserRequest;
import com.indiagold.notificationhub.model.Channel;
import com.indiagold.notificationhub.model.User;
import com.indiagold.notificationhub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserOrThrow(id));
    }

    @PutMapping("/{id}/preferences")
    public ResponseEntity<User> updatePreferences(@PathVariable Long id, @RequestBody Set<Channel> optedInChannels) {
        return ResponseEntity.ok(userService.updatePreferences(id, optedInChannels));
    }
}
