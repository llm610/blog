package com.pro.blog.admin.service;

import com.pro.blog.admin.model.params.PageParam;
import com.pro.blog.admin.pojo.Permission;
import com.pro.blog.admin.vo.Result;
import org.springframework.stereotype.Service;


public interface PermissionService {
    Result listPermission(PageParam pageParam);

    Result add(Permission permission);

    Result update(Permission permission);

    Result delete(Long id);
}
