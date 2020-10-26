package com.picard.community.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        //方法名simpleDateFormat就是bean名字
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
