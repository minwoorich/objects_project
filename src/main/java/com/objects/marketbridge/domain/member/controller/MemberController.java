package com.objects.marketbridge.domain.member.controller;


import com.objects.marketbridge.domain.member.dto.CreateMemberDto;
import com.objects.marketbridge.domain.member.dto.FindPointDto;
import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponse<Void> registerUser(@Valid @RequestBody CreateMemberDto memberDTO) {
        memberService.createMember(memberDTO);
        return ApiResponse.of(HttpStatus.CREATED,"completed",null);
    }

    @GetMapping("/membership/{id}")
    public void changeMembership(@PathVariable Long id){
        memberService.changeMemberShip(id);
    }

    @GetMapping("/point/{id}")
    public ApiResponse<FindPointDto> findPointById(@PathVariable Long id){

        FindPointDto memberPoint = memberService.findPointById(id);
        return ApiResponse.of(HttpStatus.OK,"completed",memberPoint);
    }
}
