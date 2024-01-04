package io.woorinpang.userservice.support.common.constant;

public interface GlobalConstant {
    final String HEADER_SITE_ID = "X-Site-Id"; //header 에 어떤 사이트에서 보내는 요청인지 구분하기 위한 정보
    final String AUTHORIZATION_URI = "/auth/check";
    final String REFRESH_TOKEN_URI = "/auth/token/refresh";
    final String USER_JOIN_URI = "/auth/join";
    final String LOGIN_URI = "/login";
    final String API_DOCS_URI = "/docs/**";
    final String[] SECURITY_PERMITALL_ANTPATTERNS = {
            AUTHORIZATION_URI,
            REFRESH_TOKEN_URI,
            USER_JOIN_URI,
            LOGIN_URI,
            API_DOCS_URI,
            "/actuator/**",
            "/auth/username/exists"
    };
    final String USER_SERVICE_URI = "/user-service";
}
