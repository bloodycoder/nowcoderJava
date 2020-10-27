package com.picard.community.community;

import com.picard.community.community.dao.AlphaDao;
import com.picard.community.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.security.RunAs;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	@Test
	public void testapplicationContext(){
		System.out.println(this.applicationContext);
		//测试primary
		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class);
		System.out.println(alphaDao.select());
		//按名字获取
		alphaDao = applicationContext.getBean("alphahibernate",AlphaDao.class);
		System.out.println(alphaDao.select());
	}
	@Test
	public void testBeanManagement(){
		//测试alphaservice
		AlphaService alphaService= applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		alphaService= applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}
	@Test
	public void testBeanConfig(){
		//测试alphaservice
		SimpleDateFormat simpledateformat = applicationContext.getBean("simpleDateFormat",SimpleDateFormat.class);
		System.out.println(simpledateformat.format(new Date()));
	}
	@Autowired
	@Qualifier("alphahibernate")
	private AlphaDao alphaDao;
	@Autowired
	private AlphaService alphaservice;
	@Test
	public void testDI(){
		System.out.println(alphaDao);
		System.out.println(alphaservice);
	}
}
