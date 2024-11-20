package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.service.YTodoPlanForWorkDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/work-details")
@RequiredArgsConstructor
public class YTodoPlanForWorkDetailController {

    private final YTodoPlanForWorkDetailService detailService;

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 이메일 반환
    }

    @GetMapping
    public ResponseEntity<List<YTodoPlanForWorkDetail>> getAllWorkDetails() {
        String email = getAuthenticatedUserEmail();
        List<YTodoPlanForWorkDetail> details = detailService.findAllByUser(email);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<List<YTodoPlanForWorkDetail>> getWorkDetailsByFormId(@PathVariable Long formId) {
        String email = getAuthenticatedUserEmail();
        List<YTodoPlanForWorkDetail> details = detailService.findAllByFormIdAndUser(formId, email);
        return ResponseEntity.ok(details);
    }

    @GetMapping("/{id}")
    public ResponseEntity<YTodoPlanForWorkDetail> getWorkDetailByIdAndUser(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkDetail detail = detailService.findByIdAndUser(id, email);
        return ResponseEntity.ok(detail);
    }

    @PostMapping("/form/{formId}")
    public ResponseEntity<YTodoPlanForWorkDetail> createWorkDetail(
            @PathVariable Long formId,
            @RequestBody YTodoPlanForWorkDetail detail
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkDetail savedDetail = detailService.save(formId, detail, email);
        return ResponseEntity.ok(savedDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<YTodoPlanForWorkDetail> updateWorkDetail(
            @PathVariable Long id,
            @RequestBody YTodoPlanForWorkDetail detail
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkDetail updatedDetail = detailService.update(id, detail, email);
        return ResponseEntity.ok(updatedDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkDetail(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        detailService.delete(id, email);
        return ResponseEntity.ok("WorkDetail deleted successfully");
    }
}
