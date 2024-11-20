package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YTodoPlanForWorkFormService {

    private final YTodoPlanForWorkFormRepository formRepository;
    private final YTodoUserRepository userRepository;

    /**
     * 이메일로 사용자 조회
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
     * 모든 WorkForm 조회 (현재 인증된 사용자 기준)
     *
     * @param email 사용자 이메일
     * @return 사용자의 모든 WorkForm 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPlanForWorkForm> findAllByUser(String email) {
        YTodoUser user = getUserByEmail(email);
        return formRepository.findAllByUser(user);
    }

    /**
     * 특정 ID와 사용자에 해당하는 WorkForm 조회
     *
     * @param id    WorkForm ID
     * @param email 사용자 이메일
     * @return WorkForm 객체
     */
    @Transactional(readOnly = true)
    public YTodoPlanForWorkForm findByIdAndUser(Long id, String email) {
        YTodoUser user = getUserByEmail(email);
        return formRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("WorkForm not found with ID " + id + " for user."));
    }

    /**
     * WorkForm 저장
     *
     * @param form  저장할 WorkForm
     * @param email 사용자 이메일
     * @return 저장된 WorkForm
     */
    @Transactional
    public YTodoPlanForWorkForm save(YTodoPlanForWorkForm form, String email) {
        YTodoUser user = getUserByEmail(email);
        form.setUser(user); // 사용자 설정
        return formRepository.save(form);
    }

    /**
     * WorkForm 업데이트
     *
     * @param id    업데이트할 WorkForm ID
     * @param form  새로운 데이터로 업데이트된 WorkForm 객체
     * @param email 사용자 이메일
     * @return 업데이트된 WorkForm 객체
     */
    @Transactional
    public YTodoPlanForWorkForm update(Long id, YTodoPlanForWorkForm form, String email) {
        YTodoPlanForWorkForm existingForm = findByIdAndUser(id, email);

        existingForm.setName(form.getName());
        existingForm.setContent(form.getContent());
        existingForm.setStartDate(form.getStartDate());
        existingForm.setDueDate(form.getDueDate());
        existingForm.setCompletedAt(form.getCompletedAt());
        existingForm.setProgress(form.getProgress());
        existingForm.setPriority(form.getPriority());
        existingForm.setStatus(form.getStatus());
        existingForm.setEffortEstimate(form.getEffortEstimate());
        existingForm.setTimeSpent(form.getTimeSpent());
        existingForm.setReminderSet(form.isReminderSet());
        existingForm.setDetails(form.getDetails());

        return formRepository.save(existingForm);
    }

    /**
     * WorkForm 삭제
     *
     * @param id    삭제할 WorkForm ID
     * @param email 사용자 이메일
     */
    @Transactional
    public void delete(Long id, String email) {
        YTodoPlanForWorkForm form = findByIdAndUser(id, email);
        formRepository.delete(form);
    }
}
