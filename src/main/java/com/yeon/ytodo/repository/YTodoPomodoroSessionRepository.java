package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.model.YTodoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YTodoPomodoroSessionRepository extends JpaRepository<YTodoPomodoroSession, Long> {

    // 특정 사용자에 해당하는 모든 세션 조회
    List<YTodoPomodoroSession> findAllByUser(YTodoUser user);

    // 특정 사용자의 특정 Plan에 속한 세션 조회
    List<YTodoPomodoroSession> findAllByUserAndPlanId(YTodoUser user, Long planId);

    // 특정 ID와 사용자에 해당하는 세션 조회
    Optional<YTodoPomodoroSession> findByIdAndUser(Long id, YTodoUser user);
}
