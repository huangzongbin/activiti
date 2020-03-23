<#include "/common/layoutList.ftl">
<@header></@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <div class="box-search">
                <div class="row row-search">
                    <div class="col-xs-12">
                        <form class="form-inline" action="">
                            <div class="form-group">
                                <label for="modelKey" class="control-label">流程Key：</label>
                                <input type="text" class="form-control" placeholder="流程Key" id="modelKey">
                            </div>
                            <div class="form-group">
                                <label for="modelName" class="control-label">流程名称：</label>
                                <input type="text" class="form-control" placeholder="流程名称" id="modelName">
                            </div>
                            <div class="form-group">
                                <div class="text-center">
                                    <button type="button" id="searchBtn" class="btn btn-primary btn-sm">
                                        <i class="fa fa-search fa-btn"></i>搜索
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <!-- 列表数据区 -->
            <div class="box">
                <div class="box-body">
                    <!-- 工具按钮区 -->
                    <div class="table-toolbar" id="table-toolbar">
                        <@shiro.hasPermission name="act:model:insert">
                            <button title="添加" type="button" id="insertBtn" data-placement="left" data-toggle="tooltip" class="btn btn-success btn-sm">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="act:model:delete">
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
                    url: "${basePath}/act/model/list",
                    type: "post",
                    data: function (d) {
                        d.orderName = krt.util.camel2Underline(d.columns[d.order[0].column].data);
                        d.orderType = d.order[0].dir;
                        d.modelKey = $("#modelKey").val();
                        d.modelName = $("#modelName").val();
                    }
                },
                columns: [
                    {title: 'id', data: "id", visible: false},
                    {
                        title: '序号',data: "id", class: "td-center", width: "40", orderable: false,
                        render: function (data, type, row, hang) {
                            return (datatable.context[0]._iDisplayStart + (hang.row + 1));
                        }
                    },
                    {title: "流程key", data: "modelKey"},
                    {title: "名称", data: "modelName"},
                    {title: "描述", data: "description"},
                    {title: "版本号", data: "modelVersion"},
                    {title: "发布状态", data: "status",
                        render: function (data) {
                            if (data === '1') {
                                return '<span class="badge bg-red">未发布</span>';
                            } else {
                                return '<span class="badge bg-light-blue">已发布</span>';
                            }
                        }
                    },
                    {
                        title: "操作", data: "id", orderable: false,
                        "render": function (data, type, row) {
                            var button;
                            if(row.status==='1'){
                                button = '<@shiro.hasPermission name="act:model:update">'
                                    + '<button class="btn btn-xs btn-warning updateBtn" rid="' + row.id + '">'
                                    + '<i class="fa fa-edit fa-btn"></i>修改'
                                    + '</button>'
                                    + '</@shiro.hasPermission>'+
                                    '<@shiro.hasPermission name="act:model:deploy">'
                                    + '<button class="btn btn-xs bg-maroon deployBtn" modelId="' + row.modelId + '">'
                                    + '<i class="fa fa-play fa-btn"></i>部署'
                                    + '</button>'
                                    + '</@shiro.hasPermission>';
                            }
                            if(row.status==='0'){
                                button = '<@shiro.hasPermission name="act:model:deploy">'
                                    + '<button class="btn btn-xs bg-maroon deployBtn" modelId="' + row.modelId + '">'
                                    + '<i class="fa fa-cloud-upload fa-btn"></i>升级版本'
                                    + '</button>'
                                    + '</@shiro.hasPermission>';
                            }
                            return '<@shiro.hasPermission name="act:model:designFlow">'
                                + '<button class="btn btn-xs bg-olive designFlowBtn" modelId="' + row.modelId + '">'
                                + '<i class="fa fa-pencil fa-btn"></i>设计流程图'
                                + '</button>'
                                + '</@shiro.hasPermission>' +button
                                +'<@shiro.hasPermission name="act:model:flowNodeSet">'
                                + '<button class="btn btn-xs btn-primary flowNodeSetBtn" modelId="' + row.modelId + '">'
                                + '<i class="fa fa-cog fa-btn"></i>节点设置'
                                + '</button>'
                                + '</@shiro.hasPermission>'
                                + '<@shiro.hasPermission name="act:model:delete">'
                                + '<button class="btn btn-xs btn-danger deleteBtn" rid="' + row.id + '">'
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

            //新增
            $("#insertBtn").click(function () {
                top.krt.layer.openDialog("新增流程模板", "${basePath}/act/model/insert", "400px", "430px");
            });

            //修改
            $(document).on("click", ".updateBtn", function () {
                var id = $(this).attr("rid");
                top.krt.layer.openDialog("修改流程模板", "${basePath}/act/model/update?id=" + id, "400px", "430px");
            });

            //设计流程图
            $(document).on("click", ".designFlowBtn", function () {
                var modelId = $(this).attr("modelId");
                top.krt.tab.openTab("设计流程图", "${basePath}/modeler.html?modelId=" + modelId);
            });

            //节点设置
            $(document).on("click", ".flowNodeSetBtn", function () {
                var modelId = $(this).attr("modelId");
                top.krt.tab.openTab("节点设置", "${basePath}/act/model/flowNodeSet?modelId=" + modelId);
            });

            //部署
            $(document).on("click", ".deployBtn", function () {
                var modelId = $(this).attr("modelId");
                krt.ajax({
                    type: "POST",
                    url: "${basePath}/act/model/deploy?modelId=" + modelId,
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code === 200) {
                            krt.table.reloadTable();
                        }
                    }
                });
            });

            //删除
            $(document).on("click", ".deleteBtn", function () {
                var id = $(this).attr("rid");
                krt.layer.confirm("你确定删除吗？", function () {
                    krt.ajax({
                        type: "POST",
                        url: "${basePath}/act/model/delete",
                        data: {"id": id},
                        success: function (rb) {
                            krt.layer.msg(rb.msg);
                            if (rb.code === 200) {
                                krt.table.reloadTable();
                            }
                        }
                    });
                });
            });
        });
    </script>
</@footer>
