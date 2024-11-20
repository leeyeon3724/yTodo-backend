package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.service.YTodoPlanForWorkReminderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class YTodoPlanForWorkReminderControllerTest {

    @Mock
    private YTodoPlanForWorkReminderService reminderService;

    @InjectMocks
    private YTodoPlanForWorkReminderController reminderController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    YTodoPlanForWorkReminderControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthenticatedUser(String email) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetRemindersByPlanId() {
        mockAuthenticatedUser("test@example.com");

        YTodoPlanForWorkReminder reminder = new YTodoPlanForWorkReminder();
        reminder.setReminderTime(LocalDateTime.now().plusDays(1));

        when(reminderService.findAllByPlanIdAndUser(1L, "test@example.com")).thenReturn(Collections.singletonList(reminder));

        ResponseEntity<List<YTodoPlanForWorkReminder>> response = reminderController.getRemindersByPlan(1L);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getReminderTime()).isEqualTo(reminder.getReminderTime());
        verify(reminderService, times(1)).findAllByPlanIdAndUser(1L, "test@example.com");
    }
}
