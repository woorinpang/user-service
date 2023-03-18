package com.woorinpang.userservice.global.config.jwt;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
@Getter
@AllArgsConstructor
public enum JwtException {
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 형식"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "기간 만료"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰"),
    WRONG_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰"),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "빈 토큰"),
    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "토큰 에러");

    private final HttpStatus httpStatus;
    private final String description;

    public void setResponse(HttpServletResponse response){
        response.setContentType("application/json;charset=UTF-8");
        log.error("jwt token error - {}", this.name());

        JSONObject responseJson = new JSONObject();
        responseJson.put("status", this.getHttpStatus().value());
        responseJson.put("error", this.getHttpStatus().name());
        responseJson.put("code", this.name());
        responseJson.put("message", this.getDescription());
        try {
            response.setStatus(401);
            response.getWriter().print(responseJson);
        } catch (IOException e) {
            log.error("JWT setResponse error on {}", e.getMessage());
        }
    }
}
