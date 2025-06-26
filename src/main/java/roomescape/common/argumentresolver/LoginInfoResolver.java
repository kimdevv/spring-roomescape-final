package roomescape.common.argumentresolver;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.business.CookieManager;
import roomescape.auth.business.JwtProvider;
import roomescape.auth.model.LoginInfo;
import roomescape.auth.presentation.AuthController;

public class LoginInfoResolver implements HandlerMethodArgumentResolver {

    private final CookieManager cookieManager;
    private final JwtProvider jwtProvider;

    public LoginInfoResolver(CookieManager cookieManager, JwtProvider jwtProvider) {
        this.cookieManager = cookieManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasMethodAnnotation(NormalLogin.class) || parameter.hasMethodAnnotation(AdminLogin.class))
                && parameter.getParameterType().equals(LoginInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Cookie[] cookies = ((HttpServletRequest) webRequest.getNativeRequest()).getCookies();
        String token = cookieManager.parse(cookies, AuthController.TOKEN_COOKIE_NAME);
        String email = (String) jwtProvider.parseClaim(token, "sub");
        return new LoginInfo(email);
    }
}
