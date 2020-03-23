<#include "/common/layoutList.ftl">
<@header></@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <!-- 列表数据区 -->
            <div class="box">
                <div class="box-body">
                    <!-- 工具按钮区 -->
                    <table id="defTable" class="table table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>选择</th>
                            <th>名称</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list defList as def>
                            <tr id="def_${def_index}">
                                <td style="display: none;">
                                    <input name="deploymentId" value="${def.deploymentId!}">
                                    <input name="defId" value="${def.defId!}">
                                </td>
                                <td><input type="radio" class="icheck" name="def" <#if def_index==0>checked</#if>></td>
                                <td>${def.name!}</td>
                                <td>${def.description!}</td>
                            </tr>
                        </#list>
                        </tbody>
                    </table>
                    <script type="text/javascript">
                        function flowInfo(defId, deployId, actKey, busId, modelId, nodeId, nodeType, nodeAction, title) {
                            this.defId = defId;//定义id
                            this.deployId = deployId;//部署id
                            this.actKey = actKey;//流程key
                            this.busId = busId;//业务id
                            this.modelId = modelId;//流程模型id
                            this.nodeId = nodeId;//节点id
                            this.nodeType = nodeType;//节点类型
                            this.nodeAction = nodeAction;//节点行为
                            this.title = title;//选人弹框标题
                        }

                        var flowInfo = new flowInfo();
                        //初始化
                        $(function () {
                            flowInfo.actKey = '${actKey}';
                            flowInfo.busId = '${busId}';
                            $("#defTable tbody tr")[0].click();
                        })

                        //选择流程点击事件
                        $("#defTable tbody tr").off("click").click(function () {
                            flowInfo.defId = $(this).find("input[name='defId']").val();
                            flowInfo.deployId = $(this).find("input[name='deploymentId']").val();
                            getStartFlowInfo(flowInfo.deployId);
                            $("#userTab tbody").html("");
                        });

                        //获取第一个节点信息(除开始节点)
                        function getStartFlowInfo(deployId) {
                            krt.ajax({
                                type: "GET",
                                url: "${basePath}/act/deal/getStartFlowInfo?deployId=" + deployId,
                                success: function (rb) {
                                    if (rb.code == 200) {
                                        flowInfo.modelId = rb.data.modelId;
                                        flowInfo.nodeId = rb.data.nodeId;
                                        flowInfo.nodeType = rb.data.nodeType;
                                        flowInfo.nodeAction = rb.data.nodeAction;
                                        if (flowInfo.nodeType === "2") {
                                            //审批节点
                                            if (flowInfo.nodeAction === "1") {
                                                flowInfo.title = "选择审批人";
                                                $("#selectUser").text(flowInfo.title);
                                            }
                                        }
                                        //结束节点
                                        if (flowInfo.nodeType === "5") {
                                            $("#selectUser").attr("style", "display:none");
                                        }
                                    } else {
                                        krt.layer.msg(rb.msg);
                                    }
                                }
                            });
                        }
                    </script>
                    <div class="row" style="margin: 10px 0px;">
                        <button class="btn btn-success btn-sm" type="button" onclick="userList()"><i class="fa fa-users"></i> 选择审批人</button>
                    </div>
                    <table id="userTab" class="table table-bordered table-hover" isHide="2">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>角色</th>
                            <th>组织</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                    <!-- 参数 -->
                    <input type="hidden" name="userIds" id="userIds">
                </div>
            </div>
        </section>
    </div>
</@body>
<@footer>
    <script type="text/javascript">

        //选择审批人
        function userList() {
            var def = $("#defTable input[name='def']:checked");
            if (def.size() < 1) {
                krt.layer.msg("请选择流程定义");
                return;
            }
            top.krt.layer.open({
                type: 2,
                title: ["选择审批人", true],
                area: ['900px', '700px'], //宽高
                content: "${basePath}/act/deal/userList?nodeId=" + flowInfo.nodeId + "&nodeAction=" + flowInfo.nodeAction,
                shadeClose: false,
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0];
                    var userTab = $('body').find("#userTab tbody");
                    var html = "";
                    userTab.html(html);
                    var userIds = iframeWin.contentWindow.$("#userIds").val();
                    var userNames = iframeWin.contentWindow.$("#userNames").val();
                    var roleNames = iframeWin.contentWindow.$("#roleNames").val();
                    var organNames = iframeWin.contentWindow.$("#organNames").val();
                    var userIdArr = userIds.split(",");
                    var userNameArr = userNames.split(",");
                    var roleNameArr = roleNames.split(",");
                    var organNameArr = organNames.split(",");
                    for (var i = 0; i < userIdArr.length; i++) {
                        var userId = userIdArr[i];
                        var userName = userNameArr[i];
                        var roleName = roleNameArr[i];
                        var organName = organNameArr[i];
                        var flag = true;
                        userTab.find("tr").each(function () {
                            var trid = $(this).attr("id");
                            if (trid === userId) {
                                flag = false;
                            }
                        });
                        if (flag) {
                            html += "<tr id='" + userId + "'>";
                            html += "<td>" + userName + "</td>";
                            html += "<td style='display: none'>";
                            html += "<input name='userId' value='" + userId + "'/>";
                            html += "</td>";
                            html += "<td>" + roleName + "</td>";
                            html += "<td>" + organName + "</td>";
                            html += '<td><button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 </button></td>';
                            html += "</tr>";
                        }
                    }
                    userTab.append(html);
                    $("#userIds").val(userIds);
                    layer.close(index);
                },
                cancel: function (layer_window) {
                    layer.close(layer_window);
                }
            });
        }

        //删除审批人
        function delUser(_this) {
            $(_this).parent().parent().remove();
        }

        //启动流程
        function doSubmit() {
            krt.layer.confirm("确认提交流程吗？", function () {
                var userIds = $("#userIds").val();
                if (userIds === '' && flowInfo.nodeType !== "5") {
                    krt.layer.msg("至少选择一个审批用户");
                    return false;
                }
                krt.ajax({
                    type: "POST",
                    url: "${basePath}/act/deal/startFlow",
                    data: {
                        nextUserIds: userIds,
                        defId: flowInfo.defId,
                        actKey: flowInfo.actKey,
                        busId: flowInfo.busId,
                        nextNodeId: flowInfo.nodeId
                    },
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code === 200) {
                            var index = krt.layer.getFrameIndex(); //获取窗口索引
                            krt.table.reloadTable();
                            krt.layer.close(index);
                        }
                    }
                });
            });
        }
    </script>
</@footer>
