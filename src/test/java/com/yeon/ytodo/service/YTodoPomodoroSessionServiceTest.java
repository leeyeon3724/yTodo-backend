package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.model.YTodoPomodoroStatus;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPomodoroSessionRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class YTodoPomodoroSessionServiceTest {

    @Mock
    private YTodoPomodoroSessionRepository sessionRepository;

    @Mock
    private YTodoUserRepository userRepository;

    @InjectMocks
    private YTodoPomodoroSessionService sessionService;

    YTodoPomodoroSessionServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private YTodoUser mockUser(String email) {
        YTodoUser user = new YTodoUser();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail(email);
        return user;
    }

    private YTodoPomodoroSession mockSession(YTodoUser user, String name) {
        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setId(1L);
        session.setName(name);
        session.setUser(user);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(25));
        session.setStatus(YTodoPomodoroStatus.COMPLETED);
        session.setFocusDuration(25);
        session.setShortBreakDuration(5);
        session.setLongBreakDuration(15);
        session.setSessionNumber(1);
        session.setTotalSessions(4);
        session.setCompleted(true);
        return session;
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession session = mockSession(user, "Test Session");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.findAllByUser(user)).thenReturn(Collections.singletonList(session));

        List<YTodoPomodoroSession> sessions = sessionService.findAllByUser("test@example.com");
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Test Session");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testFindAllByUserAndPlanId() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession session = mockSession(user, "Test Session");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.findAllByUserAndPlanId(user, 1L)).thenReturn(Collections.singletonList(session));

        List<YTodoPomodoroSession> sessions = sessionService.findAllByUserAndPlanId("test@example.com", 1L);
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Test Session");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).findAllByUserAndPlanId(user, 1L);
    }

    @Test
    void testFindByIdAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession session = mockSession(user, "Test Session");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(session));

        YTodoPomodoroSession foundSession = sessionService.findByIdAndUser(1L, "test@example.com");
        assertThat(foundSession.getName()).isEqualTo("Test Session");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).findByIdAndUser(1L, user);
    }

    @Test
    void testSave() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession session = mockSession(user, "New Session");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        YTodoPomodoroSession savedSession = sessionService.save(session, "test@example.com");
        assertThat(savedSession.getName()).isEqualTo("New Session");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testUpdate() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession existingSession = mockSession(user, "Old Session");
        YTodoPomodoroSession updatedSession = new YTodoPomodoroSession();
        updatedSession.setName("Updated Session");
        updatedSession.setEndTime(LocalDateTime.now().plusMinutes(30));
        updatedSession.setStatus(YTodoPomodoroStatus.IN_PROGRESS);
        updatedSession.setCompleted(false);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(existingSession));
        when(sessionRepository.save(existingSession)).thenReturn(existingSession);

        YTodoPomodoroSession result = sessionService.update(1L, updatedSession, "test@example.com");
        assertThat(result.getName()).isEqualTo("Updated Session");
        assertThat(result.getEndTime()).isEqualTo(updatedSession.getEndTime());
        assertThat(result.getStatus()).isEqualTo(YTodoPomodoroStatus.IN_PROGRESS);
        assertThat(result.isCompleted()).isEqualTo(false);

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).findByIdAndUser(1L, user);
        verify(sessionRepository, times(1)).save(existingSession);
    }

    @Test
    void testDelete() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPomodoroSession session = mockSession(user, "Session to Delete");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(sessionRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(session));
        doNothing().when(sessionRepository).delete(session);

        sessionService.delete(1L, "test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(sessionRepository, times(1)).findByIdAndUser(1L, user);
        verify(sessionRepository, times(1)).delete(session);
    }
}
