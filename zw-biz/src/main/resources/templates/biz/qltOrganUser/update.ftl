<#include "/common/layoutForm.ftl">
<@header></@header>
<@body class="body-bg-default">
<div class="wrapper">
    <section class="content">
        <div class="row">
            <div class="col-md-12">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">修改用户</h3>
                    </div>
                    <form role="form" class="form-horizontal krt-form krt-width-form" id="krtForm">
                        <div class="box-body">
                            <div class="row">
                                <div class="col-sm-offset-2 col-sm-2">
                                    <div class="row" style="margin: 0px">
                                        <div class="form-group">
                                            <div class="box-body box-profile">
                                                <input type="hidden" name="avatar" value="${(user.avatar)!}" id="avatar"/>
                                                <div id='avatarUploader' class="uploader photo-center"></div>
                                                <script type="text/javascript">
                                                    $(function () {
                                                        $('#avatarUploader').krtUploader({
                                                            type: 'photo',            // 当选择文件后立即自动进行上传操作
                                                            chunk_size: "0",
                                                            url: '${basePath}/upload/fileUpload?dir=image',  // 文件上传提交地址
                                                            resultCallBack: function (data) {
                                                                $('#avatarUploader').find('.img-photo').find('img').prop('src', data);
                                                                $("#avatar").val(data);
                                                            }
                                                        });
                                                        //回显
                                                        if ($("#avatar").val() != '') {
                                                            $('#avatarUploader').find('.img-photo').find('img').prop('src', $("#avatar").val());
                                                        }
                                                    })
                                                </script>
                                                <h3 class="profile-username text-center">${(user.username)!}</h3>
                                                <div class="form-group">
                                                    <div class="col-sm-12" style="text-align: center">
                                                        <input type="radio" class="icheck" name="sex" value="0" ${((user.sex==0)?string("checked",""))!}> 男
                                                        &nbsp;&nbsp;&nbsp;&nbsp;
                                                        <input type="radio" class="icheck" name="sex" value="1" ${((user.sex==1)?string("checked",""))!}> 女
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-7">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="username" class="control-label col-sm-4">
                                                    <span class="form-required">*</span>用户账号：
                                                </label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <input type="text" name="username" id="username" value="${(user.username)!}" class="form-control" readonly placeholder="请输入用户账号" rangelength="2,20" required>
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-user"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="name" class="control-label col-sm-4">
                                                    <span class="form-required">*</span>用户姓名：
                                                </label>
                                                <div class="col-sm-8">
                                                    <input type="text" name="name" id="name" value="${(user.name)!}" class="form-control" placeholder="请输入用户姓名" rangelength="1,10" required>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="password" class="control-label col-sm-4">
                                                    用户密码：
                                                </label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <input type="password" name="password" id="password" class="form-control" placeholder="请输入密码" rangelength="6,20">
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-unlock-alt"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="password2" class="control-label col-sm-4">
                                                    重复密码：
                                                </label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <input type="password" name="password2" id="password2" class="form-control" placeholder="请再次输入密码" equalTo="#password" rangelength="6,20">
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-unlock-alt"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="birthday" class="control-label col-sm-4">出生日期：</label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control pull-right" name="birthday" value="${(user.birthday?string('yyyy-MM-dd'))!}" id="birthday" placeholder="请输入出生日期" readonly onClick="WdatePicker()">
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-calendar"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="phone" class="control-label col-sm-4">手机号码：</label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <input type="text" class="form-control" name="phone" value="${(user.phone)!}" placeholder="请输入手机号码" isMobile="true">
                                                        <div class="input-group-addon">
                                                            <i class="fa fa-phone"></i>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="organName" class="control-label col-sm-4">组织机构：</label>
                                                <div class="col-sm-8">
                                                    <div class="input-group">
                                                        <!-- 参数 -->
                                                        <input type="hidden" name="organCode" id="organCode" value="${(qltOrganDepartment.code)!}" class="form-control">
                                                        <input type="text" name="organName" id="organName" readonly value="${(qltOrganDepartment.name)!}" class="form-control" required>
                                                        <div class="input-group-btn">
                                                            <button class="btn btn-default" id="organTreeBtn" type="button"><i class="fa fa-search"></i></button>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group">
                                                <label for="roleIds" class="control-label col-sm-4">所属角色：</label>
                                                <div class="col-sm-8">
                                                    <#list roleList as role>
                                                        <#if role.code != 'admin'>
                                                            <input name="roleIds" value="${role.id}" type="checkbox" class="icheck"
                                                                <#if user.roles??>
                                                                    <#list user.roles as item>
                                                                        <#if item.id == role.id>
                                                                   checked
                                                                        </#if>
                                                                    </#list>
                                                                </#if>
                                                                   required> ${role.name}
                                                        </#if>
                                                    </#list>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="box-footer">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <div class="col-sm-offset-5 col-sm-7">
                                            <button type="button" id="submitBtn" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                                            <button type="button" id="closeIframeBtn" class="btn btn-danger"><i class="fa fa-close"></i> 关闭</button>
                                            <button type="reset" id="resetBtn" class="btn btn-default"><i class="fa fa-refresh"></i> 重置</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 参数 -->
                        <input type="hidden" name="id" id="id" value="${user.id}">
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>

