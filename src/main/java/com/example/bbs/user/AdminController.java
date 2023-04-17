package com.example.bbs.user;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @GetMapping("/hello")
    public JSONObject hello(){
        JSONObject response = new JSONObject();
        System.out.println("hello");
        response.put("test","hello spring-security");
        return response;
    }
}
