<#include "/common/layoutList.ftl">
<@header></@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <!-- 列表数据区 -->
            <div class="box">
                <div class="box-body">
                    <!-- 工具按钮区 -->
                    <div class="table-toolbar" id="table-toolbar">
                        <@shiro.hasPermission name="act:extActProcess:delete">
                            <button class="btn btn-sm btn-danger" id="deleteBatchBtn">
                                <i class="fa fa-trash fa-btn"></i>批量删除
                            </button>
                        </@shiro.hasPermission>
                    </div>
                    <table id="datatable" class="table table-bordered table-hover"></table>
                </div>
            </div>
        </section>
    </div>
</@body>
<@footer>
    <script type="text/javascript">
        var datatable;
        $(function () {
            //初始化datatable
            datatable = $('#datatable').DataTable({
                ajax: {
                    url: "${basePath}/act/extActProcess/list",
                    type: "post",
                    data: function (d) {
                        d.orderName = krt.util.camel2Underline(d.columns[d.order[0].column].data);
                        d.orderType = d.order[0].dir;
                    }
                },
                columns: [
                    {title: 'id', data: "id", visible: false},
                    {
                        title: '<input type="checkbox" id="checkAll" class="icheck">',
                        data: "id", class: "td-center", width: "40", orderable: false,
                        render: function (data) {
                            return '<input type="checkbox" class="icheck check" value="' + data + '">';
                        }
                    },
                    {title: "流程定义ID", data: "id"},
                    {title: "流程定义名称", data: "name"},
                    {title: "流程定义的KEY", data: "key"},
                    {title: "版本", data: "version"},
                    {
                        title: "流程XML", data: "resourceName",
                        render: function (data, type, row) {
                            return '<a href="javascript:void(0)" class="viewResourceBtn" deploymentId="' + row.deploymentId + '" resourceName="' + row.resourceName + '">查看</a>'
                        }
                    },
                    {
                        title: "流程图片", data: "diagramResourceName",
                        render: function (data, type, row) {
                            return '<a href="javascript:void(0)" class="viewImageBtn" deploymentId="' + row.deploymentId + '" diagramResourceName="' + row.diagramResourceName + '">查看</a>'
                        }
                    },
                    {
                        title: "状态", data: "status",
                        render: function (data, type, row) {
                            if (row.isSuspended === true) {
                                return "挂起";
                            } else {
                                return "正常";
                            }
                        }
                    },
                    {
                        title: "操作", data: "id", orderable: false,
                        render: function (data, type, row) {
                            var tip, state;
                            if (row.isSuspended === true) {
                                tip = "激活";
                                state = "active";
                            } else {
                                tip = "挂起";
                                state = "suspend";
                            }
                            return '<@shiro.hasPermission name="act:extActProcess:updateProcessDefinitionState">'
                                + '<button class="btn btn-xs btn-warning stateBtn" state="' + state + '" processDefinitionId="' + row.id + '">'
                                + '<i class="fa fa-edit fa-btn"></i>' + tip
                                + '</button>'
                                + '</@shiro.hasPermission>'
                                + '<@shiro.hasPermission name="act:extActProcess:delete">'
                                + '<button class="btn btn-xs btn-danger deleteBtn" deploymentId="' + row.deploymentId + '" diagramResourceName="' + row.diagramResourceName + '">'
                                + '<i class="fa fa-trash fa-btn"></i>删除'
                                + '</button>'
                                + '</@shiro.hasPermission>';
                        }
                    }
                ]
            });

            //搜索
            $("#searchBtn").on('click', function () {
                datatable.ajax.reload();
            });

            //查看流程图
            $(document).on("click", ".viewImageBtn", function () {
                var deploymentId = $(this).attr("deploymentId");
                var diagramResourceName = $(this).attr("diagramResourceName");
                top.krt.tab.openTab("查看流程图", "${basePath}/act/extActProcess/viewImage?deploymentId=" + deploymentId + "&imageName=" + diagramResourceName);
            });

            //查看流程文件
            $(document).on("click", ".viewResourceBtn", function () {
                var deploymentId = $(this).attr("deploymentId");
                var resourceName = $(this).attr("resourceName");
                top.krt.tab.openTab("查看流程文件", "${basePath}/act/extActProcess/viewResource?deploymentId=" + deploymentId + "&resourceName=" + resourceName);
            });

            //删除
            $(document).on("click", ".deleteBtn", function () {
                var id = $(this).attr("rid");
                krt.layer.confirm("你确定删除吗？", function () {
                    krt.ajax({
                        type: "POST",
                        url: "${basePath}/act/extActProcess/delete",
                        data: {"id": id},
                        success: function (rb) {
                            krt.layer.msg(rb.msg);
                            if (rb.code == 200) {
                                krt.table.reloadTable();
                            }
                        }
                    });
                });
            });

            //批量删除
            $("#deleteBatchBtn").click(function () {
                var ids = getIds();
                if (ids == "") {
                    krt.layer.error("请选择要删除的数据!");
                    return false;
                } else {
                    krt.layer.confirm("你确定删除吗？", function () {
                        krt.ajax({
                            type: "POST",
                            url: "${basePath}/act/extActProcess/deleteByIds",
                            data: {"ids": ids},
                            success: function (rb) {
                                krt.layer.msg(rb.msg);
                                if (rb.code == 200) {
                                    krt.table.reloadTable(ids);
                                }
                            }
                        });
                    });
                }
            });
        });
    </script>
</@footer>
