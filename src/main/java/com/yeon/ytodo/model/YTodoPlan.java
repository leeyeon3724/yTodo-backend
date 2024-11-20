package com.yeon.ytodo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // 전략: SINGLE_TABLE, TABLE_PER_CLASS, JOINED 중 선택
@Getter
@Setter
@NoArgsConstructor
public abstract class YTodoPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private YTodoUser user;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "entityId", referencedColumnName = "id")
    private List<YTodoAttachment> attachments;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // 1. 시간 관련 속성
    private LocalDateTime startDate;   // 시작 날짜
    private LocalDateTime dueDate;      // 마감 기한
    private LocalDateTime completedAt; // 완료 시간

    // 2. 진행 상태 및 진척도
    private int progress;               // 진행 상황 (0~100%)

    @Enumerated(EnumType.STRING)
    private YTodoPlanForWorkPriority priority;   // IMPORTANT, NOT_IMPORTANT

    @Enumerated(EnumType.STRING)
    private YTodoPlanForWorkStatus status;       // TO_DO, DOING, DONE

    // 3. 시간 및 작업량, 결과 품질 측정
    private double effortEstimate;     // 예상 작업 시간
    private double timeSpent;          // 실제 소요 시간

    // 4. 알림 기능
    private boolean reminderSet;       // 리마인더 설정 여부

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<YTodoPlanForWorkReminder> reminders;  // 리마인더 리스트
}
