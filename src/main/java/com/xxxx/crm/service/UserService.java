package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     */
//    Service层： （业务逻辑层：非空判断，条件判断等业务逻辑处理）
    public UserModel userLogin(String userName, String userPwd) {
        // 1.参数判断，判断用户姓名、密码非空
        checkLoginParams(userName, userPwd);
        //2.调用数据访问层，通过用户名查询用户记录，返回用户对象
        User user = userMapper.queryUserByName(userName);
        //            3.判断用户对象是否为空
        //    如果为空，抛出异常（异常被控制层捕获并处理）
        AssertUtil.isTrue(user == null, "用户姓名不存在！");

        //    4.判断密码是否正确，比较客户端传递的用户密码与数据库中的查询的用户对象中的用户密码
        //    如果密码不相等，抛出异常（异常被控制层捕获并处理）
        checkUserPwd(userPwd, user.getUserPwd());
        return buildUserInfo(user);
    }

    /**
     * 修改密码
     *  1. 接收四个参数 （用户ID、原始密码、新密码、确认密码）
     *  2. 通过用户ID查询用户记录，返回用户对象
     *  3. 参数校验
     *     待更新用户记录是否存在 （用户对象是否为空）
     *     判断原始密码是否为空
     *     判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
     *     判断新密码是否为空
     *     判断新密码是否与原始密码一致 （不允许新密码与原始密码）
     *     判断确认密码是否为空
     *     判断确认密码是否与新密码一致
     *  4. 设置用户的新密码
     *   需要将新密码通过指定算法进行加密（md5加密）
     *  5. 执行更新操作，判断受影响的行数
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String repeatPwd ) {
        //2. 通过用户ID查询用户记录，返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        //判断用户记录是否存在
        AssertUtil.isTrue(user == null,"待更新记录不存在");
        //参数校验
        checkPasswordParams(user,oldPwd,newPwd,repeatPwd);
//        4. 设置用户的新密码
//        需要将新密码通过指定算法进行加密（md5加密）
        user.setUserPwd(Md5Util.encode(newPwd));
        //5. 执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"修改密码失败");

    }

    /**
     * 修改密码的参数判断
     * 参数校验
     * 待更新用户记录是否存在 （用户对象是否为空）
     * 判断原始密码是否为空
     * 判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
     * 判断新密码是否为空
     * 判断新密码是否与原始密码一致 （不允许新密码与原始密码）
     * 判断确认密码是否为空
     * 判断确认密码是否与新密码一致
     * @param user
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParams(User user, String oldPwd, String newPwd, String repeatPwd) {
       //判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码不能为空");
        //判断原始密码是否正确（查询的用户对象中的用户密码是否原始密码一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原密码一致");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"确认密码不能为空");
        AssertUtil.isTrue(!newPwd.equals(repeatPwd),"确认密码与新密码不一致");
    }


    /**
     * 构建需要返回给客户端的用户对象
     *
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        userPwd = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!userPwd.equals(pwd), "用户密码错误");
    }

    public void checkLoginParams(String userName, String userPwd) {
         //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户姓名不能为空");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空");

    }

    public UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //userModel.setUserId(user.getId());
        //加密后的用户id
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }
//            5.如果密码正确，登陆成功

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String, Object>> queryAllSales(){
       return  userMapper.queryAllSales();
    }

}