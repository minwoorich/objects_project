package com.objects.marketbridge.global.security.constants;

/**
 * 토큰 정보 값을 설정하는 클래스
 */
public class SecurityConst {
    //6시간
    public static final int ACCESS_TOKEN_EXPIRE_TIME = 6;

    //7일
    public static final int REFRESH_TOKEN_EXPIRE_TIME = 24 * 7;

    public static final String AUTH = "auth";

    public static final String SECRET_KEY = "${spring.jwt.secret}";

    public static final String BEARER = "bearer";

    public static final String AUTHORIZATION = "Authorization";

    public static final String ID = "id";

    public static final String AUTH_USER = "hasAuthority('USER')";
}
