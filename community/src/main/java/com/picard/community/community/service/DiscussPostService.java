package com.picard.community.community.service;

import com.picard.community.community.dao.DiscussPostMapper;
import com.picard.community.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
        return discussPostMapper.selectDiscussPosts(userId,offset,limit);
    }
    public int findDiscussPosts(int userId){
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
