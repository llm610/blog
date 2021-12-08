package com.pro.blog.Controller;

import com.pro.blog.service.CommentsService;
import com.pro.blog.vo.Result;
import com.pro.blog.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentsController {

    @Autowired
    private CommentsService commentsService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){

        return commentsService.commentsByArticleId(id);
    }

    @PostMapping("create/change")
    public Result comments(@RequestBody CommentParam commentParam){

        return commentsService.comment(commentParam);
    }

}
