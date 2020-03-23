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
                                <label for="code" class="control-label">业务编号：</label>
                                <input type="text" class="form-control" placeholder="业务编号" id="code">
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
                ordering: false,//排序
                ajax: {
                    url: "${basePath}/act/deal/myDoneList",
                    type: "post",
                    data: function (d) {
                        d.code = $("#code").val();
                    }
                },
                columns: [
                    {
                        title: '序号',
                        data: "id", class: "td-center", width: "40", orderable: false,
                        render: function (data, type, row, hang) {
                            return (datatable.context[0]._iDisplayStart + (hang.row + 1));
                        }
                    },
                    {title: "业务名称", data: "busName"},
                    {title: "任务编号", data: "code"},
                    {title: "任务名称", data: "taskName"},
                    {title: "处理意见", data: "remark"},
                    {title: "流程发起人", data: "startUserName"},
                    {title: "预处理人", data: "advanceName"},
                    {title: "办理人", data: "dealName"},
                    {
                        title: "流程状态", data: "status",
                        render: function (data) {
                            return krt.util.getDic("act_process_status",data);
                        }
                    },
                    {title: "任务创建时间", data: "insertTime"},
                    {title: "任务办理时间", data: "dealTime"},
                    {
                        title: "操作", data: "id",
                        "render": function (data, type, row) {
                            return ' <button class="btn btn-xs btn-info" onclick="doTask(\'' + row.actKey + '\',\'' + row.busId + '\',\'' + row.instanceId + '\',\'' + row.taskId + '\',\'' + row.defId + '\')">'
                                + '<i class="fa fa-eye fa-btn"></i>审批记录'
                                + '</button>';
                        }
                    }
                ]
            });

            //搜索
            $("#searchBtn").on('click', function () {
                datatable.ajax.reload();
            });
        });

        //审批信息
        function doTask(actKey, busId, instanceId, taskId, defid) {
            var url = "${basePath}/act/deal/taskInfo?flag=1&actKey=" + actKey + "&busId=" + busId + "&instanceId=" + instanceId + "&taskId=" + taskId + "&defId=" + defid;
            top.krt.tab.openTab("审批记录", url)
        }
    </script>
</@footer>
