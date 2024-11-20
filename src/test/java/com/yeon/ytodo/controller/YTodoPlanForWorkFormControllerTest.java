package com.yeon.ytodo.controller;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.service.YTodoPlanForWorkFormService;
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

class YTodoPlanForWorkFormControllerTest {

    @Mock
    private YTodoPlanForWorkFormService formService;

    @InjectMocks
    private YTodoPlanForWorkFormController formController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    YTodoPlanForWorkFormControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    private void mockAuthenticatedUser(String email) {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetAllWorkForms() {
        mockAuthenticatedUser("test@example.com");

        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setName("Test Form");

        when(formService.findAllByUser("test@example.com")).thenReturn(Collections.singletonList(form));

        ResponseEntity<List<YTodoPlanForWorkForm>> response = formController.getAllWorkForms();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Test Form");
        verify(formService, times(1)).findAllByUser("test@example.com");
    }

    @Test
    void testCreateWorkForm() {
        mockAuthenticatedUser("test@example.com");

        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setName("New Form");

        when(formService.save(form, "test@example.com")).thenReturn(form);

        ResponseEntity<YTodoPlanForWorkForm> response = formController.createWorkForm(form);
        assertThat(response.getBody().getName()).isEqualTo("New Form");
        verify(formService, times(1)).save(form, "test@example.com");
    }
}
