package com.picard.community.community.service;

import com.picard.community.community.util.HostHolder;
import com.picard.community.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    @Autowired
    private RedisTemplate redisTemplate;
    //点赞
    public void like(int userId,int entityType,int entityID){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityID);
        boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey,userId);
        if(isMember){
            redisTemplate.opsForSet().remove(entityLikeKey,userId);
        }else{
            redisTemplate.opsForSet().add(entityLikeKey,userId);
        }
    }
    //查询实体点赞数量
    public long findEntityLikeCount(int entityType,int entityID){
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType,entityID);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    //查询自己是否点赞
    public int findEntityLikeStatus(int userId,int entityType,int entityID){
        String entityLikeKey =  RedisKeyUtil.getEntityLikeKey(entityType,entityID);
        return redisTemplate.opsForSet().isMember(entityLikeKey,userId)?1:0;
    }
}
