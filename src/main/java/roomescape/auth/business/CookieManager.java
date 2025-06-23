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
        if (cookies == null) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("잘못된 요청입니다.");
    }
}
