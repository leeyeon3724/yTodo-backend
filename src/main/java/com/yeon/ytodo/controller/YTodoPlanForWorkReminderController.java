package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkReminder;
import com.yeon.ytodo.service.YTodoPlanForWorkReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/work-reminders")
@RequiredArgsConstructor
public class YTodoPlanForWorkReminderController {

    private final YTodoPlanForWorkReminderService reminderService;

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<YTodoPlanForWorkReminder>> getRemindersByPlan(@PathVariable Long planId) {
        String email = getAuthenticatedUserEmail();
        List<YTodoPlanForWorkReminder> reminders = reminderService.findAllByPlanIdAndUser(planId, email);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/after")
    public ResponseEntity<List<YTodoPlanForWorkReminder>> getRemindersAfter(@RequestParam LocalDateTime time) {
        String email = getAuthenticatedUserEmail();
        List<YTodoPlanForWorkReminder> reminders = reminderService.findAllByReminderTimeAfterAndUser(time, email);
        return ResponseEntity.ok(reminders);
    }

    @PostMapping("/plan/{planId}")
    public ResponseEntity<YTodoPlanForWorkReminder> createReminder(
            @PathVariable Long planId,
            @RequestBody YTodoPlanForWorkReminder reminder
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkReminder savedReminder = reminderService.save(planId, reminder, email);
        return ResponseEntity.ok(savedReminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<YTodoPlanForWorkReminder> updateReminder(
            @PathVariable Long id,
            @RequestBody YTodoPlanForWorkReminder reminder
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkReminder updatedReminder = reminderService.update(id, reminder, email);
        return ResponseEntity.ok(updatedReminder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReminder(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        reminderService.delete(id, email);
        return ResponseEntity.ok("Reminder deleted successfully");
    }
}
