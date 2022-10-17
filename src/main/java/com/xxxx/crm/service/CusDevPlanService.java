package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.dao.CusDevPlanMapper;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.CusDevPlanQuery;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.CusDevPlan;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {
    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会
     * 返回的数据格式必须满足LayUi中数据表格要求的格式，才能显示
     *
     * @param cusDevPlanQuery
     * @return
     */

    public Map<String, Object> cusDevPlanByParams(CusDevPlanQuery cusDevPlanQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CusDevPlan> pageInfo = new PageInfo<>(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加客户开发计划项数据
     * 1.参数校验
     * 营销机会id 非空 数据存在
     * 计划项内容 非空
     * 计划时间 非空
     * 2.设置默认数据
     * 是否有效  默认有效
     * 创建时间 默认系统当前时间
     * 修改时间 系统默认是按
     * 3.执行添加操作 ，判断受影响行数
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        /*1.参数校验*/
        checkCusDevPlanParams(cusDevPlan);
        /*2.设置默认值*/
        //是否有效 默认有效
        cusDevPlan.setIsValid(1);
        //创建时间 系统当前时间
        cusDevPlan.setCreateDate(new Date());
        //修改时间 默认系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        /*3. 执行添加操作 判断受影响行数*/
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) != 1, "添加客户开发计划项数据失败！");
    }

    /**
     * 更新客户开发计划项数据
     * 1.参数校验
     * 计划项ID  非空 数据存在
     * 营销机会id 非空 数据存在
     * 计划项内容 非空
     * 计划时间 非空
     * 2.设置默认数据
     * 修改时间 系统默认是按
     * 3.执行更新操作 ，判断受影响行数
     *
     * @param cusDevPlan
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCusDevPlan(CusDevPlan cusDevPlan) {
        /*1.参数校验*/
        //计划项Id校验
        AssertUtil.isTrue(cusDevPlan.getId() ==null
                || cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"数据异常，请重试！");
        checkCusDevPlanParams(cusDevPlan);
        /*2.设置默认值*/
        //修改时间 默认系统当前时间
        cusDevPlan.setUpdateDate(new Date());
        /*3.执行更新操作*/
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项更新失败");
    }

    /**
     * 参数校验
     *
     * @param cusDevPlan
     */
    private void checkCusDevPlanParams(CusDevPlan cusDevPlan) {
        //验证营销机会id  非空 数据存在
        Integer sId = cusDevPlan.getSaleChanceId();
        AssertUtil.isTrue(sId == null || saleChanceMapper.selectByPrimaryKey(sId) == null, "数据异常，请重试！");
        //计划项内容 非空
        AssertUtil.isTrue(StringUtils.isBlank(cusDevPlan.getPlanItem()), "计划向内容不为空");
        //计划时间不能为空
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "计划项时间不能为空");
    }

    /**
     * 删除计划项
     * 1.判断id是否为空 对象是否存在
     * 2.设置is_valid默认值
     * 3.执行更新操作
     * @param id
     */
    public void deleteCusDevPlan(Integer id){
        AssertUtil.isTrue(id == null ,"待删除对象不存在");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);

        cusDevPlan.setIsValid(0);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) != 1,"计划项删除失败");
    }

}
