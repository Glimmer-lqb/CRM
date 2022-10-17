package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.CusDevPlanService;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("cus_dev_plan")
@Controller
public class CusDevPlanController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private CusDevPlanService cusDevPlanService;

    /***
     * 进入客户开发计划页面
     */
    @RequestMapping("index")
    public String index() {
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanPage")
    public String toCusDevPlanPage(Integer id, HttpServletRequest request) {
        //通过id查询营销机会
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        request.setAttribute("saleChance", saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    /**
     * 营销机会对应的数据查询（分页数据查询）
     * 如果flag的值不为空，且值为1，则表示当前查询的是客户开发计划；否则查询营销机会查询
     *
     * @param cusDevPlanQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams(CusDevPlanQuery cusDevPlanQuery) {

        return cusDevPlanService.cusDevPlanByParams(cusDevPlanQuery);
    }

    /**
     * 添加计划项
     *
     * @param cusDevPlan
     * @return
     */
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("添加计划项成功");
    }
    /**
     * 更新计划项
     * @param cusDevPlan
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateCusDevPlan(CusDevPlan cusDevPlan) {
        cusDevPlanService.updateCusDevPlan(cusDevPlan);
        return success("更新计划项成功");
    }
    /**
     * 进入添加或修改计划项的页面
     * @return
     */
    @RequestMapping("toAddOrUpdateCusDevPlanPage")
    public String toAddOrUpdateCusDevPlanPage(Integer sId, HttpServletRequest request,Integer id){
        //将营销机会id设置到请求域中，给计划项页面获取
        request.setAttribute("sId",sId);
        //通过计划项Id查询记录
        CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
        //将计划项数据设置到请求域中·
        request.setAttribute("cusDevPlan", cusDevPlan);
        return "cusDevPlan/add_update";
    }
    /**
     * 删除计划项
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlanPage(Integer id){
        cusDevPlanService.deleteCusDevPlan(id);
        return success("计划项删除成功");
    }


}
