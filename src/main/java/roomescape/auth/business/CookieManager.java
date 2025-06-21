package roomescape.auth.business;

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
}
