$(function () {
    // 加载树形结构
    loadModuleData();
});

// 定义树形结构对象
var zTreeObj;


/**
 * 加载资源树形数据
 */
function loadModuleData() {
    // 配置信息对象  zTree的参数配置
    var setting = {
        // 使用复选框
        check: {
            enable: true
        },
        // 使用简单的JSON数据
        data: {
            simpleData: {
                enable: true
            }
        }
    }
        // 绑定函数
    //     callback: {
    //         // onCheck函数：当 checkbox/radio 被选中或取消选中时触发的函数
    //         onCheck: zTreeOnCheck
    //     }
    // };

    // 数据
    // 通过ajax查询资源列表
    $.ajax({
        type:"get",
        url:ctx + "/module/queryAllModules",
        // 查询所有的资源列表时，传递角色ID，查询当前角色对应的已经授权的资源
        data:{
            roleId:$("[name='roleId']").val()
        },
        dataType:"json",
        success:function (data) {
            // data:查询到的资源列表
            // 加载zTree树插件
            zTreeObj = $.fn.zTree.init($("#test1"), setting, data);
        }
    });
}