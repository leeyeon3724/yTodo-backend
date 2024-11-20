package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.service.YTodoPlanForWorkFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/work-forms")
@RequiredArgsConstructor
public class YTodoPlanForWorkFormController {

    private final YTodoPlanForWorkFormService formService;

    /**
     * 현재 인증된 사용자의 이메일을 가져오는 헬퍼 메서드
     */
    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // 이메일 반환
    }

    /**
     * 현재 인증된 사용자의 모든 WorkForm 조회
     *
     * @return 사용자의 모든 WorkForm 리스트
     */
    @GetMapping
    public ResponseEntity<List<YTodoPlanForWorkForm>> getAllWorkForms() {
        String email = getAuthenticatedUserEmail();
        List<YTodoPlanForWorkForm> forms = formService.findAllByUser(email);
        return ResponseEntity.ok(forms);
    }

    /**
     * 특정 ID와 사용자에 해당하는 WorkForm 조회
     *
     * @param id WorkForm ID
     * @return 해당 WorkForm 객체
     */
    @GetMapping("/{id}")
    public ResponseEntity<YTodoPlanForWorkForm> getWorkFormByIdAndUser(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkForm form = formService.findByIdAndUser(id, email);
        return ResponseEntity.ok(form);
    }

    /**
     * 새로운 WorkForm 생성
     *
     * @param form 저장할 WorkForm 객체
     * @return 생성된 WorkForm 객체
     */
    @PostMapping
    public ResponseEntity<YTodoPlanForWorkForm> createWorkForm(@RequestBody YTodoPlanForWorkForm form) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkForm savedForm = formService.save(form, email);
        return ResponseEntity.ok(savedForm);
    }

    /**
     * WorkForm 업데이트
     *
     * @param id   업데이트할 WorkForm ID
     * @param form 업데이트된 WorkForm 객체
     * @return 업데이트된 WorkForm 객체
     */
    @PutMapping("/{id}")
    public ResponseEntity<YTodoPlanForWorkForm> updateWorkForm(
            @PathVariable Long id,
            @RequestBody YTodoPlanForWorkForm form
    ) {
        String email = getAuthenticatedUserEmail();
        YTodoPlanForWorkForm updatedForm = formService.update(id, form, email);
        return ResponseEntity.ok(updatedForm);
    }

    /**
     * WorkForm 삭제
     *
     * @param id 삭제할 WorkForm ID
     * @return 성공 여부 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkForm(@PathVariable Long id) {
        String email = getAuthenticatedUserEmail();
        formService.delete(id, email);
        return ResponseEntity.ok("WorkForm deleted successfully");
    }
}
