package com.pro.blog.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pro.blog.dao.beans.Article;
import com.pro.blog.dao.dos.Archives;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.PageParams;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    Result listArticle(PageParams pageParams);

    List<Archives> listArchives();

    //IPaged mybatisplus自动配置拦截器
    IPage<Article> listArticle(Page<Article> page,
                                Long categoryId,
                                Long tagId,
                                String year,
                                String month);
}
