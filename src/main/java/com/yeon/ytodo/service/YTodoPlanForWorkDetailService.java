package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkDetailRepository;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YTodoPlanForWorkDetailService {

    private final YTodoPlanForWorkDetailRepository detailRepository;
    private final YTodoPlanForWorkFormRepository formRepository;
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
     * 특정 사용자에 해당하는 모든 WorkDetail 조회
     *
     * @param email 사용자 이메일
     * @return 사용자의 WorkDetail 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPlanForWorkDetail> findAllByUser(String email) {
        YTodoUser user = getUserByEmail(email);
        return detailRepository.findAllByUser(user);
    }

    /**
     * 특정 Form에 속한 모든 WorkDetail 조회
     *
     * @param formId Form ID
     * @param email  사용자 이메일
     * @return 해당 Form에 속한 WorkDetail 리스트
     */
    @Transactional(readOnly = true)
    public List<YTodoPlanForWorkDetail> findAllByFormIdAndUser(Long formId, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlanForWorkForm form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("WorkForm not found with ID " + formId));
        if (!form.getUser().equals(user)) {
            throw new EntityNotFoundException("WorkForm not found for the authenticated user.");
        }
        return form.getDetails();
    }

    /**
     * 특정 ID와 사용자에 해당하는 WorkDetail 조회
     *
     * @param id    WorkDetail ID
     * @param email 사용자 이메일
     * @return WorkDetail 객체
     */
    @Transactional(readOnly = true)
    public YTodoPlanForWorkDetail findByIdAndUser(Long id, String email) {
        YTodoUser user = getUserByEmail(email);
        return detailRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new EntityNotFoundException("WorkDetail not found with ID " + id + " for user."));
    }

    /**
     * WorkDetail 저장
     *
     * @param formId WorkForm ID
     * @param detail 저장할 WorkDetail
     * @param email  사용자 이메일
     * @return 저장된 WorkDetail
     */
    @Transactional
    public YTodoPlanForWorkDetail save(Long formId, YTodoPlanForWorkDetail detail, String email) {
        YTodoUser user = getUserByEmail(email);
        YTodoPlanForWorkForm form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("WorkForm not found with ID " + formId));
        if (!form.getUser().equals(user)) {
            throw new EntityNotFoundException("WorkForm not found for the authenticated user.");
        }
        detail.setForm(form);
        detail.setUser(user);
        return detailRepository.save(detail);
    }

    /**
     * WorkDetail 업데이트
     *
     * @param id     업데이트할 WorkDetail ID
     * @param detail 업데이트된 WorkDetail 객체
     * @param email  사용자 이메일
     * @return 업데이트된 WorkDetail 객체
     */
    @Transactional
    public YTodoPlanForWorkDetail update(Long id, YTodoPlanForWorkDetail detail, String email) {
        YTodoPlanForWorkDetail existingDetail = findByIdAndUser(id, email);

        existingDetail.setName(detail.getName());
        existingDetail.setContent(detail.getContent());
        existingDetail.setStartDate(detail.getStartDate());
        existingDetail.setDueDate(detail.getDueDate());
        existingDetail.setCompletedAt(detail.getCompletedAt());
        existingDetail.setProgress(detail.getProgress());
        existingDetail.setPriority(detail.getPriority());
        existingDetail.setStatus(detail.getStatus());
        existingDetail.setEffortEstimate(detail.getEffortEstimate());
        existingDetail.setTimeSpent(detail.getTimeSpent());
        existingDetail.setReminderSet(detail.isReminderSet());

        return detailRepository.save(existingDetail);
    }

    /**
     * WorkDetail 삭제
     *
     * @param id    삭제할 WorkDetail ID
     * @param email 사용자 이메일
     */
    @Transactional
    public void delete(Long id, String email) {
        YTodoPlanForWorkDetail detail = findByIdAndUser(id, email);
        detailRepository.delete(detail);
    }
}
