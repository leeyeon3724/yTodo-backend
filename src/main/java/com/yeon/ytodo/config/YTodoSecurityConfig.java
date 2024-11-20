package com.yeon.ytodo.config;

import com.yeon.ytodo.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity // IntelliJ 오류 해결
@Configuration
public class YTodoSecurityConfig {

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    protected SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOriginPatterns(Collections.singletonList("*"));                                // 모든 도메인 허용
//                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));                 // 단일 도메인만 가능
//                        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000"));    // 허용할 도메인 목록
                        config.setAllowedMethods(Collections.singletonList("*"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setExposedHeaders(Arrays.asList("Authorization"));                   // Authorization 헤더 안에 JWT 토큰 값을 함께 전송
                        config.setMaxAge(3600L);                                                    // 1시간 동안 유지
                        return config;
                    }}))
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
//                .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())       // 모든 요청에 대해 보호
//                .authorizeHttpRequests((requests) -> requests.anyRequest().denyAll())             // 모든 요청에 대해 거부
//                .authorizeHttpRequests((requests) -> requests.anyRequest().permitAll())           // 모든 요청에 대해 허용
                .authorizeHttpRequests((requests) -> requests                                       // 특정 URL 경로에 대해 선별적으로 보호한다. (인증 & 인가)
//                .requestMatchers("/authTest").hasAuthority("USER")                                // 권한부여/인가 규칙 설정 (인가 규칙 이름은 자유롭게 설정 가능)
//                .requestMatchers("/allowTest").hasAnyAuthority("USER", "VIEWER")
//                .requestMatchers("/denyTest").hasAuthority("VIEWER")
//                .requestMatchers("/authTest").hasRole("ADMIN")                                    // 역할 규칙 설정 (역할 이름은 데이터베이스의 컬럼명과는 다르게 ROLE_로 시작하지 않는 점에 유의!)
//                .requestMatchers("/allowTest").hasAnyRole("ADMIN", "USER")                        // 권한부여/인가 규칙과 역할 규칙 설정에 대한 Spring Security의 프로그래밍적 구현에는 큰 차이가 없음.
//                .requestMatchers("/denyTest").hasRole("TESTER")                                   // 만약 인가 규칙이 설정되지 않았다면, 일단 인증된 사용자는 모든 API 경로에 접근할 수 있다.
                .requestMatchers("/auth", "/api/**").authenticated()
//                .requestMatchers("/denyTest").denyAll()
                .requestMatchers("/register", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll())
                .csrf(csrf -> csrf.disable())
                .httpBasic(withDefaults());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
