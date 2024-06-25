package io.woorinpang.userservice.admin.support.page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AdminPageRequest {
    private int page = 0;

    private int size = 20;
}
