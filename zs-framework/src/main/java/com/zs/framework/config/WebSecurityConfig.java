package com.zs.framework.config;

import com.zs.framework.security.filter.MyAuthenticationFilter;
import com.zs.framework.security.filter.JwtAuthenticationTokenFilter;
import com.zs.framework.security.handler.*;
import com.zs.framework.security.provider.UserNameAuthenticationProvider;
import com.zs.framework.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.annotation.Resource;
import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)    // 启用方法级别的权限认证
public class WebSecurityConfig {

    /**
     * 退出处理类
     */
    @Resource
    private CustomLogoutSuccessHandler logoutSuccessHandler;

    @Resource
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    // 用户名密码登录成功处理
    @Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    // 用户名密码登录失败处理
    @Resource
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Resource
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Resource
    private UserNameAuthenticationProvider userNameAuthenticationProvider;

//    /**
//     * 密码明文加密方式配置
//     * @return
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



    @Bean
    public MyAuthenticationFilter myAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        MyAuthenticationFilter myAuthenticationFilter = new MyAuthenticationFilter("/login");
        myAuthenticationFilter.setAuthenticationManager(authenticationManager);
        myAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        myAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);
        return myAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // 禁用csrf
                .csrf().disable()
                // 禁止session
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 自定义异常处理
                .exceptionHandling()
                // 未登录处理
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                // 权限处理
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .authorizeHttpRequests()
//                .mvcMatchers("/login").permitAll()
                // 白名单url，匿名访问
                .antMatchers("/login").permitAll()
                // 不在白名单的请求都需要身份认证
                .antMatchers(HttpMethod.GET, "/", "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/profile/**").permitAll()
                // 不在白名单的请求都需要身份认证
//                .anyRequest().authenticated()
                .and()
                .authenticationProvider(userNameAuthenticationProvider)
                // 把token校验过滤器添加到过滤器链中
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(myAuthenticationFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }



}
