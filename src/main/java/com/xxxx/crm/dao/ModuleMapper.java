package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.vo.Module;

import java.util.List;

public interface ModuleMapper  extends BaseMapper<Module,Integer>{
     //查询所有的资源列表
    public List<TreeModel> queryAllModules();
}