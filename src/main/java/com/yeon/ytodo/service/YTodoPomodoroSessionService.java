package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPomodoroSessionRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YTodoPomodoroSessionService {

    private final YTodoPomodoroSessionRepository sessionRepository;
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
     * 특정 사용자에 해당하는 모든 Pomodoro 세션 조회
     *
     * @param email 사용자 이메일
     * @return 사용자의 세션 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPomodoroSession> findAllByUser(String email) {
        YTodoUser user = getUserByEmail(email);
        return sessionRepository.findAllByUser(user);
    }

    /**
     * 특정 사용자의 특정 Plan에 속한 세션 조회
     *
     * @param email  사용자 이메일
     * @param planId Plan ID
     * @return 세션 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPomodoroSession> findAllByUserAndPlanId(String email, Long planId) {
        YTodoUser user = getUserByEmail(email);
        return sessionRepository.findAllByUserAndPlanId(user, planId);
    }

    /**
     * 특정 ID와 사용자에 해당하는 Pomodoro 세션 조회
     *
     * @param id    세션 ID
     * @param email 사용자 이메일
     * @return Pomodoro 세션
     */
    @Transactional(readOnly = true)
    public YTodoPomodoroSession findByIdAndUser(Long id, String email) {
        YTodoUser user = getUserByEmail(email);
        return sessionRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("Pomodoro session not found with ID " + id + " for user."));
    }

    /**
     * Pomodoro 세션 저장
     *
     * @param session 저장할 세션
     * @param email   사용자 이메일
     * @return 저장된 세션
     */
    @Transactional
    public YTodoPomodoroSession save(YTodoPomodoroSession session, String email) {
        YTodoUser user = getUserByEmail(email);
        session.setUser(user);
        return sessionRepository.save(session);
    }

    /**
     * Pomodoro 세션 업데이트
     *
     * @param id      업데이트할 세션 ID
     * @param session 업데이트된 세션 객체
     * @param email   사용자 이메일
     * @return 업데이트된 세션 객체
     */
    @Transactional
    public YTodoPomodoroSession update(Long id, YTodoPomodoroSession session, String email) {
        YTodoPomodoroSession existingSession = findByIdAndUser(id, email);

        existingSession.setName(session.getName());
        existingSession.setPlan(session.getPlan());
        existingSession.setEndTime(session.getEndTime());
        existingSession.setStatus(session.getStatus());
        existingSession.setFocusDuration(session.getFocusDuration());
        existingSession.setShortBreakDuration(session.getShortBreakDuration());
        existingSession.setLongBreakDuration(session.getLongBreakDuration());
        existingSession.setSessionNumber(session.getSessionNumber());
        existingSession.setTotalSessions(session.getTotalSessions());
        existingSession.setMemo(session.getMemo());
        existingSession.setCompleted(session.isCompleted());

        return sessionRepository.save(existingSession);
    }

    /**
     * Pomodoro 세션 삭제
     *
     * @param id    삭제할 세션 ID
     * @param email 사용자 이메일
     */
    @Transactional
    public void delete(Long id, String email) {
        YTodoPomodoroSession session = findByIdAndUser(id, email);
        sessionRepository.delete(session);
    }
}
