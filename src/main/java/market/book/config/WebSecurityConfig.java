package market.book.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import market.book.config.auth.handler.MemberCustomLoginFailHandler;
import market.book.config.auth.handler.MemberCustomLoginSuccessHandler;
import market.book.config.auth.handler.WebAccessDeniedHandler;
import market.book.config.auth.handler.WebAuthenticationEntryPoint;
import market.book.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final WebAccessDeniedHandler webAccessDeniedHandler;
    private final WebAuthenticationEntryPoint webAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    @Value("${security.allowed-uris}")
    private String[] allowedUris;


    /**
     * 정적 자원(Resource)에 대해서 인증된 사용자가 정적 자원의 접근에 대해 ‘인가’에 대한 설정을 담당하는 메서드이다.
     * resources(css, js 등)의 경우 securityContext 등에 대한 조회가 불필요 하므로 disable 한다.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain resources(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("resources/**")
                .headers((header) -> header.cacheControl(HeadersConfigurer.CacheControlConfig::disable)) // 정적 파일 cache 설정
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .requestCache(RequestCacheConfigurer::disable)
                .securityContext(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .build();
    }

    /**
     * 스프링 세큐리티 규칙
     */
    @Bean
    @Order(1)
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                // TODO: CSRF 설정
                .csrf(AbstractHttpConfigurer::disable) // csrf 보안 설정 비활성화
                .authorizeHttpRequests(request ->
                    request
                            .requestMatchers("/resources/**").permitAll()
                            .requestMatchers(allowedUris).permitAll()
                            .anyRequest().authenticated()
                )

                // 예외 처리 설정
                .exceptionHandling(handler ->
                    handler
                            .accessDeniedHandler(webAccessDeniedHandler) // 권한이 없는 사용자 접근 시
                            .authenticationEntryPoint(webAuthenticationEntryPoint) // 인증되지 않은 사용자 접근 시
                )

                // 폼 로그인 설정
                .formLogin(formLogin ->
                    formLogin
                            .loginPage("/members/login-view") // 로그인 페이지 URL 설정
                            .loginProcessingUrl("/members/login")
                            .usernameParameter("email")
                            .passwordParameter("password")
                            .successHandler(memberCustomLoginSuccessHandler()) // 로그인 성공 시 이동 할 URL 설정
                            .failureHandler(memberCustomLoginFailHandler()) // 로그인 실패 시 이동 할 URL 설정
                )

                // 로그아웃 설정
                .logout(logout ->
                    logout
                            .logoutUrl("/members/logout") // 로그아웃 URL 설정
                            .invalidateHttpSession(true) // 로그아웃 후 세션 초기화 설정
                            .deleteCookies("JSESSIONID") // 로그아웃 후 쿠키 삭제 설정
                            .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 URL 설정
                )
                .userDetailsService(userDetailsService)
                .build();
    }

    /**
     * 비밀번호 암호화 로직
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 로그인 성공 시 실행될 handler bean 등록
     */
    @Bean
    public MemberCustomLoginSuccessHandler memberCustomLoginSuccessHandler() {
        return new MemberCustomLoginSuccessHandler();
    }

    /**
     * 로그인 실패 시 실행될 handler bean 등록
     */
    @Bean
    public MemberCustomLoginFailHandler memberCustomLoginFailHandler() {
        return new MemberCustomLoginFailHandler();
    }
}
