package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.BaseQuery;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.query.UserQuery;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.User;
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

    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    /**
     * 添加用户·
     * @param user
     * @return
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user) {
        userService.addUser(user);
        return success("用户添加成功");
    }
    /**
     * 更新用户信息·
     * @param user
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user) {
        userService.updateUser(user);
        return success("用户更新成功");
    }
    /**
     * 删除用户信息
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids) {
        userService.deleteByIds(ids);
        return success("用户删除成功");
    }
    /**
     * 打开添加或修改用户的页面
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String openAddOrUpdateUserDialog(Integer id,HttpServletRequest request) {
        //判断id是否为空 不为空执行更新操作 查询用户对象
        if(id != null ){
            User user = userService.selectByPrimaryKey(id);
            //将数据设置到请求域中
            request.setAttribute("userInfo",user);
        }
        return "user/add_update";
    }
}
