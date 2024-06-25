package io.woorinpang.userservice.admin.support.page;

public record AdminPageInfo(
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast
) {
}
