package roomescape.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import roomescape.common.argumentresolver.LoginInfoResolver;
import roomescape.auth.business.CookieManager;
import roomescape.auth.business.JwtProvider;
import roomescape.common.interceptor.AdminLoginInterceptor;
import roomescape.common.interceptor.LogInterceptor;
import roomescape.common.interceptor.NormalLoginInterceptor;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CookieManager cookieManager;
    private final JwtProvider jwtProvider;

    public WebMvcConfig(CookieManager cookieManager, JwtProvider jwtProvider) {
        this.cookieManager = cookieManager;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new NormalLoginInterceptor(cookieManager));
        registry.addInterceptor(new AdminLoginInterceptor(cookieManager, jwtProvider));
        registry.addInterceptor(new LogInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginInfoResolver(cookieManager, jwtProvider));
    }
}
