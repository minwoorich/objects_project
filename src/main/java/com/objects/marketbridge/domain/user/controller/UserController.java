package com.objects.marketbridge.domain.user.controller;


import com.objects.marketbridge.domain.user.dto.CreateUser;
import com.objects.marketbridge.domain.user.service.UserService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ApiResponse<Void> registerUser(@Valid @RequestBody CreateUser userDTO) {
        userService.saveUser(userDTO);
        return ApiResponse.of(HttpStatus.CREATED,"completed",null);
    }
}
