package com.objects.marketbridge.domain.user.service;

import com.objects.marketbridge.domain.model.User;
import com.objects.marketbridge.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDuplicate(String email){

        User useremail = userRepository.findByEmail(email).orElse(null);

        if (useremail != null) {
            // null이 아니면 이미 가입된 email
            // 이미 등록된 아이디라는 문구 출력
            //....

        } else {
            // 사용할 수 있는 email
            // 정상적인 가입이 가능하다
            // ....
        }

    }
}
