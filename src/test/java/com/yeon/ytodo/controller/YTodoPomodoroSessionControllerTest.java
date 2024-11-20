package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.service.YTodoPomodoroSessionService;
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

class YTodoPomodoroSessionControllerTest {

    @Mock
    private YTodoPomodoroSessionService sessionService;

    @InjectMocks
    private YTodoPomodoroSessionController sessionController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    YTodoPomodoroSessionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthenticatedUser(String email) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllSessions() {
        mockAuthenticatedUser("test@example.com");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("Test Session");

        when(sessionService.findAllByUser("test@example.com")).thenReturn(Collections.singletonList(session));

        ResponseEntity<List<YTodoPomodoroSession>> response = sessionController.getAllSessions();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Test Session");
        verify(sessionService, times(1)).findAllByUser("test@example.com");
    }

    @Test
    void testGetSessionsByPlan() {
        mockAuthenticatedUser("test@example.com");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("Plan Session");

        when(sessionService.findAllByUserAndPlanId("test@example.com", 1L)).thenReturn(Collections.singletonList(session));

        ResponseEntity<List<YTodoPomodoroSession>> response = sessionController.getSessionsByPlan(1L);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Plan Session");
        verify(sessionService, times(1)).findAllByUserAndPlanId("test@example.com", 1L);
    }

    @Test
    void testGetSessionById() {
        mockAuthenticatedUser("test@example.com");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("Specific Session");

        when(sessionService.findByIdAndUser(1L, "test@example.com")).thenReturn(session);

        ResponseEntity<YTodoPomodoroSession> response = sessionController.getSessionById(1L);
        assertThat(response.getBody().getName()).isEqualTo("Specific Session");
        verify(sessionService, times(1)).findByIdAndUser(1L, "test@example.com");
    }

    @Test
    void testCreateSession() {
        mockAuthenticatedUser("test@example.com");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("New Session");

        when(sessionService.save(session, "test@example.com")).thenReturn(session);

        ResponseEntity<YTodoPomodoroSession> response = sessionController.createSession(session);
        assertThat(response.getBody().getName()).isEqualTo("New Session");
        verify(sessionService, times(1)).save(session, "test@example.com");
    }

    @Test
    void testUpdateSession() {
        mockAuthenticatedUser("test@example.com");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("Updated Session");

        when(sessionService.update(1L, session, "test@example.com")).thenReturn(session);

        ResponseEntity<YTodoPomodoroSession> response = sessionController.updateSession(1L, session);
        assertThat(response.getBody().getName()).isEqualTo("Updated Session");
        verify(sessionService, times(1)).update(1L, session, "test@example.com");
    }

    @Test
    void testDeleteSession() {
        mockAuthenticatedUser("test@example.com");

        doNothing().when(sessionService).delete(1L, "test@example.com");

        ResponseEntity<String> response = sessionController.deleteSession(1L);
        assertThat(response.getBody()).isEqualTo("Pomodoro session deleted successfully");
        verify(sessionService, times(1)).delete(1L, "test@example.com");
    }
}
