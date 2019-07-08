package com.boc.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(Exception.class)
    public String exception(Exception e, HttpServletRequest req) {
        req.setAttribute("javax.servlet.error.exception", e);
        req.setAttribute("javax.servlet.error.message", "fuck");
        req.setAttribute("javax.servlet.error.status_code", 404);
        return "forward:/error";
    }
    
}
