package com.picard.community.community;

import com.picard.community.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Test
    public void test1() throws InterruptedException {
        for(int i=0;i<50;i++){
            Thread.sleep(10000);
            mailClient.sendMail("caoye@nfs.iscas.ac.cn","Pornhub Date Tonight","Hello caoye,I’m 24 yrs old. I wish to find a mate which can make my nights unsleepy. Do you wish to have power over me? I will do all that you will say, with not exclusions! Get in touch with me in my personal profile and your hidden dreams will materialize! You will be able to be strict only with me and my creativeness! follow this link");
            mailClient.sendMail("linfeng@nfs.iscas.ac.cn","Pornhub Date Tonight","Hello linfeng,I’m 24 yrs old. I wish to find a mate which can make my nights unsleepy. Do you wish to have power over me? I will do all that you will say, with not exclusions! Get in touch with me in my personal profile and your hidden dreams will materialize! You will be able to be strict only with me and my creativeness! follow this link");
        }
    }
    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username","picard");
        String content = templateEngine.process("/mail/demo",context);
        System.out.println(content);
        mailClient.sendMail("510297127@qq.com","HTML",content);
    }

}
