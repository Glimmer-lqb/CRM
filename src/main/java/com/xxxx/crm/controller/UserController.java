package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.BaseQuery;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("login")
    @ResponseBody
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //通过try catch捕获service层的异常，如果service层输出异常，则表示登陆失败
        UserModel userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);
//        try {
//
//        } catch (ParamsException p) {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.getStackTrace();
//        } catch (Exception e) {
//            resultInfo.setCode(500);
//            resultInfo.setMsg("登录失败");
//        }
        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     * @param request
     * @param oldPassword
     * @param newPassword
     * @param repeatPassword
     * @return
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request, String oldPassword,
                                         String newPassword, String repeatPassword) {
        ResultInfo resultInfo = new ResultInfo();
        //获取cookie中的userId
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        //调用Service层的修改密码方法
        userService.updatePassWord(userId, oldPassword, newPassword, repeatPassword);
//        try {
//
//        } catch (ParamsException p) {
//            resultInfo.setCode(p.getCode());
//            resultInfo.setMsg(p.getMsg());
//            p.printStackTrace();
//        }catch(Exception e){
//            resultInfo.setCode(500);
//            resultInfo.setMsg("修改密码失败!");
//            e.printStackTrace();
//        }
        return resultInfo;
    }

    /**
     * 进入修改密码页面
     *
     * @return
     */
    @RequestMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> selectByParams(UserQuery userQuery) {
        return userService.queryByParamsForTable(userQuery);
    }
}