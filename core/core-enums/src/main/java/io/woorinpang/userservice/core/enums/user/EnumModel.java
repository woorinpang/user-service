package io.woorinpang.userservice.core.enums.user;

public interface EnumModel<T> {
    T findByCode(String code);
}
