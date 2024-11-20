package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class YTodoPlanForWorkFormRepositoryTest {

    @Autowired
    private YTodoPlanForWorkFormRepository formRepository;

    @Autowired
    private YTodoUserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        YTodoUser user = createUser();

        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setName("Test Form");
        form.setUser(user);
        form = formRepository.save(form);

        Optional<YTodoPlanForWorkForm> foundForm = formRepository.findById(form.getId());
        assertThat(foundForm).isPresent();
        assertThat(foundForm.get().getName()).isEqualTo("Test Form");
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = createUser();

        YTodoPlanForWorkForm form1 = createForm(user, "Form 1");
        YTodoPlanForWorkForm form2 = createForm(user, "Form 2");

        List<YTodoPlanForWorkForm> forms = formRepository.findAllByUser(user);
        assertThat(forms).hasSize(2);
        assertThat(forms.get(0).getName()).isEqualTo(form1.getName());
        assertThat(forms.get(1).getName()).isEqualTo(form2.getName());
    }

    private YTodoUser createUser() {
        YTodoUser user = new YTodoUser();
        user.setName("testuser");
        user.setEmail("testuser@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    private YTodoPlanForWorkForm createForm(YTodoUser user, String name) {
        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setName(name);
        form.setUser(user);
        return formRepository.save(form);
    }
}
