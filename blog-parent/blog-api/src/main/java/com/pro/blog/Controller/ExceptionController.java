package com.pro.blog.Controller;

import com.pro.blog.vo.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//controller增强标签：意思是对加了controller的方法进行拦截处理 AOP的实现
@ControllerAdvice
public class ExceptionController {
    //加载异常处理信息
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json数据
    public Result doException(Exception e){
        e.printStackTrace();
        return Result.fail(404,"系统异常");
    }
}
