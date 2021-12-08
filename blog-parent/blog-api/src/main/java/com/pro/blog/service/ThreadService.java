package com.pro.blog.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pro.blog.dao.beans.Article;
import com.pro.blog.dao.mapper.ArticleMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class ThreadService {

    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

        Integer viewCount = article.getViewCounts();

        Article articleUpdate = new Article();
        articleUpdate.setViewCounts(viewCount+1);

        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,article.getId());
        //设置一个乐观锁，修改之前再次确认这个值没有被其他线程抢先修改  保护线程安全
        updateWrapper.eq(Article::getViewCounts,viewCount);

        articleMapper.update(articleUpdate,updateWrapper);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
