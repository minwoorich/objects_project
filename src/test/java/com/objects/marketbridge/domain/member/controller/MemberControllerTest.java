package com.objects.marketbridge.domain.member.controller;

import com.objects.marketbridge.domain.member.service.MemberService;
import com.objects.marketbridge.domain.order.RestDocsSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MemberControllerTest extends RestDocsSupport {
    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

    @Test
    void checkDuplicateEmail() {
        //given

        //when

        //then
    }

    @Test
    void signUp() {
        //given

        //when

        //then
    }

    @Test
    void signIn() {
        //given

        //when

        //then
    }

    @Test
    void signOut() {
        //given

        //when

        //then
    }

    @Test
    void reIssueToken() {
        //given

        //when

        //then
    }

    @Test
    void authTest() {
        //given

        //when

        //then
    }
}