package com.picard.community.community.controller;

import com.picard.community.community.annotation.LoginRequired;
import com.picard.community.community.entity.User;
import com.picard.community.community.service.LikeService;
import com.picard.community.community.service.UserService;
import com.picard.community.community.util.CommunityUtil;
import com.picard.community.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private LikeService likeService;
    @LoginRequired
    @RequestMapping(path="/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @RequestMapping(path="/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","尚未选择图片");
            return "/site/setting";
        }
        String filename = headerImage.getOriginalFilename();
        String sufix = filename.substring(filename.lastIndexOf("."));
        if(StringUtils.isBlank(sufix)){
            model.addAttribute("error","文件格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        filename = CommunityUtil.generateUUID()+sufix;
        File dest = new File(uploadPath+"/"+filename);
        try {
            //存储文件
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("上传图片失败:"+e.getMessage());
            throw new RuntimeException("上传图片失败,服务器异常",e);
        }
        //更新当前用户头像
        // http://localhost:8080/user/header/xxx.png
        User user = hostHolder.getUser();
        String headerUrl = domain+ "/user/header/"+filename;
        userService.updateHeader(user.getId(),headerUrl);
        return "redirect:/index";
    }
    @RequestMapping(path="/header/{filename}",method=RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String filename, HttpServletResponse response){
        //服务器存放的路径
        filename = uploadPath+"/"+filename;
        //文件的后缀
        String sufix = filename.substring(filename.lastIndexOf("."));
        response.setContentType("image/"+sufix);
        try(
                FileInputStream fin = new FileInputStream(filename);
                OutputStream os = response.getOutputStream();
                ){
            byte[]buffer = new byte[1024];
            int b = 0;
            while((b = fin.read(buffer))!=-1){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("读取头像失败:"+e.getMessage());
            e.printStackTrace();
        }
    }
    @RequestMapping(path="/updatepassword",method = RequestMethod.POST)
    public String updatePassword(Model model,String oldPassword,
                                 String newPassword1,String newPassword2){
        User user = hostHolder.getUser();
        Map<String,Object> map = userService.updatePassword(user.getId(),oldPassword,newPassword1,newPassword2);
        if(map == null || map.isEmpty()){
            model.addAttribute("msg","密码修改成功!");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{
            model.addAttribute("oldPasswordMsg",map.get("oldPasswordMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            return "/site/setting";
        }
    }
    @RequestMapping(path="/profile/{userId}",method = RequestMethod.GET)
    public String getProfilePage(@PathVariable("userId")int userId, Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException(String.format("用户%d不存在",userId));
        }
        //
        model.addAttribute("user",user);
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);
        return "/site/profile";
    }

}
