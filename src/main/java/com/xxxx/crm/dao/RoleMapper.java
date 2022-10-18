package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    //查询所有的角色列表(只需要查询 id 和角色名字roleName)
    public List<Map<String,Object>> queryAllRoles(Integer userId);

}