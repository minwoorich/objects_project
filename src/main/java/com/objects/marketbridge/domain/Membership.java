package com.objects.marketbridge.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Membership {
    BASIC("일반"),
    WOW("와우");

    private final String text;
}
