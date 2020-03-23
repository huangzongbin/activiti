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
                    url: "${basePath}/act/deal/myToDoList",
                    type: "post",
                    data: function (d) {
                        d.code = $("#code").val();
                    }
                },
                columns: [
                    {
                        title: '序号', data: "busId", class: "td-center", width: "40", orderable: false,
                        render: function (data, type, row, hang) {
                            return (datatable.context[0]._iDisplayStart + (hang.row + 1));
                        }
                    },
                    {title: "业务名称", data: "busName"},
                    {title: "任务编号", data: "code"},
                    {title: "任务名称", data: "taskName"},
                    {title: "流程发起人", data: "startUserName"},
                    {title: "任务创建时间", data: "createTime"},
                    {
                        title: "等待处理人", data: "advanceName",
                        render: function (data, type, row) {
                            if (row.dealName !== '') {
                                return row.dealName;
                            } else {
                                return data;
                            }
                        }
                    },
                    {
                        title: "操作", data: "busId",
                        render: function (data, type, row) {
                            //预处理人
                            var advanceId = "," + row.advanceId + ",";
                            //办理人
                            var dealId = "," + row.dealId + ",";
                            //当前用户
                            var userId = ",${sessionUser.id},";
                            console.log(advanceId.indexOf(userId) >= 0 && row.dealId === "");
                            if (advanceId.indexOf(userId) >= 0 && row.dealId === "") {
                                //预处理人有多个 并且当前用户在
                                return ' <@shiro.hasPermission name="act:deal:claim">'
                                    + '<button class="btn btn-xs bg-purple claimBtn" taskId="' + row.taskId + '">'
                                    + '<i class="fa fa-sign-in fa-btn"></i>签收'
                                    + '</button>'
                                    + '</@shiro.hasPermission>';
                            } else if (advanceId.indexOf(userId) >= 0 && dealId === userId) {
                                var button = "";
                                if(row.advanceId.indexOf(",")>0){
                                    button = ' <@shiro.hasPermission name="act:deal:unClaim">'
                                        + '<button class="btn btn-xs bg-purple unClaimBtn" taskId="' + row.taskId + '">'
                                        + '<i class="fa fa-sign-out fa-btn"></i>释放'
                                        + '</button>'
                                        + '</@shiro.hasPermission>';
                                }
                                return   button
                                    + ' <button class="btn btn-xs btn-primary updateBtn" onclick="doTask(\'' + row.actKey + '\',\'' + row.busId + '\',\'' + row.instanceId + '\',\'' + row.taskId + '\',\'' + row.defId + '\')">'
                                    + '<i class="fa fa-edit fa-btn"></i>办理'
                                    + '</button>';
                            }else{
                                return ' <button class="btn btn-xs btn-primary updateBtn" onclick="doTask(\'' + row.actKey + '\',\'' + row.busId + '\',\'' + row.instanceId + '\',\'' + row.taskId + '\',\'' + row.defId + '\')">'
                                    + '<i class="fa fa-edit fa-btn"></i>办理'
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
        });

        //签收
        $(document).on("click", ".claimBtn", function () {
            var taskId = $(this).attr("taskId");
            krt.ajax({
                method: "GET",
                url: "${basePath}/act/deal/claim?taskId=" + taskId,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        krt.table.reloadTable();
                    }
                }
            });
        });

        //签收
        $(document).on("click", ".unClaimBtn", function () {
            var taskId = $(this).attr("taskId");
            krt.ajax({
                method: "GET",
                url: "${basePath}/act/deal/unClaim?taskId=" + taskId,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        krt.table.reloadTable();
                    }
                }
            });
        });

        /**
         * 办理任务
         */
        function doTask(actKey, busId, instanceId, taskId, defid, nodeId) {
            var url = "${basePath}/act/deal/taskInfo?flag=2&actKey=" + actKey + "&busId=" + busId + "&instanceId=" + instanceId + "&taskId=" + taskId + "&defId=" + defid;
            top.krt.tab.openTab("办理任务", url)
        }
    </script>
</@footer>
