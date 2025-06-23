package roomescape.auth.presentation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.business.AuthService;
import roomescape.auth.business.CookieManager;
import roomescape.auth.model.LoginInfo;
import roomescape.auth.presentation.dto.request.LoginRequest;
import roomescape.auth.presentation.dto.response.LoginCheckResponse;

@Controller
public class AuthController {

    public static final String TOKEN_COOKIE_NAME = "token";
    private final AuthService authService;
    private final CookieManager cookieManager;

    public AuthController(AuthService authService, CookieManager cookieManager) {
        this.authService = authService;
        this.cookieManager = cookieManager;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest requestBody) {
        String token = authService.login(requestBody);
        ResponseCookie tokenCookie = cookieManager.generateCookie(TOKEN_COOKIE_NAME, token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, tokenCookie.toString())
                .build();
    }

    @NormalLogin
    @GetMapping("/login/check")
    public ResponseEntity<LoginCheckResponse> checkLogin(LoginInfo loginInfo, HttpServletRequest request) {
        String name = authService.findMemberNameByEmail(loginInfo.email());
        return ResponseEntity.status(HttpStatus.OK).body(new LoginCheckResponse(name));
    }
}
