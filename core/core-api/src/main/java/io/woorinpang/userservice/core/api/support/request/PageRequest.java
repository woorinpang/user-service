package io.woorinpang.userservice.core.api.support.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PageRequest {
    private int page = 0;

    private int size = 20;
}
