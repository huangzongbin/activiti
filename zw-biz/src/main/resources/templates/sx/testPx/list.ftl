<#include "/common/layoutList.ftl">
<@header></@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <!-- 搜索条件区 -->
            <div class="box-search">
                <div class="row row-search">
                    <div class="col-xs-12">
                        <form class="form-inline" action="">
                            <div class="form-group">
                                <label for="userName" class="control-label ">报销人:</label>
                                <input type="text" id="userName" name="userName" placeholder="报销人" class="form-control">
                            </div>
                            <div class="form-group">
                                <label for="title" class="control-label ">报销标题:</label>
                                <input type="text" id="title" name="title" placeholder="报销标题" class="form-control">
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
                        <@shiro.hasPermission name="test_px:testPx:insert">
                            <button title="添加" type="button" id="insertBtn" data-placement="left" data-toggle="tooltip"
                                    class="btn btn-success btn-sm">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </@shiro.hasPermission>
                        <@shiro.hasPermission name="test_px:testPx:delete">
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
    <script type="text/javascript" src="${basePath}/act/js/act.js"></script>
    <script type="text/javascript">
        var datatable;
        $(function () {
            //初始化datatable
            datatable = $('#datatable').DataTable({
                ajax: {
                    url: "${basePath}/sx/testPx/list",
                    type: "post",
                    data: function (d) {
                        d.userName = $("#userName").val();
                        d.title = $("#title").val();
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
                    {title: "id", data: "id"},
                    {title: "报销人", data: "userId"},
                    {title: "报销人", data: "userName"},
                    {title: "报销标题", data: "title"},
                    {title: "报销金额", data: "day"},
                    {title: "报销原因", data: "reason"},
                    {title: "业务流程状态  1=草稿 2=审批中 3=结束", data: "status"},
                    {title: "业务状态", data: "bizStatus"},
                    {
                        title: "操作", data: "id", orderable: false,
                        "render": function (data, type, row) {
                            if (row.status == '1') {
                                return ' <button class="btn btn-xs btn-primary submitBtn" onclick="flowSumbit(\'test_px\',\'' + row.id + '\')">'
                                    + '<i class="fa fa-check fa-btn"></i>提交'
                                    + '</button>'
                                    + '<button class="btn btn-xs btn-primary submitBtn" onclick="autoSumbit(\'test_px\',\'' + row.id + '\')">'
                                    + '<i class="fa fa-check fa-btn"></i>自动提交'
                                    + '</button>'
                                    + ' <@shiro.hasPermission name="test_px:testPx:update">'
                                    + '<button class="btn btn-xs btn-warning updateBtn" rid="' + row.id + '">'
                                    + '<i class="fa fa-edit fa-btn"></i>修改'
                                    + '</button>'
                                    + '</@shiro.hasPermission>'
                                    + '<@shiro.hasPermission name="test_px:testPx:delete">'
                                    + '<button class="btn btn-xs btn-danger deleteBtn" rid="' + row.id + '">'
                                    + '<i class="fa fa-trash fa-btn"></i>删除'
                                    + '</button>'
                                    + '</@shiro.hasPermission>';
                            }
                            if ((row.status == '2' || row.status == '3' || row.status == '4') && row.actResult != '2') {
                                return '<button class="btn btn-xs btn-info" onclick="doTaskTab(\'test_px\',\'' + row.id + '\',\'' + row.instanceId + '\')">'
                                    + '<i class="fa fa-eye fa-btn"></i>审批记录'
                                    + '</button>';
                            }

                            if ((row.status == '2' || row.status == '3') && row.actResult == '2') {
                                return ' <button class="btn btn-xs btn-primary submitBtn" onclick="flowSumbit(\'test_px\',\'' + row.id + '\')">'
                                    + '<i class="fa fa-check fa-btn"></i>提交'
                                    + '</button>'
                                    + '<button class="btn btn-xs btn-primary submitBtn" onclick="autoSumbit(\'test_px\',\'' + row.id + '\')">'
                                    + '<i class="fa fa-check fa-btn"></i>自动提交'
                                    + '</button>'
                                    + ' <@shiro.hasPermission name="test_px:testPx:update">'
                                    + '<button class="btn btn-xs btn-warning updateBtn" rid="' + row.id + '">'
                                    + '<i class="fa fa-edit fa-btn"></i>修改'
                                    + '</button>'
                                    + '</@shiro.hasPermission>'
                                    + '<button class="btn btn-xs btn-info" onclick="doTaskTab(\'leave\',\'' + row.id + '\',\'' + row.instanceId + '\')">'
                                    + '<i class="fa fa-eye fa-btn"></i>审批记录'
                                    + '</button>';
                            }
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
                top.krt.layer.openDialog("新增报销流程测试", "${basePath}/sx/testPx/insert", "1000px", "500px");
            });

            //修改
            $(document).on("click", ".updateBtn", function () {
                var id = $(this).attr("rid");
                top.krt.layer.openDialog("修改报销流程测试", "${basePath}/sx/testPx/update?id=" + id, "1000px", "500px");
            });

            //删除
            $(document).on("click", ".deleteBtn", function () {
                var id = $(this).attr("rid");
                krt.layer.confirm("你确定删除吗？", function () {
                    krt.ajax({
                        type: "POST",
                        url: "${basePath}/sx/testPx/delete",
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
                            url: "${basePath}/sx/testPx/deleteByIds",
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
