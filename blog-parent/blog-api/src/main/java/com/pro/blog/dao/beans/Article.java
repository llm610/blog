package com.pro.blog.dao.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    public static final int  Article_TOP=1;

    public static final int  Article_COMMON=0;

    private Long id;
    //评论计数
    private Integer commentCounts;
    //创建日期
    private Long createDate ;
    //总结
    private String summary;
    //标题
    private String title;
    //观看次数
    private Integer viewCounts;
    //置顶
    private Integer weight;
    //作者ID
    private Long authorId;
    //内容ID
    private Long bodyId;
    //类别ID
    private Long categoryId;

}
