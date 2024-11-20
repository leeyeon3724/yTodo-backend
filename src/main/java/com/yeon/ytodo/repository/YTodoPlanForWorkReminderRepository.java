package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.model.YTodoPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YTodoPlanForWorkReminderRepository extends JpaRepository<YTodoPlanForWorkReminder, Long> {

    // 특정 Plan과 관련된 모든 리마인더 조회
    List<YTodoPlanForWorkReminder> findAllByPlan(YTodoPlan plan);

    // 특정 Plan ID와 관련된 모든 리마인더 조회
    List<YTodoPlanForWorkReminder> findAllByPlanId(Long planId);

    // 특정 시간 이후의 리마인더 조회
    List<YTodoPlanForWorkReminder> findAllByReminderTimeAfter(java.time.LocalDateTime time);

}
