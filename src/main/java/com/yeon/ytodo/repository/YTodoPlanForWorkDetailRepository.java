package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.model.YTodoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YTodoPlanForWorkDetailRepository extends JpaRepository<YTodoPlanForWorkDetail, Long> {

    // 특정 ID와 사용자에 해당하는 엔티티 조회
    Optional<YTodoPlanForWorkDetail> findByIdAndUser(Long id, YTodoUser user);

    // 특정 사용자에 해당하는 모든 엔티티 조회
    List<YTodoPlanForWorkDetail> findAllByUser(YTodoUser user);
}
