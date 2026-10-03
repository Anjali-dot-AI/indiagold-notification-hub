package com.indiagold.notificationhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indiagold.notificationhub.dto.NotificationRequest;
import com.indiagold.notificationhub.repository.NotificationLogRepository;
import com.indiagold.notificationhub.service.NotificationDispatcherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifies the controller rejects invalid payloads (required-field validation)
 * BEFORE they ever reach the dispatcher service.
 */
@WebMvcTest(NotificationController.class)
class NotificationControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationDispatcherService dispatcherService;

    @MockBean
    private NotificationLogRepository notificationLogRepository;

    @Test
    void shouldReturn400WhenRequiredFieldsAreMissing() throws Exception {
        NotificationRequest invalidRequest = new NotificationRequest();
        invalidRequest.setChannels(Collections.emptyList()); // userId, title, body all missing; channels empty

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
