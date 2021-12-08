package com.pro.blog.admin.service;

import com.pro.blog.admin.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

//Security提供UserDetailsService 方法
@Component
public class SecurityUserServiceImpl implements UserDetailsService {

    @Autowired
    private Adminservice adminservice;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //登录时会把Username传递到该方法
        //通过username查询 admin表，如果admin存在，将密码告诉spring security
        //如果不存在，返回null  认真失败
        Admin admin = adminservice.findAdminByUserName(username);

        if (admin==null){
            //登录失败
            return null;
        }

        User user = new User(username,admin.getPassword(),new ArrayList<>());
        return user;
    }
}
