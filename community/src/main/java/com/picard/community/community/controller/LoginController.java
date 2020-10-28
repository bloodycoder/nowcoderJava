package com.picard.community.community.controller;

import com.picard.community.community.entity.User;
import com.picard.community.community.service.UserService;
import com.picard.community.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;
    @RequestMapping(path="/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path="/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    @RequestMapping(path="/register",method = RequestMethod.POST)
    public String getRegisterPage(Model model, User user){
        Map<String,Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","注册成功,我们已经向您的邮箱发送了一封激活邮件");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }
    }
    ///activation/151/8f7dcfaa0702493dbd2a7fffcd82cb3a
    @RequestMapping(path="/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String code){
        int res = userService.activation(userId,code);
        if(res == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功,可以正常使用");
            model.addAttribute("target","/login");
        }else if(res == ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已经激活");
            model.addAttribute("target","/index");
        }else{
            model.addAttribute("msg","激活码错误");
            model.addAttribute("target","/index");
        }
        return "site/operate-result";
    }
}
