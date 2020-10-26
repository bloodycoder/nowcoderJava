package com.picard.community.community.service;

import com.picard.community.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
@Scope("prototype")
public class AlphaService {
    @Autowired
    @Qualifier("alphahibernate")
    private AlphaDao alphaDao;
    public AlphaService(){
        System.out.println("实例化service");
    }
    @PostConstruct
    public void init(){
        System.out.println("初始化service");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("销毁service");
    }
    public String find(){
        return alphaDao.select();
    }
}
