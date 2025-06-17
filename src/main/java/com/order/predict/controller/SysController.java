package com.order.predict.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: TODO
 * @version: v1.0.0
 * @author: lidelin
 * @date: 2025/6/10 10:10
 */
@Controller
public class SysController {
    @PostMapping("/login")
    public String login(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpSession session) {

        // 简化演示，实际应调用数据库校验用户名密码
        if ("admin".equals(username) && "123".equals(password)) {
            session.setAttribute("user", "admin");
            return "redirect:/order.html";
        } else if ("user".equals(username) && "123".equals(password)) {
            session.setAttribute("user", "user");
            return "redirect:/order.html";
        } else {
        }

        return "redirect:/error.html";
    }

    @GetMapping("/me")
    @ResponseBody
    public Map<String, Object> getCurrentUser(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        String user = (String) session.getAttribute("user");
        if (user != null) {
            result.put("status", "ok");
            result.put("role", user);
        } else {
            result.put("status", "fail");
        }
        return result;
    }



}
