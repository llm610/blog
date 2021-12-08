package com.pro.blog.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.blog.admin.pojo.Admin;
import com.pro.blog.admin.pojo.Permission;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminMapper extends BaseMapper<Admin> {

    @Select("SELECT * from ms_permission WHERE id in(SELECT permission_id from ms_admin_permission WHERE admin_id=#{id});")
    List<Permission> findPermissionByAdminId(Long id);
}
