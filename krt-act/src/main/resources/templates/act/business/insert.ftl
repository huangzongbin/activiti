<#include "/common/layoutForm.ftl">
<@header></@header>
<@body >
    <div class="wrapper">
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <form role="form" class="form-horizontal krt-form" id="krtForm">
                        <div class="box-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            父节点：
                                        </label>
                                        <div class="col-sm-8">
                                            <!-- 参数 -->
                                            <input type="hidden" name="pid" id="pid" value="${(pBusiness.id)!}" class="form-control">
                                            <div class="input-group">
                                                <input type="text" name="pname" readonly id="pname" value="${(pBusiness.name)!}" class="form-control">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-default" id="pTreeBtn" type="button">
                                                        <i class="fa fa-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                            <script type="text/javascript">
                                                $("#pname,#pTreeBtn").click(function () {
                                                    var url = "${basePath}/act/business/businessTreeData";
                                                    krt.layer.open({
                                                        type: 2,
                                                        area: ['310px', '450px'],
                                                        title: "选择上级业务",
                                                        maxmin: true, //开启最大化最小化按钮
                                                        content: "${basePath}/common/treeSelect?url=" + encodeURI(url) + "&sValue=" + $("#pid").val(),
                                                        btn: ['确定', '取消', '关闭'],
                                                        yes: function (index, layero) {
                                                            var tree = layero.find("iframe")[0].contentWindow.tree;
                                                            var nodes = tree.getSelectedNodes();
                                                            if (nodes == null || nodes == '') {
                                                                krt.layer.msg("请选择父节点");
                                                            } else {
                                                                $("#pname").val(nodes[0].name);
                                                                $("#pid").val(nodes[0].id);
                                                                krt.layer.close(index);
                                                            }
                                                        },
                                                        btn2: function () {
                                                            $("#pname").val("");
                                                            $("#pid").val("");
                                                        },
                                                        cancel: function (index) {
                                                            krt.layer.close(index);
                                                        }
                                                    });
                                                });
                                            </script>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            <span class="form-required">*</span>业务名称：
                                        </label>
                                        <div class="col-sm-8">
                                            <input type="text" id="name" name="name" class="form-control" rangelength="1,30" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            流程key：
                                        </label>
                                        <div class="col-sm-8">
                                            <input type="text" id="actKey" name="actKey" class="form-control">
                                            <span class="help-inline">业务类必填且不可重复</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            <span class="form-required">*</span>类型：
                                        </label>
                                        <div class="col-sm-8">
                                            <select class="form-control select2" style="width: 100%" id="type" name="type" required>
                                                <option value="">==请选择==</option>
                                                <@dic type="act_business_type" ; dicList>
                                                    <#list dicList as item>
                                                        <option value="${item.code}">${item.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            类路径：
                                        </label>
                                        <div class="col-sm-8">
                                            <input type="text" id="classUrl" name="classUrl" class="form-control">
                                            <span class="help-inline">业务类:com.krt.ClassName<br/>回调:com.krt.ClassName.MethodName</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            <span class="form-required">*</span>排序：
                                        </label>
                                        <div class="col-sm-8">
                                            <input type="text" id="sort" name="sort" class="form-control" digits="true" required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            备注：
                                        </label>
                                        <div class="col-sm-10">
                                            <textarea rows="4" name="remark" class="form-control" rangelength="1,50"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 隐藏域 -->
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</@body>
<@footer>
    <script type="text/javascript">
        var validateForm;
        $(function () {
            //验证表单
            validateForm = $("#krtForm").validate({});
        });

        //提交
        function doSubmit() {
            krt.ajax({
                type: "POST",
                url: "${basePath}/act/business/insert",
                data: $('#krtForm').serialize(),
                validateForm: validateForm,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code === 200) {
                        var index = krt.layer.getFrameIndex(); //获取窗口索引
                        var contentWindow = top.krt.tab.getContentWindow();
                        contentWindow.reloadTable();
                        contentWindow.initTree();
                        krt.layer.close(index);
                    }
                }
            });
        }
    </script>
</@footer>

