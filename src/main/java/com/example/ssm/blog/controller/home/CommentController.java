package com.example.ssm.blog.controller.home;

import com.example.ssm.blog.entity.Article;
import com.example.ssm.blog.entity.Comment;
import com.example.ssm.blog.enums.ArticleStatus;
import com.example.ssm.blog.enums.Role;
import com.example.ssm.blog.service.ArticleService;
import com.example.ssm.blog.service.CommentService;
import com.example.ssm.blog.util.Functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author 言曌
 * @date 2017/9/10
 */

@Controller
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    /**
     * '添加评论
     *
     * @param request
     * @param comment
     */
    @RequestMapping(value = "/comment", method = {RequestMethod.POST})
    @ResponseBody
    public void insertComment(HttpServletRequest request, Comment comment) {
        //添加评论
        comment.setCommentCreateTime(new Date());
        comment.setCommentIp(Functions.getIpAddr(request));
        if (request.getSession().getAttribute("user") != null) {
            comment.setCommentRole(Role.ADMIN.getValue());
        } else {
            comment.setCommentRole(Role.VISITOR.getValue());
        }
        comment.setCommentAuthorAvatar(Functions.getGravatar(comment.getCommentAuthorEmail()));
        commentService.insertComment(comment);

        //更新文章的评论数
        Article article = articleService.getArticleByStatusAndId(ArticleStatus.PUBLISH.getValue(), comment.getCommentArticleId());
        articleService.updateCommentCount(article.getArticleId());
    }


}
