package com.yuntian.security.config.security;

import com.yuntian.security.config.security.filter.JwtTokenAuthorizationFilter;
import com.yuntian.security.config.security.handler.AuthEntryPoint;
import com.yuntian.security.config.security.handler.AuthFailureHandler;
import com.yuntian.security.config.security.handler.AuthSuccessHandler;
import com.yuntian.security.config.security.service.UserDetailServiceImpl;
import com.yuntian.security.config.session.CookieProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import javax.annotation.Resource;

/**
 * EnableWebSecurity注解使得SpringMVC集成了Spring Security的web安全支持
 *
 * @author guangleilei
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private CookieProperties cookieProperties;

    /**
     * 权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置拦截规则
        http.csrf().disable()
                .cors().and().formLogin().loginPage("/login").loginProcessingUrl("/auth/login")
                .usernameParameter("userName").passwordParameter("passWord")
                .successHandler(authSuccessHandler()).failureHandler(authFailureHandler());
        http.authorizeRequests()
                .antMatchers("/favicon.ico", "/static/**", "/login", "/logout", "/auth/login")
                .permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .rememberMe()
                .rememberMeParameter("rememberMe")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds((int) cookieProperties.getRememberMeTimeout().getSeconds())
                .userDetailsService(userDetailService())
                .and()
                .exceptionHandling().authenticationEntryPoint(authEntryPoint());
        // 基于token，所以不需要session
        http .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 禁用缓存
        http.headers().cacheControl();
        http.addFilterBefore(jwtTokenAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout().logoutUrl("/logout").deleteCookies("remember-me");
    }

    /**
     * 自定义认证数据源
     */
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailService())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository  persistentTokenRepository() {
        return new RedisTokenRepositoryImpl(cookieProperties.getRememberMeTimeout().getSeconds());
    }

    @Bean
    public AuthEntryPoint authEntryPoint()  {
        return new AuthEntryPoint();
    }
    @Bean
    public JwtTokenAuthorizationFilter jwtTokenAuthorizationFilter() throws Exception {
        return new JwtTokenAuthorizationFilter(authenticationManagerBean());
    }

    @Bean
    public UserDetailsService userDetailService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }

    @Bean
    public AuthFailureHandler authFailureHandler() {
        return new AuthFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @ConditionalOnMissingBean(ClassPathTldsLoader.class)
    public ClassPathTldsLoader classPathTldsLoader() {
        return new ClassPathTldsLoader();
    }
}