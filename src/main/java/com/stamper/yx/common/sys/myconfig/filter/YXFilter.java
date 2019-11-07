package com.stamper.yx.common.sys.myconfig.filter;

import com.stamper.yx.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * yx过滤器拦截所有页面跳转,
 * 校验用户token
 */
@Slf4j
@Component
public class YXFilter extends OncePerRequestFilter {
    private List<String> ignorePath;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        log.info("进入视图过滤器");
        String requestURI = httpServletRequest.getRequestURI();
//        System.out.println("请求路径：" + requestURI);
        ignorePath = new ArrayList<>();
        ignorePath.add("/index.html");
        ignorePath.add("/user/getTicket");
        boolean contains = ignorePath.contains(requestURI);
        if (contains == false) {
            //TODO 校验admin session 处理界面跳转
            HttpSession session = httpServletRequest.getSession();
            Object loginUser = session.getAttribute("loginUser");
            if (loginUser == null || !loginUser.equals("admin")) {
                httpServletRequest.setAttribute("msg","没有权限请先登陆");
                httpServletRequest.getRequestDispatcher("/index.html").forward(httpServletRequest,httpServletResponse);
                return;
            }
//            log.info("session内容{{}}", loginUser);
//
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
