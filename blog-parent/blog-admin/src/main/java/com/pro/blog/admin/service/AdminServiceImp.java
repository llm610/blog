package com.pro.blog.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pro.blog.admin.mapper.AdminMapper;
import com.pro.blog.admin.pojo.Admin;
import com.pro.blog.admin.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImp implements Adminservice {

    @Autowired
    private AdminMapper adminMapper;


    @Override
    public Admin findAdminByUserName(String username) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Admin::getUsername,username);

        queryWrapper.last("limit 1");

        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public List<Permission> findPermissionByAdminId(Long id) {

        return adminMapper.findPermissionByAdminId(id);


    }
}
