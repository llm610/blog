package com.pro.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.blog.dao.beans.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagId(Long articleId);

    List<Long> findHot(int limit);

    List<Tag> findTagByTagId(List<Long> tagId);
}
