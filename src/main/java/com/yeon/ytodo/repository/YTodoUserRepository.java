package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YTodoUserRepository extends CrudRepository<YTodoUser, Long> {

    List<YTodoUser> findByEmail(String email);

    List<YTodoUser> findByName(String name);

}