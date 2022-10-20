package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    public Integer countPermissionByRoleId(Integer roleId);

    public void deletePermissionByRoleId(Integer roleId);

    public List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);
}