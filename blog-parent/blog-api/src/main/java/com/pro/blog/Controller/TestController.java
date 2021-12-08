package com.pro.blog.Controller;

import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.utils.UserThreadLocal;
import com.pro.blog.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();

        return Result.success(null);
    }
}
