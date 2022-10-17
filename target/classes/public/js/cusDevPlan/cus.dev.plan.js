layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;
    /**
     * 客户开发数据列表
     */
    var  tableIns = table.render({
        elem: '#saleChanceList',
        url : ctx+'/sale_chance/list?flag=1',
        cellMinWidth : 95,
        page : true,
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "saleChanceTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'chanceSource', title: '机会来源',align:"center"},
            {field: 'customerName', title: '客户名称',  align:'center'},
            {field: 'cgjl', title: '成功几率', align:'center'},
            {field: 'overview', title: '概要', align:'center'},
            {field: 'linkMan', title: '联系人',  align:'center'},
            {field: 'linkPhone', title: '联系电话', align:'center'},
            {field: 'description', title: '描述', align:'center'},
            {field: 'createMan', title: '创建人', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'devResult', title: '开发状态',
                align:'center',templet:function (d) {
                    return formatDevResult(d.devResult);
                }},
            {title: '操作',fixed:"right",align:"center",
                minWidth:150,templet:"#op"}
        ]]
    });
    /**
     * 格式化开发状态
     * @param value
     * @returns {string}
     */
    function formatDevResult(devResult){
        if(devResult == 0) {
            return "<div style='color: yellow'>未开发</div>";
        } else if(devResult==1) {
            return "<div style='color: orange'>开发中</div>";
        } else if(devResult==2) {
            return "<div style='color: green'>开发成功</div>";
        } else if(devResult==3) {
            return "<div style='color: red'>开发失败</div>";
        } else {
            return "<div style='color: blue'>未知</div>"
        }
    }
    /**
     * 绑定搜索按钮的点击事件
     */
    $(".search_btn").click(function () {
        table.reload('saleChanceTable', {
            where: { //设定异步数据接口的额外参数，任意设
                customerName: $("input[name='customerName']").val(),  // 客户名
                createMan: $("input[name='createMan']").val(),  // 创建人
                devResult: $("#devResult").val()  // 开发状态
            }
            ,page: {
                curr: 1 // 重新从第 1 页开始
            }
        }); // 只重载数据
    });
    /**
     * 行监听事件
     */
    table.on("tool(saleChances)", function (data) {

        if (data.event == "info") {
            // 打开计划项开发与详情页面 ——详情
            openCusDevPlanDialog("计划项数据维护", data.data.id);
        } else if (data.event == "dev") {
            // 开发
            openCusDevPlanDialog("计划项数据开发", data.data.id);
        }
    });
    /**
     * 打开开发计划开饭或详情对话框
     * @param title
     * @param id
     */
    function openCusDevPlanDialog(title, id) {
        layui.layer.open({
            title:title,
            type: 2,
            area:["750px","550px"],
            maxmin: true,
            content:ctx + "/cus_dev_plan/toCusDevPlanPage?id=" + id
        });
    }
});

