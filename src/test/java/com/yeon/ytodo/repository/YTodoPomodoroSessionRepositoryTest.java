package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class YTodoPomodoroSessionRepositoryTest {

    @Autowired
    private YTodoPomodoroSessionRepository sessionRepository;

    @Autowired
    private YTodoPlanForWorkFormRepository planRepository;

    @Autowired
    private YTodoUserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm plan = createPlan(user, "Test Plan");

        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName("Test Session");
        session.setUser(user);
        session.setPlan(plan);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(25));
        session.setFocusDuration(25);
        session.setShortBreakDuration(5);
        session.setLongBreakDuration(15);
        session.setSessionNumber(1);
        session.setTotalSessions(4);

        session = sessionRepository.save(session);

        Optional<YTodoPomodoroSession> foundSession = sessionRepository.findById(session.getId());
        assertThat(foundSession).isPresent();
        assertThat(foundSession.get().getName()).isEqualTo("Test Session");
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = createUser();
        createSession(user, null, "Session 1");
        createSession(user, null, "Session 2");

        List<YTodoPomodoroSession> sessions = sessionRepository.findAllByUser(user);
        assertThat(sessions).hasSize(2);
        assertThat(sessions.get(0).getName()).isEqualTo("Session 1");
        assertThat(sessions.get(1).getName()).isEqualTo("Session 2");
    }

    @Test
    void testFindAllByUserAndPlanId() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm plan1 = createPlan(user, "Plan 1");
        YTodoPlanForWorkForm plan2 = createPlan(user, "Plan 2");

        createSession(user, plan1, "Session 1");
        createSession(user, plan2, "Session 2");

        List<YTodoPomodoroSession> sessions = sessionRepository.findAllByUserAndPlanId(user, plan1.getId());
        assertThat(sessions).hasSize(1);
        assertThat(sessions.get(0).getName()).isEqualTo("Session 1");
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

    private YTodoPlanForWorkForm createPlan(YTodoUser user, String name) {
        YTodoPlanForWorkForm plan = new YTodoPlanForWorkForm();
        plan.setName(name);
        plan.setUser(user);
        return planRepository.save(plan);
    }

    private YTodoPomodoroSession createSession(YTodoUser user, YTodoPlanForWorkForm plan, String name) {
        YTodoPomodoroSession session = new YTodoPomodoroSession();
        session.setName(name);
        session.setUser(user);
        session.setPlan(plan);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusMinutes(25));
        session.setFocusDuration(25);
        session.setShortBreakDuration(5);
        session.setLongBreakDuration(15);
        session.setSessionNumber(1);
        session.setTotalSessions(4);
        return sessionRepository.save(session);
    }
}
