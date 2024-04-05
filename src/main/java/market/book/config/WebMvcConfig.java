package market.book.config;

import lombok.RequiredArgsConstructor;
import market.book.common.interceptor.ProfileImageInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final ProfileImageInterceptor profileImageInterceptor;
    @Value("${mvc.exclude-uris}")
    private String[] excludeUris;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(profileImageInterceptor)
                .order(0)
                .excludePathPatterns(excludeUris);
    }
}
