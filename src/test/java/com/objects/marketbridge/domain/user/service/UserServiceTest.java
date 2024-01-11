package com.objects.marketbridge.domain.user.service;


import com.objects.marketbridge.domain.model.User;
import com.objects.marketbridge.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    private User existingUser; // 생성된 사용자 객체를 저장할 변수


    @BeforeEach
    public void createUserOrigin() {
        existingUser = User.builder().email("pmmh9395@gmail.com").build();
        userRepository.save(existingUser);
    }

    @Test
    @DisplayName("이메일은 중복되지 않은 경우.")
    public void checkEmail() throws Exception{
        //given
        String emailCheck = "code11@gmail.com";

        //when
        Boolean isEmailDuplicate = userService.checkDuplicateEmail(emailCheck);

        //then
        assertThat(isEmailDuplicate).isFalse(); // 중복되지 않는 경우 false 반환을 확인
    }

    @Test
    @DisplayName("이메일이 중복되는 경우")
    public void testCheckEmailForDuplicate() {
        // given
        String emailToCheck = "pmmh9395@gmail.com";

        // when
        Boolean isEmailDuplicate = userService.checkDuplicateEmail(emailToCheck);

        // then
        assertThat(isEmailDuplicate).isTrue(); // 중복되는 경우 true 반환을 확인
    }
}