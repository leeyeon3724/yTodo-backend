package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YTodoPlanForWorkFormRepository extends JpaRepository<YTodoPlanForWorkForm, Long> {

    // 특정 ID와 사용자에 해당하는 엔티티 조회
    Optional<YTodoPlanForWorkForm> findByIdAndUser(Long id, YTodoUser user);

    // 특정 사용자에 해당하는 모든 엔티티 조회
    List<YTodoPlanForWorkForm> findAllByUser(YTodoUser user);
}
