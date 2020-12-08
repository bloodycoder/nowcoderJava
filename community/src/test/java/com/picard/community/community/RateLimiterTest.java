package com.picard.community.community;

import com.picard.community.community.util.SimpleRateLimiter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RateLimiterTest {
    @Autowired
    SimpleRateLimiter rateLimiter;
    @Test
    public void test1(){
        boolean ans1 = rateLimiter.isPostAllowed(101);
        boolean ans2 = rateLimiter.isPostAllowed(101);
        boolean ans3 = rateLimiter.isPostAllowed(101);
        boolean ans4 = rateLimiter.isPostAllowed(101);
        System.out.println("123");
    }
}