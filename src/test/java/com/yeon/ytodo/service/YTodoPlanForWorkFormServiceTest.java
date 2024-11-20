package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class YTodoPlanForWorkFormServiceTest {

    @Mock
    private YTodoPlanForWorkFormRepository formRepository;

    @Mock
    private YTodoUserRepository userRepository;

    @InjectMocks
    private YTodoPlanForWorkFormService formService;

    YTodoPlanForWorkFormServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private YTodoUser mockUser(String email) {
        YTodoUser user = new YTodoUser();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail(email);
        return user;
    }

    private YTodoPlanForWorkForm mockForm(YTodoUser user, String name) {
        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setId(1L);
        form.setName(name);
        form.setUser(user);
        form.setStartDate(LocalDateTime.now());
        form.setDueDate(LocalDateTime.now().plusDays(1));
        form.setCompletedAt(null);
        form.setProgress(50);
        return form;
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = mockUser("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findAllByUser(user)).thenReturn(Collections.singletonList(mockForm(user, "Form 1")));

        List<YTodoPlanForWorkForm> forms = formService.findAllByUser("test@example.com");
        assertThat(forms).hasSize(1);
        assertThat(forms.get(0).getName()).isEqualTo("Form 1");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testFindByIdAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm form = mockForm(user, "Test Form");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(form));

        YTodoPlanForWorkForm foundForm = formService.findByIdAndUser(1L, "test@example.com");
        assertThat(foundForm.getName()).isEqualTo("Test Form");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findByIdAndUser(1L, user);
    }

    @Test
    void testSave() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm form = mockForm(user, "New Form");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.save(form)).thenReturn(form);

        YTodoPlanForWorkForm savedForm = formService.save(form, "test@example.com");
        assertThat(savedForm.getName()).isEqualTo("New Form");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).save(form);
    }

    @Test
    void testUpdate() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm existingForm = mockForm(user, "Old Form");
        YTodoPlanForWorkForm updatedForm = new YTodoPlanForWorkForm();
        updatedForm.setName("Updated Form");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(existingForm));
        when(formRepository.save(existingForm)).thenReturn(existingForm);

        YTodoPlanForWorkForm result = formService.update(1L, updatedForm, "test@example.com");
        assertThat(result.getName()).isEqualTo("Updated Form");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findByIdAndUser(1L, user);
        verify(formRepository, times(1)).save(existingForm);
    }

    @Test
    void testDelete() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm form = mockForm(user, "Form to Delete");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(form));
        doNothing().when(formRepository).delete(form);

        formService.delete(1L, "test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findByIdAndUser(1L, user);
        verify(formRepository, times(1)).delete(form);
    }

    @Test
    void testFindByIdAndUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> formService.findByIdAndUser(1L, "test@example.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with email");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, never()).findByIdAndUser(anyLong(), any());
    }
}
