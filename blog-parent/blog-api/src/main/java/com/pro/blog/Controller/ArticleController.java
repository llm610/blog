package com.pro.blog.Controller;

import com.pro.blog.common.aop.Cache;
import com.pro.blog.common.aop.LogAnnotation;
import com.pro.blog.service.ArticleService;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.ArticleParam;
import com.pro.blog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//JSON数据交互
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /*首页文章列表*/

    @PostMapping
    //加上此注解表示要对此接口进行记录日志  ，AOP
    @LogAnnotation(module = "文章",operator="获取文章列表")
    public Result listArticle(@RequestBody PageParams pageParams){

        return articleService.listArticle(pageParams);
    }

    /**
     * 最热文章标签
     * @param
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5*1000,name ="hot_article")
    public Result hotArticle(){
        int limit = 3;
        return articleService.hotArticle(limit);
    }

    /**
     *最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5*60*1000,name ="new_article")
    public Result newArticle(){
        int limit = 3;
        return articleService.newArticle(limit);
    }

    /**
     *文章归档
     * @return
     */
    @PostMapping("listArchives")
    @Cache(expire = 5*60*1000,name ="listArchives")
    public Result listArchives(){
        //Result 输出数据
        //Result(success=true, code=200, msg=success, data=[Archives(year=2021, month=6, count=1)
        return articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    /**
     * 发布文章
     * @param articleParam
     * @return
     */
    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){

        return articleService.publish(articleParam);
    }
}
