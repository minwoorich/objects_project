package com.objects.marketbridge.domain.user.repository;

import com.objects.marketbridge.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    //JpaRepository에서 제공되는 기본메서드 사용
    Optional<User> findByEmail(String email);

}