</@body>
<@footer>
<script type="text/javascript">
    var validateForm;
    $(function () {
        //表单验证
        validateForm = $("#krtForm").validate({
            rules: {
                username: {
                    remote: {
                        url: "${basePath}/sys/user/checkUsername",
                        type: "post",
                        dataType: "json",
                        async: false,
                        data: {
                            id: function () {
                                return $("#id").val();
                            },
                            username: function () {
                                return $("#username").val();
                            }
                        }
                    }
                }
            },
            messages: {
                username: {remote: "用户名已存在"}
            }
        });

        //提交表单
        $("#submitBtn").click(function () {
            krt.ajax({
                type: "POST",
                url: "${basePath}/sys/user/update",
                data: $('#krtForm').serialize(),// 要提交的表单
                validateForm:validateForm,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        top.krt.tab.closeThisTabs(function (contentWindow) {
                            contentWindow.datatable.ajax.reload(null, false);
                        })
                    }
                }
            });
        })


        //内制机构的弹出框
        $("#organName,#organTreeBtn").click(function () {
            var url = "${basePath}/biz/qltOrganDepartment/qltOrganDepartmentTree";
            krt.layer.open({
                type: 2,
                area: ['300px', '450px'],
                title: "选择机构",
                maxmin: true, //开启最大化最小化按钮
                content: "${basePath}/common/treeSelect?url=" + encodeURI(url) + "&sId=code&sValue=" + $("#organCode").val() + "&expandAll=true",
                btn: ['确定', '取消', '关闭'],
                yes: function (index, layero) {
                    var tree = layero.find("iframe")[0].contentWindow.tree;
                    var nodes = tree.getSelectedNodes();
                    if (nodes == null) {
                        krt.layer.msg("请选择机构");
                    } else {
                        $("#organName").val(nodes[0].name);
                        $("#organCode").val(nodes[0].code); //因内制机构表的code是唯一的（qlt_organ_department）
                        krt.layer.close(index);
                    }
                },
                btn2: function () {
                    $("#organName").val("");
                    $("#organCode").val("");
                },
                cancel: function (index) {
                    krt.layer.close(index);
                }
            });
        });

        //机构
        $("#organName1,#organTreeBtn1").click(function () {
            var url = "${basePath}/sys/organ/organTree";
            krt.layer.open({
                type: 2,
                area: ['300px', '450px'],
                title: "选择机构",
                maxmin: true, //开启最大化最小化按钮
                content: "${basePath}/common/treeSelect?url=" + encodeURI(url) + "&sId=code&sValue=" + $("#organCode").val()+"&expandAll=true",
                btn: ['确定', '取消', '关闭'],
                yes: function (index, layero) {
                    var tree = layero.find("iframe")[0].contentWindow.tree;
                    var nodes = tree.getSelectedNodes();
                    if (nodes == null) {
                        krt.layer.msg("请选择机构");
                    } else {
                        $("#organName").val(nodes[0].name);
                        $("#organCode").val(nodes[0].code);
                        krt.layer.close(index);
                    }
                },
                btn2: function () {
                    $("#organName").val("");
                    $("#organCode").val("");
                },
                cancel: function (index) {
                    krt.layer.close(index);
                }
            });
        });
    });
</script>
</@footer>
