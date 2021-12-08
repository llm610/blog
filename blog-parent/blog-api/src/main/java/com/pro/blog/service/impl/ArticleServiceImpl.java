package com.pro.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pro.blog.dao.beans.Article;
import com.pro.blog.dao.beans.ArticleBody;
import com.pro.blog.dao.beans.ArticleTag;
import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.dao.dos.Archives;
import com.pro.blog.dao.mapper.ArticleBodyMapper;
import com.pro.blog.dao.mapper.ArticleMapper;
import com.pro.blog.dao.mapper.ArticleTagMapper;
import com.pro.blog.service.*;
import com.pro.blog.utils.UserThreadLocal;
import com.pro.blog.vo.ArticleBodyVo;
import com.pro.blog.vo.ArticleVo;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.TagVo;
import com.pro.blog.vo.params.ArticleParam;
import com.pro.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;


//    @Override
//    public Result listArticle(PageParams pageParams) {
//        /*
//        * 1。分页查询article数据*/
//        //mybatisPlus的分页插件,设置的是文章这一页的分页插件
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//
//        //查询条件,一般都是按照最新的发布的时间排序，若是有置顶属性，那么可以通过文章的指定属性置顶
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        if (pageParams.getCategoryId()!=null){
//
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId()!=null){
//            //article_tag article_id 1: n tag_id 一对多的关系，所以需要通过tagid查出文章id，在进行in查询
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            //select * from article_Tags where ArticleTagID = pageParams.getTagId();
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size()>0){
//                //and in (articleid)
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //通过调用该API的方法调用排序方法，传入排序的参数,该方法是可以查询条件参数的
//        //相当于order by creat_date desc;
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//
//        //mybatisPlus的继承接口有该插件查询方法（该方法的参数是，Page,查询条件对象）
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//
//        //按照这些条件查询，排序后返回一个文章的List集合
//        List<Article> records = articlePage.getRecords();
//
//        //但是这个List不能直接返回，因为这个只是对用的是数据库对象的数据，而不是前端的，需要一个文章vo对象对应
//        //前端有对应的ORM，后端有对应的ORM数据交互，这样达到松耦合，所以需要另一个vo对象
//
//        List<ArticleVo> articles = copyList(records,true,true);
//
//        return Result.success(articles);
//    }

    @Override
    public Result listArticle(PageParams pageParams) {
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(
                page,
                pageParams.getCategoryId(),
                pageParams.getTagId(),
                pageParams.getYear(),
                pageParams.getMonth());
        List<Article> records = articleIPage.getRecords();


        return Result.success(copyList(records,true,true));
    }

    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        /*queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getTitle);
        queryWrapper.last("limit "+limit);*/
        List<Article> articles = articleMapper.selectList(
                queryWrapper.orderByDesc(Article::getViewCounts)
                        .select(Article::getId,Article::getTitle)
                        .last("limit "+limit));
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        //输出格式
        //[Archives(year=2021, month=6, count=1), Archives(year=2021, month=10, count=1)]
        List<Archives> archives = articleMapper.listArchives();
        return Result.success(archives);
    }

    @Autowired
    private ThreadService threadService;
    //查看文章
    @Override
    public Result findArticleById(Long articleId) {
        /**
         * 1.根据文章看文章信息
         * 2.根据bodyID和categoryid 去查询对象
         */
        Article article = articleMapper.selectById(articleId);
        ArticleVo articleVo= copy(article, true, true,true,true);
        //查看文章了，新增阅读数，
        //查看完文章后本该返回数据了，这个时候做了一个跟新操作，更新时枷锁，阻塞了其他操作，性能就会变低
        //更新 增加了此次接口的耗时 如果一旦更新出问题，不能影响查看文章的操作
        //使用线程池可以把更新操作扔到线程池中执行，和主线程就不相关了

        threadService.updateArticleViewCount(articleMapper,article);

        return Result.success(articleVo);
    }

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result publish(ArticleParam articleParam) {

        SysUser sysUser = UserThreadLocal.get();
        /**
         * 1. 发布文章 目的 构建Article对象
         * 2. 作者id  当前的登录用户
         * 3. 标签  要将标签加入到 关联列表当中
         * 4. body 内容存储 article bodyId
         */
        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setWeight(Article.Article_COMMON);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCreateDate(System.currentTimeMillis());

        article.setCategoryId(articleParam.getCategory().getId());
        //插入之后 会生成一个文章id
        this.articleMapper.insert(article);
        //tag
        List<TagVo> tags = articleParam.getTags();

        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
        //body
        ArticleBody articleBody  = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());
        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        List<ArticleVo> articleVos = new ArrayList<>();

        for (Article record : records) {
            articleVos.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVos;
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVos = new ArrayList<>();

        for (Article record : records) {
            articleVos.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVos;
    }

    @Autowired
    private CategoryService categoryService;

    private ArticleVo copy(Article article,boolean isTag,boolean isAuthor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        //把相同的属性拷贝到vo对象中，但是时间对象是不行的需要重新设置
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));

        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();

            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }

        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return  articleBodyVo;
    }
}
