package roomescape.auth.business;

import jakarta.servlet.http.Cookie;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void 쿠키에서_원하는_값을_파싱할_수_있다() {
        // Given
        String name1 = "cookieName1";
        String value1 = "cookieValue1";
        String name2 = "cookieName2";
        String value2 = "cookieValue2";
        Cookie[] cookies = {
                new Cookie(name1, value1), new Cookie(name2, value2)
        };

        // When & Then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(cookieManager.parse(cookies, name1)).isEqualTo(value1);
            softAssertions.assertThat(cookieManager.parse(cookies, name2)).isEqualTo(value2);
        });
    }

    @Test
    void 잘못된_쿠키의_이름으로는_값을_파싱할_수_없다() {
        // Given
        String name = "cookieName";
        String value = "cookieValue";
        Cookie[] cookies = {
                new Cookie(name, value)
        };

        // When & Then
        assertThatThrownBy(() -> cookieManager.parse(cookies, "invalidName"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("잘못된 요청입니다.");
    }
}