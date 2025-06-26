package roomescape.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.business.CookieManager;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.auth.presentation.AuthController;

public class NormalLoginInterceptor implements HandlerInterceptor {

    private final CookieManager cookieManager;

    public NormalLoginInterceptor(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof ResourceHttpRequestHandler) && ((HandlerMethod) handler).getMethod().isAnnotationPresent(NormalLogin.class)) {
            try {
                cookieManager.parse(request.getCookies(), AuthController.TOKEN_COOKIE_NAME);
                return true;
            } catch (IllegalArgumentException exception) {
                throw new UnauthorizedException("로그인하지 않았거나 잘못된 요청입니다.");
            }
        }
        return true;
    }
}
