package roomescape.auth.business;

import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    public static final long TOKEN_COOKIE_DURATION = 3_600L;

    public ResponseCookie generateCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .maxAge(TOKEN_COOKIE_DURATION)
                .build();
    }

    public String parse(Cookie[] cookies, String name) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        throw new IllegalStateException("로그인하지 않은 상태이거나 잘못된 요청입니다.");
    }
}
