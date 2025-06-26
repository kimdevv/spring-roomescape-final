package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.business.CookieManager;
import roomescape.auth.business.JwtProvider;
import roomescape.auth.exception.ForbiddenException;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.presentation.AuthController;

public class AdminLoginInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;
    private final JwtProvider jwtProvider;

    public AdminLoginInterceptor(CookieManager cookieManager, JwtProvider jwtProvider) {
        this.cookieManager = cookieManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof ResourceHttpRequestHandler) && ((HandlerMethod) handler).getMethod().isAnnotationPresent(AdminLogin.class)) {
            try {
                String token = cookieManager.parse(request.getCookies(), AuthController.TOKEN_COOKIE_NAME);
                if (jwtProvider.isAdmin(token)) {
                    return true;
                }
                throw new ForbiddenException("접근 권한이 없습니다.");
            } catch (IllegalArgumentException exception) {
                throw new UnauthorizedException("로그인하지 않았거나 잘못된 요청입니다.");
            }
        }
        return true;
    }
}
