package com.pro.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.service.LoginService;
import com.pro.blog.service.SysUserService;
import com.pro.blog.utils.JWTUtils;
import com.pro.blog.vo.ErrorCode;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String salt="mszlu!@#";
    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1.检查参数是否合法
         * 2.根据用户和密码查询用户是否存在
         * 3.如果不存在就登录失败
         * 4.如果存在就生成token，返回给前端
         * 5.token就存放在redis中，token：user 信息设置过期时间
         * （登录认证token字符串是否合法，去redis认证是否存在）
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();

        if (StringUtils.isBlank(loginParam.getAccount()) || StringUtils.isBlank(loginParam.getPassword())){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        password = DigestUtils.md5Hex(password+salt);

        SysUser sysUser = sysUserService.findUser(account,password);
        if (sysUser == null) {

            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getId());

        //将数据存储到redis,set(键：token,值：阿里巴巴的JSON工具类把对象转化为字符串，设置过期时间
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }

        Map<String, Object> map = JWTUtils.checkToken(token);
        if (map==null){
            return null;
        }

        String uerJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        //判断是否过期删除了
        if (StringUtils.isBlank(uerJson)) {
            return null;
        }

        //解析json
        SysUser sysUser = JSON.parseObject(uerJson, SysUser.class);

        return sysUser;
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }


    //注册
    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1.判断参数是否合法
         * 2.判断账户是否存在，存在则返回已经被注册
         * 3.如果账户不存在，注册用户
         * 4.生成token
         * 5.存入redis，并返回
         * 6.加上事务，一旦中间的任何过程出现问题，注册用户需要回滚
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        String nickname = loginParam.getNickname();
        if (StringUtils.isBlank(account)
            && StringUtils.isBlank(password)
            && StringUtils.isBlank(nickname)
        ) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }

        SysUser sysUser = sysUserService.findUserByAccount(account);

        if (sysUser!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),ErrorCode.ACCOUNT_EXIST.getMsg());
        }

        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+salt));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAccount("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1);
        sysUser.setDeleted(0);
        sysUser.setStatus("");
        sysUser.setSalt("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());

        redisTemplate.opsForValue().set("TOKEN_"+token,JSON.toJSONString(sysUser),1,TimeUnit.DAYS);
        return Result.success(token);
    }
}
