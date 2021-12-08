package com.pro.blog.vo.params;

import com.pro.blog.dao.beans.Tag;
import com.pro.blog.vo.CategoryVo;
import com.pro.blog.vo.params.ArticleBodyParam;
import com.pro.blog.vo.TagVo;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVo category;

    private String summary;

    private List<TagVo> tags;

    private String title;
}
