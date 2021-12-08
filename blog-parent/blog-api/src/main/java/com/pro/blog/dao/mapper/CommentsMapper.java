package com.pro.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.blog.dao.beans.Comment;
import org.springframework.stereotype.Component;

@Component
public interface CommentsMapper extends BaseMapper<Comment> {
}
