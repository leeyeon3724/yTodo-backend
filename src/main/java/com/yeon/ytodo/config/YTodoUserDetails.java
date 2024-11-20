package com.yeon.ytodo.config;

import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class YTodoUserDetails implements UserDetailsService {

    @Autowired
    private YTodoUserRepository YTodoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        YTodoUser YTodoUser = YTodoUserRepository.findByName(username).stream().findFirst().orElse(null);
        if (YTodoUser == null) {
            YTodoUser = YTodoUserRepository.findByEmail(username).stream().findFirst().orElse(null);
        }
        if (YTodoUser == null) {
            throw new UsernameNotFoundException("User details not found for the user: " + username);
        }
        String userName = YTodoUser.getEmail();
        String password = YTodoUser.getPassword();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(YTodoUser.getRole()));

        return new User(userName, password, authorities);
    }


}
