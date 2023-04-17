package com.example.bbs.user.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
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

    @Column
    private String name;

    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer score = 0;

    private String contact;

    private String nature;

    private String workPlace;

//    @Enumerated(EnumType.STRING)
//    private Role role;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(role.name()));
//    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<UserRole> userRoleSet = new HashSet<>();

    @JsonGetter
    public Boolean isAdmin() {
        return userRoleSet != null;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoleSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

//    public Set<UserRole> getUserRoleSet() {
//        return userRoleSet;
//    }
//
//    public void setUserRoleSet(Set<UserRole> userRoleSet) {
//        this.userRoleSet = userRoleSet;
//    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                username.equals(user.username) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, userRoleSet);
    }

}