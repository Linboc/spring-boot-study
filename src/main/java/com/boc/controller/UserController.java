package com.boc.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.boc.entite.User;
import com.boc.mapper.UserMapper;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    
    @GetMapping("/list")
    public Collection<User> list() {
        return userMapper.list();
    }
    
    @GetMapping("/{id}")
    public User list(@PathVariable("id") Integer id) {
        return userMapper.getById(id);
    }
    
}
