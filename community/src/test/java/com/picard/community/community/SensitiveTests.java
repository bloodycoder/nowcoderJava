package com.picard.community.community;

import com.picard.community.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {
    @Autowired
    SensitiveFilter sensitiveFilter;
    @Test
    public void testSensitive(){
        String text = "这里可以赌博，可以嫖娼，可以吸毒，可以开票。哈哈哈";
        System.out.println((sensitiveFilter.filter(text)));
    }
}
