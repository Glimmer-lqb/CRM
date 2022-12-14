layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 再执行关闭
        parent.layer.close(index);
    });
    /**
     * 监听submit事件
     * 实现营销机会的添加与更新
     */
    form.on("submit(addOrUpdateUser)", function (data) {
        // 提交数据时的加载层 （https://layer.layui.com/）
        var index = layer.msg("数据提交中,请稍后...",{
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        //得到所有的表单元素的值
        var formData = data.field;
        // 请求的地址
        var url = ctx + "/user/add";
        // 发送ajax请求
        // 通过营销机会的ID来判断当前需要执行添加操作还是修改操作
        // 如果营销机会的ID为空，则表示执行添加操作；如果ID不为空，则表示执行更新操作
        // 通过获取隐藏域中的ID
        if($("[name='id']").val()){
            var url = ctx + "/user/update";
        }
        $.post(url, formData, function (result) {
            // 操作成功
            if (result.code == 200) {
                // 提示成功
                layer.msg("操作成功！");
                // 关闭加载层
                layer.close(index);
                // 关闭弹出层
                layer.closeAll("iframe");
                // 刷新父页面，重新渲染表格数据
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon:5});
            }
        });
        return false; // 阻止表单提交
    });
    /**
     * 加载角色下拉框
     *
     * 配置远程搜索, 请求头, 请求参数, 请求类型等
     *
     * formSelects.config(ID, Options, isJson);
     *
     * @param ID        xm-select的值
     * @param Options   配置项
     * @param isJson    是否传输json数据, true将添加请求头 Content-Type: application/json; charset=UTF-8
     */
    var userId = $("[name='id']").val();
    formSelects.config("selectId",{
        type:"post", // 请求方式
        searchUrl: ctx+"/role/queryAllRoles?userId="+userId, // 请求地址
        keyName: 'roleName',  // 下拉框中的文本内容，要与返回的数据中对应key一致
        keyVal: 'id'
    }, true);
});