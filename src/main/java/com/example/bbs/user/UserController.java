package com.example.bbs.user;


import com.alibaba.fastjson2.JSONObject;
import com.example.bbs.user.model.User;
import com.example.bbs.util.SecurityUtil;
import com.example.bbs.util.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final TokenUtil tokenUtil;

    @ResponseBody
    @PostMapping("/registry")
    public JSONObject registry(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String username = Optional.of(request.getString("username"))
                .orElseThrow(() -> new MissingServletRequestParameterException("username","String"));
        String password = Optional.of(request.getString("password"))
                .orElseThrow(() -> new MissingServletRequestParameterException("password","String"));

        User user = new User();

        user.setUsername(username);
        user.setPassword(password);

        try {
            response.put("data", userService.add(user));
            response.put("status", 1);
            response.put("message", "Registry Success.");
        } catch (DataIntegrityViolationException e) {
            if (userService.checkByUsername(username)) {
                response.put("status", 0);
                response.put("message", "Registry failed: username exits");
            } else {
                throw e;
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/login")
    public JSONObject token(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject result = new JSONObject();

        String username = Optional.of(request.getString("username"))
                .orElseThrow(() -> new MissingServletRequestParameterException("username", "String"));

        String password = Optional.of(request.getString("password"))
                .orElseThrow(() -> new MissingServletRequestParameterException("password", "String"));

        try {
            User user = (User) userService.loadUserByUsername(username);
            //((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
            System.out.println(user);
            if (userService.checkPassword(user, password)) {
                //result.put("data", user);
                result.put("status", 1);
                result.put("message", "Login success.");
                result.put("token", tokenUtil.generateToken(user));
                //result.put("auth",((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities());
            } else {
                result.put("status", 0);
                result.put("message", "Wrong password.");
            }
        } catch (UsernameNotFoundException e) {
            result.put("status", 0);
            result.put("message", "The user does not exist.");
        }
        return result;
    }

    @ResponseBody
    @PostMapping("/password")
    @Transactional
    public JSONObject editPassword(@RequestBody JSONObject request) throws MissingServletRequestParameterException {
        JSONObject response = new JSONObject();

        String oldPassword = Optional.of(request.getString("oldPassword"))
                .orElseThrow(() -> new MissingServletRequestParameterException("oldPassword", "String"));
        String newPassword = Optional.of(request.getString("newPassword"))
                .orElseThrow(() -> new MissingServletRequestParameterException("newPassword", "String"));

        User user = userService.loadById(SecurityUtil.getUserId());

        if (userService.checkPassword(user, oldPassword)) {
            user.setPassword(newPassword);
            userService.updatePassword(user);
            response.put("status", 1);
            response.put("message", "Edit Password Success");
        } else {
            response.put("status", 0);
            response.put("message", "Old Password Wrong");
        }

        return response;
    }

    @ResponseBody
    @GetMapping("/profile")
    public JSONObject getProfile() {
        JSONObject response = new JSONObject();

        response.put("status", 1);
        response.put("message", "Get profile success.");
        response.put("data", SecurityUtil.getUser());

        return response;
    }

    @ResponseBody
    @GetMapping("/profile/{id}")
    public JSONObject getProfileById(@PathVariable("id") Long id) {
        JSONObject response = new JSONObject();

        User user = userService.loadById(id);

        response.put("status", 1);
        response.put("message", "Get profile success.");
        response.put("data", user);

        return response;
    }

    @ResponseBody
    @PostMapping("/profile")
    @Transactional
    public JSONObject editProfile(@RequestBody JSONObject request) {
        JSONObject response = new JSONObject();

        User user = userService.loadById(SecurityUtil.getUserId());

        Optional<String> name = Optional.of(request.getString("name"));

        //user.setName()
        name.ifPresent(user::setName);

        try {
            response.put("data", userService.update(user));
            response.put("status", 1);
            response.put("message", "Update profile success.");
        } catch (Exception e) {
            response.put("status", 0);
            response.put("message", "Update profile failed.");
        }

        return response;
    }



    @GetMapping("/hello")
    public JSONObject hello(){
        JSONObject response = new JSONObject();
        System.out.println("hello");
        response.put("test","hello spring-security");
        return response;
    }
}
