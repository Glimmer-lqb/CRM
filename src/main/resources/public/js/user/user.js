layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;

    /**
     * 加载数据表格
     */
    var  tableIns = table.render({
        elem: '#userList', // 表格绑定的ID
        url : ctx + '/user/list', // 访问数据的地址（后台的数据接口）
        cellMinWidth : 95,//单元格最小的宽度
        page : true, // 开启分页
        height : "full-125",//容器高度
        limits : [10,15,20,25],//每页页数的可选项
        limit : 10,//默认每页显示的数量
        toolbar: "#toolbarDemo",//开启头部工具栏
        id :'userTable',
        cols : [[
            {type: "checkbox", fixed:"left", width:50},
            {field: "id", title:'编号',fixed:"true", width:80},
            {field: 'userName', title: '用户名', minWidth:50, align:"center"},
            {field: 'email', title: '用户邮箱', minWidth:100, align:'center'},
            {field: 'phone', title: '用户电话', minWidth:100, align:'center'},
            {field: 'trueName', title: '真实姓名', align:'center'},
            {field: 'createDate', title: '创建时间',
                align:'center',minWidth:150},
            {field: 'updateDate', title: '更新时间',
                align:'center',minWidth:150},
            {title: '操作', minWidth:150,
                templet:'#userListBar',fixed:"right",align:"center"}
        ]]
    });
    /**
     * 搜索按钮的点击事件
     */
    $(".search_btn").click(function () {

        /**
         * 表格重载
         *  多条件查询
         */
        tableIns.reload({
            // 设置需要传递给后端的参数
            where: { //设定异步数据接口的额外参数，任意设
                // 通过文本框/下拉框的值，设置传递的参数
                userName: $("[name='userName']").val() // 客户名称
                ,email: $("[name='email']").val() // 创建人
                ,phone:$("[name='phone']").val() // 状态
            }
            ,page: {
                curr: 1 // 重新从第 1 页开始
            }
        });

    });
    /**
     * 监听头部工具栏事件
     *  格式：
     *      table.on('toolbar(数据表格的lay-filter属性值)', function (data) { })
     */
    table.on('toolbar(users)', function (data) {
        // data.event：对应的元素上设置的lay-event属性值
        // console.log(data);
        // 判断对应的事件类型
        if (data.event == "add") {
            // 打开添加操作或修改对话框
            openAddOrUpdateUserDialog();

        } else if (data.event == "del") {
            // 删除操作
            deleteSaleChance(data);
        }
    })
});
/**
 * 打开用户添加或更新对话框
 */
function openAddOrUpdateUserDialog() {
    var url  =  ctx + "/user/addOrUpdateUserPage";
    var title = "用户管理-用户添加";
    // if(userId){
    //     url = url + "?id="+userId;
    //     title = "用户管理-用户更新";
    // }
    layui.layer.open({
        title : title,
        type : 2,
        area:["650px","400px"],
        maxmin:true,
        content : url
    });
}