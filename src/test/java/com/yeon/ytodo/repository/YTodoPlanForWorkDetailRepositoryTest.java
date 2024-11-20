package com.yeon.ytodo.repository;

import com.yeon.ytodo.model.YTodoPlanForWorkDetail;
import com.yeon.ytodo.model.YTodoPlanForWorkForm;
import com.yeon.ytodo.model.YTodoUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class YTodoPlanForWorkDetailRepositoryTest {

    @Autowired
    private YTodoPlanForWorkDetailRepository detailRepository;

    @Autowired
    private YTodoPlanForWorkFormRepository formRepository;

    @Autowired
    private YTodoUserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm form = createForm(user);

        YTodoPlanForWorkDetail detail = new YTodoPlanForWorkDetail();
        detail.setName("Test Detail");
        detail.setUser(user);
        detail.setForm(form);
        detail = detailRepository.save(detail);

        Optional<YTodoPlanForWorkDetail> foundDetail = detailRepository.findById(detail.getId());
        assertThat(foundDetail).isPresent();
        assertThat(foundDetail.get().getName()).isEqualTo("Test Detail");
    }

    @Test
    void testFindAllByUser() {
        YTodoUser user = createUser();
        YTodoPlanForWorkForm form = createForm(user);

        YTodoPlanForWorkDetail detail1 = createDetail(user, form, "Detail 1");
        YTodoPlanForWorkDetail detail2 = createDetail(user, form, "Detail 2");

        List<YTodoPlanForWorkDetail> details = detailRepository.findAllByUser(user);
        assertThat(details).hasSize(2);
        assertThat(details.get(0).getName()).isEqualTo(detail1.getName());
        assertThat(details.get(1).getName()).isEqualTo(detail2.getName());
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

    private YTodoPlanForWorkForm createForm(YTodoUser user) {
        YTodoPlanForWorkForm form = new YTodoPlanForWorkForm();
        form.setName("Test Form");
        form.setUser(user);
        return formRepository.save(form);
    }

    private YTodoPlanForWorkDetail createDetail(YTodoUser user, YTodoPlanForWorkForm form, String name) {
        YTodoPlanForWorkDetail detail = new YTodoPlanForWorkDetail();
        detail.setName(name);
        detail.setUser(user);
        detail.setForm(form);
        return detailRepository.save(detail);
    }
}
