package com.boc.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    /**
     * 如果前面加/代表项目根路径,如果不加/代表当前路径的下级路径,请求转发是服务器内部的,对浏览器不可见,因此路径不会改变,重定向
     * 是浏览器重新请求的,因此如果加了/,浏览器的路径会改为根路径
     */
    @PostMapping("/user/login")
    @PreAuthorize("hasAnyRole('ROLE_Admin')")
    public String login(String username
            , String password
            , Map<String, Object> map
            , HttpSession session) {
        
        if (!StringUtils.isEmpty(username) && "123456".equals(password)) {
            session.setAttribute("loginUser", username);
            return "redirect:/main";
        }
        map.put("msg", "用户名或密码错误!");
        return "/index";        
    }
    
}
