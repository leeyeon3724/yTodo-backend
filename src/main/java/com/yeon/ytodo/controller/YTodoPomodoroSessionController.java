package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPomodoroSession;
import com.yeon.ytodo.service.YTodoPomodoroSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pomodoro-sessions")
@RequiredArgsConstructor
public class YTodoPomodoroSessionController {

    private final YTodoPomodoroSessionService sessionService;

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 이메일 반환
    }

    @GetMapping
    public ResponseEntity<List<YTodoPomodoroSession>> getAllSessions() {
        String email = getAuthenticatedUserEmail();
        List<YTodoPomodoroSession> sessions = sessionService.findAllByUser(email);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<YTodoPomodoroSession>> getSessionsByPlan(@PathVariable Long planId) {
        String email = getAuthenticatedUserEmail();
        List<YTodoPomodoroSession> sessions = sessionService.findAllByUserAndPlanId(email, planId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<YTodoPomodoroSession> getSessionById(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        YTodoPomodoroSession session = sessionService.findByIdAndUser(id, email);
        return ResponseEntity.ok(session);
    }

    @PostMapping
    public ResponseEntity<YTodoPomodoroSession> createSession(@RequestBody YTodoPomodoroSession session) {
        String email = getAuthenticatedUserEmail();
        YTodoPomodoroSession savedSession = sessionService.save(session, email);
        return ResponseEntity.ok(savedSession);
    }

    @PutMapping("/{id}")
    public ResponseEntity<YTodoPomodoroSession> updateSession(
            @PathVariable Long id,
            @RequestBody YTodoPomodoroSession session
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPomodoroSession updatedSession = sessionService.update(id, session, email);
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSession(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        sessionService.delete(id, email);
        return ResponseEntity.ok("Pomodoro session deleted successfully");
    }
}
