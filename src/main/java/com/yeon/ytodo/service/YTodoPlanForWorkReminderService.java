package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlan;
import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoPlanForWorkReminderRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YTodoPlanForWorkReminderService {

    private final YTodoPlanForWorkReminderRepository reminderRepository;
    private final YTodoPlanForWorkFormRepository formRepository;
    private final YTodoUserRepository userRepository;

    /**
     * 인증된 사용자의 이메일로 사용자 조회
     *
     * @param email 사용자 이메일
     * @return YTodoUser 객체
     */
    private YTodoUser getUserByEmail(String email) {
        List<YTodoUser> users = userRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new EntityNotFoundException("User not found with email: " + email);
        }
        return users.get(0); // 이메일은 고유하다고 가정
    }

    /**
     * 특정 Plan에 속한 모든 Reminder 조회
     *
     * @param planId Plan ID
     * @param email  사용자 이메일
     * @return Plan에 속한 모든 Reminder 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPlanForWorkReminder> findAllByPlanIdAndUser(Long planId, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlan plan = formRepository.findById(planId)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for the authenticated user."));
        return reminderRepository.findAllByPlan(plan);
    }

    /**
     * 특정 시간 이후의 모든 Reminder 조회
     *
     * @param time  기준 시간
     * @param email 사용자 이메일
     * @return 특정 시간 이후의 Reminder 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPlanForWorkReminder> findAllByReminderTimeAfterAndUser(LocalDateTime time, String email) {
        YTodoUser user = getUserByEmail(email);
        return reminderRepository.findAllByReminderTimeAfter(time).stream()
                .filter(reminder -> reminder.getPlan().getUser().equals(user))
                .toList();
    }

    /**
     * Reminder 저장
     *
     * @param planId   Plan ID
     * @param reminder 저장할 Reminder 객체
     * @param email    사용자 이메일
     * @return 저장된 Reminder 객체
     */
    @Transactional
    public YTodoPlanForWorkReminder save(Long planId, YTodoPlanForWorkReminder reminder, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlan plan = formRepository.findById(planId)
                .filter(p -> p.getUser().equals(user))
                .orElseThrow(() -> new EntityNotFoundException("Plan not found for the authenticated user."));
        reminder.setPlan(plan);
        return reminderRepository.save(reminder);
    }

    /**
     * Reminder 업데이트
     *
     * @param id       업데이트할 Reminder ID
     * @param reminder 새로운 데이터로 업데이트된 Reminder 객체
     * @param email    사용자 이메일
     * @return 업데이트된 Reminder 객체
     */
    @Transactional
    public YTodoPlanForWorkReminder update(Long id, YTodoPlanForWorkReminder reminder, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlanForWorkReminder existingReminder = reminderRepository.findById(id)
                .filter(r -> r.getPlan().getUser().equals(user))
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found for the authenticated user."));

        existingReminder.setReminderTime(reminder.getReminderTime());
        return reminderRepository.save(existingReminder);
    }

    /**
     * Reminder 삭제
     *
     * @param id    삭제할 Reminder ID
     * @param email 사용자 이메일
     */
    @Transactional
    public void delete(Long id, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlanForWorkReminder reminder = reminderRepository.findById(id)
                .filter(r -> r.getPlan().getUser().equals(user))
                .orElseThrow(() -> new EntityNotFoundException("Reminder not found for the authenticated user."));
        reminderRepository.delete(reminder);
    }
}
