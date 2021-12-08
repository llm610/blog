package com.pro.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pro.blog.dao.beans.Comment;
import com.pro.blog.dao.beans.SysUser;
import com.pro.blog.dao.mapper.CommentsMapper;
import com.pro.blog.service.CommentsService;
import com.pro.blog.service.SysUserService;
import com.pro.blog.utils.UserThreadLocal;
import com.pro.blog.vo.CommentVo;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.UserVo;
import com.pro.blog.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private SysUserService sysUserService;

    @Override
    public Result commentsByArticleId(Long id) {
        /**
         * 1.根据文章ID查询评论表 从comment中查询
         * 2.根据作者的id查询作者的信息
         * 3.判断 如果level=1 查询子评论存不存在
         * 4.如果有根据评论id查询
         */
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        queryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> comments = commentsMapper.selectList(queryWrapper);

        List<CommentVo> commentVos = copyList(comments);
        return Result.success(commentVos);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());

        Long parent = commentParam.getParent();

        if (parent==null || parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null?0:parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:parent);
        this.commentsMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();

        for (Comment comment : comments) {
            commentVos.add(copy(comment));
        }
        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo = new CommentVo();

        BeanUtils.copyProperties(comment,commentVo);
//        commentVo.setId(String.valueOf(comment.getId()));
        Long authorId = comment.getAuthorId();

        UserVo userVo = this.sysUserService.findUserVoById(authorId);

        commentVo.setAuthor(userVo);
        //子评论
        Integer level = comment.getLevel();

        if (1 == level){
            Long id = comment.getId();

            List<CommentVo> commentVos = findCommentByParenId(id);
            commentVo.setChildrens(commentVos);
        }

        if (level > 1){
            Long toUid = comment.getToUid();
            UserVo toUserVo = this.sysUserService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }

        return commentVo;
    }

    private List<CommentVo> findCommentByParenId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);

        return copyList(commentsMapper.selectList(queryWrapper));
    }
}
