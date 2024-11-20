package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class YTodoLoginController {

    @Autowired
    private YTodoUserRepository YTodoUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerYTodoUser(@RequestBody YTodoUser YTodoUser) {
        YTodoUser savedTodoUser = null;
        ResponseEntity response = null;
        try {
            String hashPassword = passwordEncoder.encode(YTodoUser.getPassword());
            YTodoUser.setPassword(hashPassword);
            savedTodoUser = YTodoUserRepository.save(YTodoUser);
            if (savedTodoUser.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Given user details are successfully registered");
            }
        } catch (Exception ex) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An exception occured due to " + ex.getMessage());
        }
        return response;
    }

    @RequestMapping("/auth")
    public YTodoUser getUserDetailsAfterLogin(Authentication authentication) {
        List<YTodoUser> TodoUsers = YTodoUserRepository.findByEmail(authentication.getName());
        if (!TodoUsers.isEmpty()) {
            return TodoUsers.get(0);
        } else {
            return null;
        }

    }

}