package com.pro.blog.admin.service;

import com.pro.blog.admin.pojo.Admin;
import com.pro.blog.admin.pojo.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService{

    String requestURI;

    @Autowired
    private Adminservice adminservice;

    @Override
    public boolean auth(HttpServletRequest request, Authentication authentication) {
        //权限认证
        requestURI = request.getRequestURI();

        Object principal = authentication.getPrincipal();

        if (principal == null || "anonymousUser".equals(principal)){
            return false;
        }

        UserDetails userDetails = (UserDetails)principal;

        String username = userDetails.getUsername();

        Admin admin = adminservice.findAdminByUserName(username);

        if(admin == null){
            return false;
        }

        //通过ID给与超级权限，也可以给与多种权限
        if (1 == admin.getId()){
            //超级管理员
            return true;
        }

        Long id = admin.getId();

        List<Permission> permissionList =adminservice.findPermissionByAdminId(id);

        requestURI = StringUtils.split(requestURI,'?')[0];


//        if (requestURI.contains("delete")){
//            requestURI = requestURI.substring(0,24);
//
//        }

        if (StringUtils.contains(requestURI,"delete")){
            requestURI = StringUtils.substring(requestURI, 0, 24);
            System.out.println(requestURI);
        }

        for (Permission permission : permissionList) {
            if (requestURI.equals(permission.getPath())){
                return  true;
            }
        }
        return false;
    }
}
