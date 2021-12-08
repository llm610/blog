package com.pro.blog.service;

import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.LoginParam;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface LoginService {
    Result login(LoginParam loginParam);

    SysUser checkToken(String token);

    Result logout(String token);

    Result register(LoginParam loginParam);
}
