package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.service.YTodoPlanForWorkDetailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class YTodoPlanForWorkDetailControllerTest {

    @Mock
    private YTodoPlanForWorkDetailService detailService;

    @InjectMocks
    private YTodoPlanForWorkDetailController detailController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    YTodoPlanForWorkDetailControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthenticatedUser(String email) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllWorkDetails() {
        mockAuthenticatedUser("test@example.com");

        YTodoPlanForWorkDetail detail = new YTodoPlanForWorkDetail();
        detail.setName("Test Detail");

        when(detailService.findAllByUser("test@example.com")).thenReturn(Collections.singletonList(detail));

        ResponseEntity<List<YTodoPlanForWorkDetail>> response = detailController.getAllWorkDetails();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Test Detail");
        verify(detailService, times(1)).findAllByUser("test@example.com");
    }

    @Test
    void testCreateWorkDetail() {
        mockAuthenticatedUser("test@example.com");

        YTodoPlanForWorkDetail detail = new YTodoPlanForWorkDetail();
        detail.setName("New Detail");

        when(detailService.save(1L, detail, "test@example.com")).thenReturn(detail);

        ResponseEntity<YTodoPlanForWorkDetail> response = detailController.createWorkDetail(1L, detail);
        assertThat(response.getBody().getName()).isEqualTo("New Detail");
        verify(detailService, times(1)).save(1L, detail, "test@example.com");
    }
}
