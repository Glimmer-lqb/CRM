package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.SaleChanceMapper;
import com.xxxx.crm.enums.DevResult;
import com.xxxx.crm.enums.StateStatus;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    /**
     * 多条件分页查询营销机会
     * 返回的数据格式必须满足LayUi中数据表格要求的格式，才能显示
     *
     * @param saleChanceQuery
     * @return
     */
    public Map<String, Object> querySaleChanceByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
        //得到对应的分页对象
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.selectByParams(saleChanceQuery));
        //设置map对象
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        //设置分页好的列表
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 添加营销机会
     * 1.参数校验
     * customerName客户名称  非空
     * linkMan联系人       非空
     * linkPhone联系电话   非空，手机号码格式正确
     * 2.设置相关参数默认值
     * creatMan 创建人    当前登录用户名
     * assignMan指派人
     * 如果未设置指派人（默认）
     * state分配状态  0未分配  1已分配
     * 0 = 未分配
     * 指派时间  设置为null
     * 如果设置了指派人
     * 3.执行添加操作，判断受影响行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addSaleChance(SaleChance saleChance) {
        /*1.校验参数 */
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(),
                saleChance.getLinkPhone());
        /*2.设置相关字段的默认值*/
        //isValid是否有效 （0无效  1有效）设置为有效 1=有效
        saleChance.setIsValid(1);
        //createDate 创建时间 默认为系统当前时间
        saleChance.setCreateDate(new Date());
        //updateDate 修改时间 默认为系统当前时间
        saleChance.setUpdateDate(new Date());
        //判断是否设置了指派人
        if (StringUtils.isBlank(saleChance.getAssignMan())) {
            //如果为空则表示未设置指派人
            //state 分配状态 未分配=0  使用枚举类传参
            saleChance.setState(StateStatus.UNSTATE.getType());
            //assignTime 直排时间 设置为null
            saleChance.setAssignTime(null);
            //devResult 设置开发状态  0=未开发 1=开发中 2= 开放成功 3= 开发失败  默认为 未开发 = 0
            saleChance.setDevResult(DevResult.UNDEV.getStatus());
        } else {
            //不为空则  表示设置了指派人
            //分配状态 已分配
            saleChance.setState(StateStatus.STATED.getType());
            //设置指派时间 为当前系统时间
            saleChance.setAssignTime(new Date());
            //设置开放状态 为开发中=1  使用枚举类传参
            saleChance.setDevResult(DevResult.DEVING.getStatus());
        }
        /* 3.执行添加操作  判断受影响行数*/
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) != 1, "添加营销机会失败");
    }

    /**
     * 更新营销机会
     * 1.参数校验
     * 营销机会ID   非空  数据库中对应的记录存在
     * customerName客户名称  非空
     * linkMan联系人       非空
     * linkPhone联系电话   非空，手机号码格式正确
     * 2.设置相关参数默认值
     * updateDate  更新时间 设置为系统当前时间
     * assignMan 指派人
     * 原始数据未设置 （修改后未设置（不需要更新） 修改后已设置（指派时间设置为系统当前时间、
     * 分配状态已分配开发状态开发中））
     * 和 已设置两种情况（修改后未设置（null  未分配  未开发）
     * 修改后已设置（判断修改前后是否是同一个指派人 如果是不需操作  如果不是，则需要更新 assignTime））
     * 3.执行更新操作  判断受影响行数
     *
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance(SaleChance saleChance) {
        /*1.判断id*/
        AssertUtil.isTrue(null == saleChance.getId(), "待更新记录不存在");
        //通过主键查询对象
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(null == temp, "待更新记录不存在！");
        //参数校验
        checkSaleChanceParams(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        /*2.设置默认值*/
        saleChance.setUpdateDate(new Date());
        //判断修改的原始数据是否存在指派人
        if (StringUtils.isBlank(temp.getAssignMan())) {//不存在
            //判断修改后的值是否存在
            if (!StringUtils.isBlank(saleChance.getAssignMan())) {//修改前为空 修改后有值
                saleChance.setAssignTime(new Date());
                saleChance.setState(StateStatus.STATED.getType());
                saleChance.setDevResult(DevResult.DEVING.getStatus());
            }
        } else {//存在
        //判断修改后的值是否存在
            if(StringUtils.isBlank(saleChance.getAssignMan())) {
                //改前有值，如果改后不存在
                saleChance.setAssignTime(null);
                saleChance.setState(StateStatus.UNSTATE.getType());
                saleChance.setDevResult(DevResult.UNDEV.getStatus());
            }else{
                //修改前有值  修改后有值
                if(!saleChance.getAssignMan().equals(temp.getAssignMan())) {
                    //更新指派时间
                    saleChance.setAssignTime(new Date());
                }else{
                    //设置指派时间为原始时间
                    saleChance.setAssignTime(temp.getAssignTime());
                }
            }
        }
        /*3.执行更新操作，判断受影响行数*/
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"更新营销机会失败");
    }

    /**
     * 1.参数校验
     * customerName客户名称  非空
     * linkMan联系人       非空
     * linkPhone联系电话   非空，手机号码格式正确
     *
     * @param customerName
     * @param linkMan
     * @param linkPhone
     */
    private void checkSaleChanceParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系号码不能为空");
        //判断手机号码格式是否正确
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "联系号码格式不正确");
    }

    /**
     *删除营销机会
     * @param ids
     */
    public void deleteSaleChance(Integer[] ids){
    //判断id是否为空
      AssertUtil.isTrue(ids == null || ids.length < 1,"待删除记录不存在");
      //执行删除操作
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) != ids.length,"营销机会数据删除失败");
    }

    /**
     * 更新营销机会状态
     * @param id
     * @param devResult
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
    //第一步判断id是否为空
        AssertUtil.isTrue(id == null ,"待更新记录不存在");
        //通过id查询营销机会数据
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        //判断对象是否为空
        AssertUtil.isTrue(saleChance == null,"待更新对象不能为空");
        saleChance.setDevResult(devResult);
        //执行更新操作
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) != 1,"开发状态更新失败");
    }
}
