package com.pro.blog.service;

import com.pro.blog.vo.Result;
import com.pro.blog.vo.TagVo;

import java.util.List;

public interface TagService {
    List<TagVo> findTagId(Long articleId);

    Result hot(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailDetailById(Long id);
}
