package com.picard.community.community.util;

import com.picard.community.community.service.SimpleRateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SimpleRateLimiter implements CommunityConstant{
    static final int PERIOD_PUBLISH = 3600*24;
    static final int MAXLIMIT_PUBLISH = 3;
    @Autowired
    SimpleRateLimiterService rateLimiterService;
    public boolean isPostAllowed(int userId){
        return rateLimiterService.shouldBeAllowed(userId,TOPIC_PUBLISH,PERIOD_PUBLISH,MAXLIMIT_PUBLISH);
    }
}
