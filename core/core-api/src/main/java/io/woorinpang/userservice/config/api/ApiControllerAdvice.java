package io.woorinpang.userservice.config.api;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class ApiControllerAdvice {

    /**
     * 모든 컨트롤러로 들어오는 요청을 초기화 한다.
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.initBeanPropertyAccess(); //setter 구현 없이 dto 클래스 필드에 접근
    }
}
