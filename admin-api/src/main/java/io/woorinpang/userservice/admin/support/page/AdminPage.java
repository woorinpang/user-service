package io.woorinpang.userservice.admin.support.page;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminPage<T> {
    private List<T> content;
    private AdminPageInfo page;

    public AdminPage(List<T> content, AdminPageInfo page) {
        this.content = content;
        this.page = page;
    }

    public <U> AdminPage<U> map(Function<? super T, ? extends U> converter) {
        Assert.notNull(converter, "Function must not be null");

        List<U> convertedContent = this.content
                .stream()
                .map(converter)
                .collect(Collectors.toList());
        return new AdminPage<>(convertedContent, this.page);
    }
}
