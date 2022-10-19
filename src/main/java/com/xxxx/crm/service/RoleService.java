package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Role;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Resource
    private RoleMapper roleMapper;

    /**
     * 查询所有角色信息
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色操作
     * 1.参数校验
     * 角色名roleName 不能为空 名称唯一
     * 2.设置默认值
     * is_valid = 1
     * 创建时间
     * 更新时间
     * 3.执行添加操作  判断受影响行数
     *
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role) {
        /*1.参数校验*/
        //角色名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName() ) ,"角色名不能为空");
        //角色名唯一  通过用户名查询对象
        Role temp = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(temp != null,"角色名已存在，请重新输入！");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        /*3.执行添加操作，判断受影响行数*/
        AssertUtil.isTrue(roleMapper.insertSelective(role) < 1,"角色添加失败，请重试！");
    }
    /**
     * 更新角色操作
     * 1.参数校验
     * 角色id 不能为空 且数据存在
     * 角色名称非空且唯一
     * 2.设置默认值
     * is_valid
     * 更新时间
     * 3.执行更新操作  判断受影响行数
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        /*1.参数校验*/
        //角色名不能为空
        AssertUtil.isTrue(role.getId() == null ,"待更新记录不存在");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(temp == null,"待更新记录不存在");
        //角色名不能为空
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName() ) ,"角色名不能为空");
        //角色名唯一  通过用户名查询角色记录是否存在（如果不存在，则表示可以使用，如果存在 且角色ID与当前更新的角色ID不一样，则不呢个使用）
        Role tempRole = roleMapper.selectByRoleName(role.getRoleName());
        AssertUtil.isTrue(tempRole != null && (!tempRole.getId().equals(role.getId())),"角色名已存在，请重新输入！");
        /*2，设置默认值*/
        role.setUpdateDate(new Date());
        /*3.执行添加操作，判断受影响行数*/
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"角色更新失败，请重试！");
    }

    /**
     * 删除角色信息
     * 1.参数校验
     *   角色ID 非空 数据存在
     * 2.设置相关参数默认
     *  是否有效 0（删除记录）
     *  更新时间 设置
     *  3.执行更新操作 判断受影响行数
     * @param roleId
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
    /*1.参数校验*/
        //id 不能为空  通哟id查询角色记录不能为空
      AssertUtil.isTrue(roleId == null ,"待删除记录不存在");
      Role role = roleMapper.selectByPrimaryKey(roleId);
      AssertUtil.isTrue(role == null,"待删除记录不存在");
      /*2.设置默认值*/
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        /*3.执行删除操作*/
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role) < 1,"删除记录失败，请重试");
    }
}
