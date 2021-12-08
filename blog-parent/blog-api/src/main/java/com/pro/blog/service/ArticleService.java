package com.pro.blog.service;

import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.ArticleParam;
import com.pro.blog.vo.params.PageParams;

public interface ArticleService {

    Result listArticle(PageParams pageParams);

    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    /**
     * 发布文章接口
     * @param articleParam
     * @return
     */
    Result publish(ArticleParam articleParam);
}
