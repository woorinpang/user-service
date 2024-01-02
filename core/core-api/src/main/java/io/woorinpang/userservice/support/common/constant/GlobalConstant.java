package io.woorinpang.userservice.support.common.constant;

public interface GlobalConstant {
    final String HEADER_SITE_ID = "X-Site-Id"; //header 에 어떤 사이트에서 보내는 요청인지 구분하기 위한 정보
    final String AUTHORIZATION_URI = "/auth/check";
    final String REFRESH_TOKEN_URI = "/auth/token/refresh";
    final String API_DOCS_URI = "/docs/**";
    final String USER_JOIN_URI = "/users/join";
    final String LOGIN_URI = "/login";
    final String[] SECURITY_PERMITALL_ANTPATTERNS = {
            AUTHORIZATION_URI,
            REFRESH_TOKEN_URI,
            USER_JOIN_URI,
            LOGIN_URI,
            "/actuator/**",
            API_DOCS_URI
    };
    final String USER_SERVICE_URI = "/user-service";
}
