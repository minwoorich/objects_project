package com.objects.marketbridge.domain.member.controller;


import com.objects.marketbridge.domain.member.dto.CreateMember;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<Void> registerUser(@Valid @RequestBody CreateMember userDTO) {
        memberService.save(userDTO);
        return ApiResponse.of(HttpStatus.CREATED,"completed",null);
    }
}
