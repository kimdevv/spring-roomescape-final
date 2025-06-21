package roomescape.auth.presentation;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.business.AuthService;
import roomescape.auth.business.CookieManager;
import roomescape.auth.presentation.dto.request.LoginRequest;

@Controller
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService, CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest requestBody) {
        String token = authService.login(requestBody);
        ResponseCookie tokenCookie = cookieManager.generateCookie("token", token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .build();
    }
}
