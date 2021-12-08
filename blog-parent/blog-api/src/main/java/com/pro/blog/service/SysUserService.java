package com.pro.blog.service;

import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.UserVo;

public interface SysUserService {

    SysUser findUserById(Long authorId);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);
    // 保存用户
    void save(SysUser sysUser);

    UserVo findUserVoById(Long id);
}
