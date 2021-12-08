package com.pro.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pro.blog.dao.beans.Tag;
import com.pro.blog.dao.mapper.TagMapper;
import com.pro.blog.service.TagService;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sun.text.CollatorUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    /**
     * 通过文章的id查询返回标签VO
     * mybatisplus 无法进行多表查询所以要自己写sql语句
     * @param articleId
     * @return
     */
    @Override
    public List<TagVo> findTagId(Long articleId) {
        List<Tag> tags = tagMapper.findTagId(articleId);
        return copyList(tags);
    }

    @Override
    public Result hot(int limit) {
        /**
         * 1.标签所拥有的文章数量最多的，最热的标签
         * 2.查询，根据tag_id分组计数，重大到小排列
         */
        List<Long> tagId =tagMapper.findHot(limit);
        if (CollectionUtils.isEmpty(tagId)){
            return Result.success(Collections.emptyList());
        }
        //需求的是tag_id和tag_name Tag对象
        List<Tag> tagList=tagMapper.findTagByTagId(tagId);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tagList = tagMapper.selectList(queryWrapper);

        return Result.success(copyList(tagList));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tagList = tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tagList));
    }

    @Override
    public Result findDetailDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        return Result.success(copy(tag));
    }

    private List<TagVo> copyList(List<Tag> tags) {
        List<TagVo> tagVos = new ArrayList<>();

        for (Tag tag : tags) {
            tagVos.add(copy(tag));
        }
        return tagVos;
    }

    private TagVo copy(Tag tag) {
        TagVo tagVo = new TagVo();
        BeanUtils.copyProperties(tag,tagVo);
        return tagVo;
    }


}
