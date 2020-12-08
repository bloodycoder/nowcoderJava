package com.picard.community.community.service;

import com.picard.community.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SimpleRateLimiterService {
    @Autowired
    private RedisTemplate redisTemplate;
    //检查行动是否被允许
    public boolean shouldBeAllowed(int userId,String actionKey,int period, int maxCount){
        String limiterKey = RedisKeyUtil.getLimiter(userId,actionKey);
        long nowTs = System.currentTimeMillis();
        redisTemplate.opsForZSet().add(limiterKey,nowTs,nowTs);
        redisTemplate.opsForZSet().removeRangeByScore(limiterKey,0,nowTs-period*1000);
        long count = redisTemplate.opsForZSet().zCard(limiterKey);
        redisTemplate.expire(limiterKey,period+1, TimeUnit.SECONDS);
        return count<=maxCount;
    }
}
