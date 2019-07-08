package com.boc.config;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 拦截器
 * @author linbc
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    /**
     * 目标执行之前
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        try {

            if (Objects.isNull(request.getSession(false).getAttribute("loginUser"))) {
                request.setAttribute("msg", "没有权限,请先登录!");
                //页面显示路径还是他请求的路径,可是服务器返回的是/的视图,并且带上请求转发的参数
                request.getRequestDispatcher("/").forward(request, response);;
            }

            return Objects.nonNull(request.getSession(false).getAttribute("loginUser"));
        } catch(Exception e) {
            request.setAttribute("msg", "请求失败,请先登录!");
            request.getRequestDispatcher("/").forward(request, response);;
            return false;
        }
    }

    /**
     * 目标执行之后
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    /**
     * 页面响应之后
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }

}
