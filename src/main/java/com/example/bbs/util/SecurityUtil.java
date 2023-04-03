package com.example.bbs.util;

import com.example.bbs.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SecurityUtil {
    // 访问当前登录的用户对象
    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // 访问当前登录的用户对象的id
    public static Long getUserId() {
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        //返回User权限
        return ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities();
    }
}
