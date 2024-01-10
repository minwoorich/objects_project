package com.objects.marketbridge.domain.user.controller;


import com.objects.marketbridge.domain.user.dto.CreateUser;
import com.objects.marketbridge.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> registerUser(@RequestBody CreateUser userDTO) {
        userService.saveUser(userDTO);
        return ResponseEntity.ok("User registered successfully");
    }
}
