layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    /**
     * 用户登录 表单submit提交
     * return false //阻止表单跳转
     */
    form.on("submit(login)", function (data) {
        console.log(data.field)//当前容器的全部表单字段，名值对形式：{name: value}

        // 发送 ajax 请求，传递用户姓名与密码。执行用户登录操作
        //ctx  当前项目路径

        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            data: {
                userName: data.field.username,
                userPwd: data.field.password
            },
            success: function (result) {//result 回调函数用来接收后端返回的数据
                console.log(result);
                // 判断是否登录成功
                if (result.code == 200) {
                    //登陆成功
                    layer.msg("登录成功！", function () {
                        //判断用户是否选择记住密码（判断复选框是否被选中，如果选中）
                        if($("#rememberMe").prop("checked")){
                            // 将用户信息存到cookie中
                            $.cookie("userIdStr", result.result.userIdStr,{expires : 7});
                            //加密后的用户id
                            $.cookie("userName", result.result.userName,{expires : 7});
                            $.cookie("trueName", result.result.trueName,{expires : 7});

                        }else{
                            // 将用户信息存到cookie中
                            $.cookie("userIdStr", result.result.userIdStr);
                            //加密后的用户id
                            $.cookie("userName", result.result.userName);
                            $.cookie("trueName", result.result.trueName);
                        }

                        // 登录成功后，跳转到首页
                        window.location.href = ctx + "/main";
                    });
                } else {
                    // 提示信息 登陆失败
                    layer.msg(result.msg,{icon:5});
                }
            }
        });
        // 阻止表单跳转
        return false;
    });
});

