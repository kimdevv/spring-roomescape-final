package roomescape.auth.business;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import static org.assertj.core.api.Assertions.assertThat;

class CookieManagerTest {

    private final CookieManager cookieManager = new CookieManager();

    @Test
    void 주어진_이름과_값을_담고_있는_쿠키를_생성할_수_있다() {
        // Given
        String name = "token";
        String value = "value";

        // When
        ResponseCookie responseCookie = cookieManager.generateCookie(name, value);

        // Then
        assertThat(responseCookie.toString()).contains(
                name + "=" + value,
                "Max-Age=3600",
                "Expires=",
                "HttpOnly");
    }
}