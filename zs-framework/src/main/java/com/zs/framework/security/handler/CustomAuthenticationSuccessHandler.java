package com.zs.framework.security.handler;

import com.alibaba.fastjson2.JSON;
import com.zs.common.core.HttpEnum;
import com.zs.common.core.Result;
import com.zs.common.model.LoginUserInfo;
import com.zs.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义登录成功处理
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUserInfo loginUserInfo = (LoginUserInfo) authentication.getPrincipal();
        response.setContentType("application/json;charset=UTF-8");
        String s = JSON.toJSONString(new Result().ok(200, "登录成功", jwtUtil.createToken(loginUserInfo)));
        response.getWriter().println(s);

    }
}
