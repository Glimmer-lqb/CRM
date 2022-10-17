package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    /**
     * 系统登录页面
     *
     * @return
     */
    @Resource
   private UserService userService;
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    //系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("main")
    public String main(HttpServletRequest request) {

         //获取cookie中的用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
         //查询用户对象，设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        return "main";
    }

}
