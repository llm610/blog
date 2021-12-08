package com.pro.blog.Controller;

import com.pro.blog.service.TagService;
import com.pro.blog.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("hot")
    public Result hot(){
        int limit =6;
        return tagService.hot(limit);
    }

    @GetMapping
    public Result findAll(){

        return tagService.findAll();
    }

    @GetMapping("detail")
    public Result findAllDetailDetail(){

        return tagService.findAllDetail();
    }

    @GetMapping("detail/{id}")
    public Result findDetailDetailById(@PathVariable("id") Long id){

        return tagService.findDetailDetailById(id);
    }
}
