package com.picard.community.community.controller;

import com.picard.community.community.entity.Comment;
import com.picard.community.community.entity.DiscussPost;
import com.picard.community.community.entity.Page;
import com.picard.community.community.entity.User;
import com.picard.community.community.service.CommentService;
import com.picard.community.community.service.DiscussPostService;
import com.picard.community.community.service.UserService;
import com.picard.community.community.util.CommunityConstant;
import com.picard.community.community.util.CommunityUtil;
import com.picard.community.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @RequestMapping(path="/add",method= RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        User user = hostHolder.getUser();
        if(user == null){
            return CommunityUtil.getJSONString(403,"你还未登陆");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJSONString(0,"发布成功!");
    }
    @RequestMapping(path="/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子
        DiscussPost discussPost = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
        //
        User user = userService.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);

        //查评论信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" +discussPostId);
        page.setRows(discussPost.getCommentCount());
        //评论，给帖子的评论
        //回复，给评论的评论
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST,discussPost.getId(),page.getOffset(),page.getLimit());
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList!=null){
            for(Comment comment:commentList){
                //评论VO
                Map<String,Object>commentVo = new HashMap<>();
                //评论
                commentVo.put("comment",comment);
                //作者
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                //回复列表
                List<Comment>replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);
                //回复vo列表
                List<Map<String,Object>>replyVolist = new ArrayList<>();
                if(replyList!=null){
                    for(Comment reply:replyList){
                        Map<String,Object>replyVo = new HashMap<>();
                        //回复
                        replyVo.put("reply",reply);
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User target = reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        replyVolist.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVolist);
                int replyCnt = commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("replyCount",replyCnt);
                /*
                * commentVoList = [{"comment":xx,"user":xx,replys:[{"reply":}]}]
                * cvo = {"comment":xx,"user":xx,replys:[{"reply":}]}
                * */
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        return "/site/discuss-detail";
    }
}
