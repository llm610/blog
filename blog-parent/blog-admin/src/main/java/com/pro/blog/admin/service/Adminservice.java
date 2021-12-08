package com.pro.blog.admin.service;


import com.pro.blog.admin.pojo.Admin;
import com.pro.blog.admin.pojo.Permission;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface Adminservice {

    public Admin findAdminByUserName(String username);

    List<Permission> findPermissionByAdminId(Long id);
}
