package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoPlanForWorkReminderRepository;
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

class YTodoPlanForWorkReminderServiceTest {

    @Mock
    private YTodoPlanForWorkReminderRepository reminderRepository;

    @Mock
    private YTodoPlanForWorkFormRepository formRepository;

    @Mock
    private YTodoUserRepository userRepository;

    @InjectMocks
    private YTodoPlanForWorkReminderService reminderService;

    YTodoPlanForWorkReminderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private YTodoUser mockUser(String email) {
        YTodoUser user = new YTodoUser();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail(email);
        return user;
    }

    private YTodoPlanForWorkForm mockPlan(YTodoUser user) {
        YTodoPlanForWorkForm plan = new YTodoPlanForWorkForm();
        plan.setId(1L);
        plan.setName("Test Plan");
        plan.setUser(user);
        return plan;
    }

    private YTodoPlanForWorkReminder mockReminder(YTodoPlanForWorkForm plan) {
        YTodoPlanForWorkReminder reminder = new YTodoPlanForWorkReminder();
        reminder.setId(1L);
        reminder.setPlan(plan);
        reminder.setReminderTime(LocalDateTime.now().plusDays(1));
        return reminder;
    }

    @Test
    void testFindAllByPlanIdAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm plan = mockPlan(user);
        YTodoPlanForWorkReminder reminder = mockReminder(plan);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findById(1L)).thenReturn(java.util.Optional.of(plan));
        when(reminderRepository.findAllByPlan(plan)).thenReturn(Collections.singletonList(reminder));

        List<YTodoPlanForWorkReminder> reminders = reminderService.findAllByPlanIdAndUser(1L, "test@example.com");
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReminderTime()).isEqualTo(reminder.getReminderTime());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).findAllByPlan(plan);
    }

    @Test
    void testFindAllByReminderTimeAfterAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm plan = mockPlan(user);
        YTodoPlanForWorkReminder reminder = mockReminder(plan);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(reminderRepository.findAllByReminderTimeAfter(any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(reminder));

        List<YTodoPlanForWorkReminder> reminders =
                reminderService.findAllByReminderTimeAfterAndUser(LocalDateTime.now(), "test@example.com");
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReminderTime()).isEqualTo(reminder.getReminderTime());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(reminderRepository, times(1)).findAllByReminderTimeAfter(any(LocalDateTime.class));
    }

    @Test
    void testSave() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm plan = mockPlan(user);
        YTodoPlanForWorkReminder reminder = mockReminder(plan);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findById(1L)).thenReturn(java.util.Optional.of(plan));
        when(reminderRepository.save(reminder)).thenReturn(reminder);

        YTodoPlanForWorkReminder savedReminder = reminderService.save(1L, reminder, "test@example.com");
        assertThat(savedReminder.getReminderTime()).isEqualTo(reminder.getReminderTime());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).save(reminder);
    }

    @Test
    void testUpdate() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm plan = mockPlan(user);
        YTodoPlanForWorkReminder existingReminder = mockReminder(plan);
        YTodoPlanForWorkReminder updatedReminder = new YTodoPlanForWorkReminder();
        updatedReminder.setReminderTime(LocalDateTime.now().plusDays(2));

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(reminderRepository.findById(1L)).thenReturn(java.util.Optional.of(existingReminder));
        when(reminderRepository.save(existingReminder)).thenReturn(existingReminder);

        YTodoPlanForWorkReminder result = reminderService.update(1L, updatedReminder, "test@example.com");
        assertThat(result.getReminderTime()).isEqualTo(updatedReminder.getReminderTime());

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(reminderRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).save(existingReminder);
    }

    @Test
    void testDelete() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm plan = mockPlan(user);
        YTodoPlanForWorkReminder reminder = mockReminder(plan);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(reminderRepository.findById(1L)).thenReturn(java.util.Optional.of(reminder));
        doNothing().when(reminderRepository).delete(reminder);

        reminderService.delete(1L, "test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(reminderRepository, times(1)).findById(1L);
        verify(reminderRepository, times(1)).delete(reminder);
    }
}
