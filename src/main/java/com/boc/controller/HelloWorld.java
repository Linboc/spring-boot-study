package com.boc.controller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.boc.dao.DepartmentDao;
import com.boc.dao.EmployeeDao;
import com.boc.entity.Employee;

@Controller
public class HelloWorld {
    
    @Autowired
    private EmployeeDao employeeDao;
    
    @Autowired
    private DepartmentDao departmentDao;

    @RequestMapping("/success")
    public String success(Model model) {
        model.addAttribute("hello", "<h1>你好<h1>");
        model.addAttribute("users", Arrays.asList("zhangsan", "lisi", "wangwu"));
        Objects.requireNonNull(null, "made");
        return "success";
    }
    
    @GetMapping("/emps")
    public String list(HttpServletRequest req) {
        req.setAttribute("emps", employeeDao.getAll());
        return "list";
    }
    
    @GetMapping("/empPage")
    public String toEmpPage(HttpServletRequest req) {
        req.setAttribute("depas", departmentDao.getDepartments());
        return "add";
    }
    
    @PostMapping("/emp")
    public String addEmp(Employee e) {
        employeeDao.save(e);
        //不能直接返回视图,因为请求域里面没有数据,要重定向到员工列表,请求转发会导致添加
        //页面的表单重复提交,并且因为请求是Post,如果直接转发到员工列表,就会失败,没有一个可以Post的emps
        return "redirect:/emps";
    }
    
    @GetMapping("/empUpdatePage")
    public String toEmpUpdatePage(Integer id, HttpServletRequest req) {
        req.setAttribute("emp", employeeDao.get(id));
        req.setAttribute("depas", departmentDao.getDepartments());
        return "add";
    }
    
    @PutMapping("emp")
    public String updateEmp(Employee e) {
        employeeDao.save(e);
        return "redirect:/emps";
    }
    
    @DeleteMapping("/emp/{id}")
    public String delete(@PathVariable("id") Integer id) {
        employeeDao.delete(id);
        return "redirect:/emps";
    }
    
}
