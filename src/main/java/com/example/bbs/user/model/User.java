package com.example.bbs.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(nullable = false,unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    //账号是否过期
    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号是否被锁定
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    //凭证密码是否过期
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //是否可用
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}