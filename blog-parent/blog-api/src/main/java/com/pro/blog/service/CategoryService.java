package com.pro.blog.service;

import com.pro.blog.vo.CategoryVo;
import com.pro.blog.vo.Result;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoriesDetailById(Long id);
}
