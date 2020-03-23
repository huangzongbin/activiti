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
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            <span class="form-required">*</span>名称：
                                        </label>
                                        <div class="col-sm-10">
                                            <input type="text" id="modelName" name="modelName" value="${model.modelName!}" class="form-control">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-6">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">
                                            <span class="form-required">*</span>业务：
                                        </label>
                                        <div class="col-sm-8">
                                            <div class="input-group">
                                                <input type="hidden" name="extBusinessId" id="extBusinessId" readonly value="${model.extBusinessId!}" class="form-control">
                                                <input type="text" name="extBusinessName" id="extBusinessName" readonly value="${business.name!}" class="form-control">
                                                <div class="input-group-btn">
                                                    <button class="btn btn-default" id="extBusinessIdBtn" type="button">
                                                        <i class="fa fa-search"></i>
                                                    </button>
                                                </div>
                                            </div>
                                            <script type="text/javascript">
                                                $("#extBusinessName,#extBusinessIdBtn").click(function () {
                                                    var url = "${basePath}/act/business/businessTree";
                                                    krt.layer.open({
                                                        type: 2,
                                                        area: ['310px', '450px'],
                                                        title: "选择业务流程",
                                                        maxmin: true, //开启最大化最小化按钮
                                                        content: "${basePath}/common/treeSelect?expandAll=true&url=" + encodeURI(url) + "&sValue=" + $("#extBusinessId").val(),
                                                        btn: ['确定', '取消', '关闭'],
                                                        yes: function (index, layero) {
                                                            var tree = layero.find("iframe")[0].contentWindow.tree;
                                                            var nodes = tree.getSelectedNodes();
                                                            if (nodes == null || nodes == '') {
                                                                krt.layer.msg("请选择业务流程");
                                                            } else if (nodes[0].type != 2) {
                                                                krt.layer.msg("业务类型错误");
                                                            } else {
                                                                $("#extBusinessId").val(nodes[0].id);
                                                                $("#extBusinessName").val(nodes[0].name);
                                                                krt.layer.close(index);
                                                            }
                                                        },
                                                        btn2: function () {
                                                            $("#extBusinessId").val("");
                                                            $("#extBusinessName").val("");
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
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            描述：
                                        </label>
                                        <div class="col-sm-10">
                                            <textarea rows="4" name="description" class="form-control" rangelength="1,50">${model.description!}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 隐藏域 -->
                            <input type="hidden" name="id" value="${model.id}">
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
                url: "${basePath}/act/model/update",
                data: $('#krtForm').serialize(),
                validateForm: validateForm,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code === 200) {
                        var index = krt.layer.getFrameIndex(); //获取窗口索引
                        krt.table.reloadTable();
                        krt.layer.close(index);
                    }
                }
            });
        }
    </script>
</@footer>

