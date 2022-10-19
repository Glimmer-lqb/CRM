package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.model.TreeModel;
import com.xxxx.crm.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("module")
@Controller
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;
    /**
     * 查询所有的资源列表
     *
     *
     * 乐字节：专注线上IT培训
     * 答疑老师微信：lezijie
     * @param
     * @return java.util.List<com.xxxx.crm.model.TreeModel>
     */
    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeModel> queryAllModules() {
        return moduleService.queryAllModules();
    }

    /***
     * 进入授权页面
     * @param
     * @return java.lang.String
     */
    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, HttpServletRequest request) {
        // 将需要授权的角色ID设置到请求域中
        request.setAttribute("roleId", roleId);
        return "role/grant";
    }
}
