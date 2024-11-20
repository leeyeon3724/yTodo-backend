package com.yeon.ytodo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class YTodoPomodoroSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 포모도로 세션 이름
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private YTodoUser user;

    // 특정 작업과 연결되지 않을 수도 있으므로 nullable 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = true)
    private YTodoPlan plan;

    // 세션 시작 시간
    @Column(nullable = false)
    private LocalDateTime startTime;

    // 세션 종료 시간
    @Column(nullable = false)
    private LocalDateTime endTime;

    // 각 포모도로 세션의 상태 (e.g., PENDING, IN_PROGRESS, COMPLETED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private YTodoPomodoroStatus status = YTodoPomodoroStatus.PENDING; // 기본값 설정

    // 집중 시간 (단위: 분, 기본값: 25)
    @Column(nullable = false)
    private int focusDuration = 25;

    // 짧은 휴식 시간 (단위: 분, 기본값: 5)
    @Column(nullable = false)
    private int shortBreakDuration = 5;

    // 긴 휴식 시간 (단위: 분, 기본값: 15)
    @Column(nullable = false)
    private int longBreakDuration = 15;

    // 세션 중 몇 번째 포모도로인지 추적 (e.g., 1~4 사이의 값)
    @Column(nullable = false)
    private int sessionNumber;

    // 사용자가 요청한 전체 포모도로 세션의 수
    @Column(nullable = false)
    private int totalSessions;

    // 세션에 대한 메모
    @Column(columnDefinition = "TEXT", nullable = true)
    private String memo;

    // 세션이 완료되었는지 여부
    @Column(nullable = false)
    private boolean isCompleted = false;

    @PrePersist
    protected void onCreate() {
        this.startTime = LocalDateTime.now();
    }
}
