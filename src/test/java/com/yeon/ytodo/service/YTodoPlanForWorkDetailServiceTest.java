package com.yeon.ytodo.service;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import com.yeon.ytodo.repository.YTodoPlanForWorkDetailRepository;
import com.yeon.ytodo.repository.YTodoPlanForWorkFormRepository;
import com.yeon.ytodo.repository.YTodoUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class YTodoPlanForWorkDetailServiceTest {

    @Mock
    private YTodoPlanForWorkDetailRepository detailRepository;

    @Mock
    private YTodoPlanForWorkFormRepository formRepository;

    @Mock
    private YTodoUserRepository userRepository;

    @InjectMocks
    private YTodoPlanForWorkDetailService detailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private YTodoUser mockUser(String email) {
        YTodoUser user = new YTodoUser();
        user.setId(1L);
        user.setName("testuser");
        user.setEmail(email);
        return user;
    }

    private YTodoPlanForWorkForm mockForm(YTodoUser user) {
        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setId(1L);
        form.setName("Test Form");
        form.setUser(user);
        return form;
    }

    private YTodoPlanForWorkDetail mockDetail(YTodoPlanForWorkForm form, YTodoUser user) {
        YTodoPlanForWorkDetail detail = new YTodoPlanForWorkDetail();
        detail.setId(1L);
        detail.setName("Test Detail");
        detail.setForm(form);
        detail.setUser(user);
        return detail;
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = mockUser("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(detailRepository.findAllByUser(user)).thenReturn(Collections.singletonList(mockDetail(null, user)));

        List<YTodoPlanForWorkDetail> details = detailService.findAllByUser("test@example.com");
        assertThat(details).hasSize(1);
        assertThat(details.get(0).getName()).isEqualTo("Test Detail");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(detailRepository, times(1)).findAllByUser(user);
    }

    @Test
    void testFindByIdAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkDetail detail = mockDetail(null, user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(detailRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(detail));

        YTodoPlanForWorkDetail foundDetail = detailService.findByIdAndUser(1L, "test@example.com");
        assertThat(foundDetail.getName()).isEqualTo("Test Detail");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(detailRepository, times(1)).findByIdAndUser(1L, user);
    }

    @Test
    void testSave() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm form = mockForm(user);
        YTodoPlanForWorkDetail detail = mockDetail(form, user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findById(1L)).thenReturn(java.util.Optional.of(form));
        when(detailRepository.save(detail)).thenReturn(detail);

        YTodoPlanForWorkDetail savedDetail = detailService.save(1L, detail, "test@example.com");
        assertThat(savedDetail.getName()).isEqualTo("Test Detail");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findById(1L);
        verify(detailRepository, times(1)).save(detail);
    }

    @Test
    void testUpdate() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkDetail existingDetail = mockDetail(null, user);
        YTodoPlanForWorkDetail updatedDetail = new YTodoPlanForWorkDetail();
        updatedDetail.setName("Updated Detail");

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(detailRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(existingDetail));
        when(detailRepository.save(existingDetail)).thenReturn(existingDetail);

        YTodoPlanForWorkDetail result = detailService.update(1L, updatedDetail, "test@example.com");
        assertThat(result.getName()).isEqualTo("Updated Detail");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(detailRepository, times(1)).findByIdAndUser(1L, user);
        verify(detailRepository, times(1)).save(existingDetail);
    }

    @Test
    void testDelete() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkDetail detail = mockDetail(null, user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(detailRepository.findByIdAndUser(1L, user)).thenReturn(java.util.Optional.of(detail));
        doNothing().when(detailRepository).delete(detail);

        detailService.delete(1L, "test@example.com");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(detailRepository, times(1)).findByIdAndUser(1L, user);
        verify(detailRepository, times(1)).delete(detail);
    }

    @Test
    void testFindAllByFormIdAndUser() {
        YTodoUser user = mockUser("test@example.com");
        YTodoPlanForWorkForm form = mock(YTodoPlanForWorkForm.class);
        YTodoPlanForWorkDetail detail = mockDetail(form, user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(List.of(user));
        when(formRepository.findById(1L)).thenReturn(java.util.Optional.of(form));
        when(form.getUser()).thenReturn(user);
        when(form.getDetails()).thenReturn(Collections.singletonList(detail));

        List<YTodoPlanForWorkDetail> details = detailService.findAllByFormIdAndUser(1L, "test@example.com");
        assertThat(details).hasSize(1);
        assertThat(details.get(0).getName()).isEqualTo("Test Detail");

        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(formRepository, times(1)).findById(1L);
        verify(form, times(1)).getUser();
        verify(form, times(1)).getDetails();
    }
}
