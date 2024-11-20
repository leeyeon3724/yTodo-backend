package com.yeon.ytodo.config;

import com.yeon.ytodo.model.YTodoAuthority;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class YTodoAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private YTodoUserRepository YTodoUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<YTodoUser> YTodoUser = YTodoUserRepository.findByName(username);

        if (YTodoUser.isEmpty()) {
            YTodoUser = YTodoUserRepository.findByEmail(username);
        }

        if (!YTodoUser.isEmpty()) {
            if (passwordEncoder.matches(password, YTodoUser.get(0).getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, password, getGrantedAuthorities(YTodoUser.get(0).getAuthorities()));
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<YTodoAuthority> yTodoAuthorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (YTodoAuthority YTodoAuthority : yTodoAuthorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(YTodoAuthority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
