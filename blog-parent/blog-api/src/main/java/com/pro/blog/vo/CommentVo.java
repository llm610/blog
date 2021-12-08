package com.pro.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pro.blog.dao.beans.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo  {
    //防止前端 精度损失 把id转为string
    //分布式数据库是int类型，数据比较长，会造成精度损失问题，通过json序列化把Long转化为string类型进行传输就不会有问题了
   @JsonSerialize(using = ToStringSerializer.class)
    private Long  id;

    private UserVo author;

    private String content;

    private List<CommentVo> childrens;

    private String createDate;

    private Integer level;

    private UserVo toUser;
}
