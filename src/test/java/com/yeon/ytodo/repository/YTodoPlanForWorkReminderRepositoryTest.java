package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.model.YTodoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class YTodoPlanForWorkReminderRepositoryTest {

    @Autowired
    private YTodoPlanForWorkReminderRepository reminderRepository;

    @Autowired
    private YTodoPlanForWorkFormRepository planRepository;

    @Autowired
    private YTodoUserRepository userRepository;

    @Test
    void testSaveAndFindAllByPlan() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm plan = createPlan(user);

        YTodoPlanForWorkReminder reminder = new YTodoPlanForWorkReminder();
        reminder.setPlan(plan);
        reminder.setReminderTime(LocalDateTime.now().plusDays(1));
        reminder = reminderRepository.save(reminder);

        List<YTodoPlanForWorkReminder> reminders = reminderRepository.findAllByPlan(plan);
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReminderTime()).isEqualTo(reminder.getReminderTime());
    }

    @Test
    void testFindAllByReminderTimeAfter() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm plan = createPlan(user);

        YTodoPlanForWorkReminder reminder1 = createReminder(plan, LocalDateTime.now().plusDays(1));
        YTodoPlanForWorkReminder reminder2 = createReminder(plan, LocalDateTime.now().plusDays(3));

        LocalDateTime after = LocalDateTime.now().plusDays(2);
        List<YTodoPlanForWorkReminder> reminders = reminderRepository.findAllByReminderTimeAfter(after);
        assertThat(reminders).hasSize(1);
        assertThat(reminders.get(0).getReminderTime()).isAfter(after);
    }

    private YTodoUser createUser() {
        YTodoUser user = new YTodoUser();
        user.setName("testuser");
        user.setEmail("testuser@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    private YTodoPlanForWorkForm createPlan(YTodoUser user) {
        YTodoPlanForWorkForm plan = new YTodoPlanForWorkForm();
        plan.setName("Test Plan");
        plan.setUser(user);
        return planRepository.save(plan);
    }

    private YTodoPlanForWorkReminder createReminder(YTodoPlanForWorkForm plan, LocalDateTime time) {
        YTodoPlanForWorkReminder reminder = new YTodoPlanForWorkReminder();
        reminder.setPlan(plan);
        reminder.setReminderTime(time);
        return reminderRepository.save(reminder);
    }
}
