<#include "/common/layoutList.ftl">
<@header></@header>
<@body>
    <div class="wrapper">
        <section class="content">
            <!-- 节点信息 -->
            <table id="nodeTable" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>节点名称</th>
                    <th>节点类型</th>
                    <th>节点行为</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody> </tbody>
            </table>
            <!-- 审批人员 -->
            <table id="userTab" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th>名称</th>
                    <th>角色</th>
                    <th>组织</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
        </section>
    </div>
</@body>
<@footer>
    <script type="text/javascript">

        //定义流程相关信息类
        function processInfo(busId, taskId, instanceId, changeFields, defId, vars, varValue, nodeType, remark, isSelectUser,callback) {
            this.busId = busId;//业务id
            this.taskId = taskId;//任务id
            this.instanceId = instanceId;//流程实例
            this.changeFields = changeFields;//可更改的字段
            this.defId = defId;//流程定义id
            this.vars = vars;//vars 流程变量数组
            this.varValue = varValue;//vars 流程变量数组
            this.nodeType = nodeType;//节点类型
            this.remark = remark;//审批意见
            this.isSelectUser = isSelectUser;//是否必需选择下级审批用户
            this.callback = callback;//回调
        }
        var processInfo = new processInfo();

        //初始化
        $(function () {
            $('#userTab tr').find('th:eq(5)').hide();
            processInfo.busId = '${flowBusiness.busId!}';
            processInfo.taskId = '${processTaskDTO.taskId!}';
            processInfo.instanceId = '${flowBusiness.instanceId!}';
            processInfo.changeField = '${nodeSet.changeField!}';
            processInfo.defId = '${flowBusiness.defId!}';
            processInfo.varName = '${processTaskDTO.varName!}';
            processInfo.varValue = '${processTaskDTO.varValue!}';
            processInfo.nodeType = '${nodeSet.nodeType!}';
            processInfo.callback = '${processTaskDTO.callback!}';
            getNextNodeInfo();
        });

        //获取流程下个流向信息
        function getNextNodeInfo() {
            krt.ajax({
                method: "POST",
                url: "${basePath}/act/deal/getNextActNodes",
                data: {
                    defId: processInfo.defId,
                    taskId: processInfo.taskId,
                    varName: processInfo.varName,
                    varValue: processInfo.varValue
                },
                success: function (rb) {
                    if (rb.code === 200) {
                        var nextActNodes = rb.data;
                        var html = '';
                        for (var i = 0; i < nextActNodes.length; i++) {
                            var node = nextActNodes[i];
                            if (node.nodeType === '5') {
                                node.nodeName = "结束";
                                processInfo.isSelectUser = false;
                                $("#userTab").remove();
                            }
                            html += '<tr id="node_' + node.nodeId + '" >' +
                                '<input name="nodeId" type="hidden" value="' + node.nodeId + '" ">' +
                                '<input name="nodeAction" type="hidden" value="' + node.nodeAction + '" ">' +
                                '<input name="nodeName" type="hidden" value="' + node.nodeName + '" ">' +
                                '<td>' + node.nodeName + '</td>' +
                                '<td>' + krt.util.getDic("act_node_type",node.nodeType) + '</td>' +
                                '<td>' + krt.util.getDic("act_node_action",node.nodeAction) + '</td>';
                            //节点为结束节点,不选择下级处理人
                            if (node.nodeType !== '5') {
                                html += '<td style="width:100px;"><button type="button"  onclick="selectUser(this);" class="btn btn-success btn-sm" ><i class="fa fa-users"></i> 选择审批人</button></td></tr>'
                            } else {
                                html += '<td></td></tr>'
                            }
                        }
                        $("#nodeTable tbody").html(html);
                    } else {
                        krt.layer.msg(rb.msg);
                    }
                }
            });
        }

        //选择下一级审批人弹框
        function selectUser(_this) {
            var nodeId = $(_this).parent().parent().children("input[name='nodeId']:hidden").val();
            var url = "${basePath}/act/deal/userList?nodeId=" + nodeId;
            //弹框层
            top.krt.layer.open({
                type: 2,
                title: ["选择审批人", true],
                area: ['900px', '700px'], //宽高
                content: url,
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
                            if (trid == userId) {
                                flag = false;
                            }
                        });
                        if (flag) {
                            html += "<tr id='" + userId + "'>";
                            html += "<td>" + userName + "</td>";
                            html += "<td style='display:none'>";
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

        //办理任务
        function doSubmit() {
            var url = "${basePath}/act/deal/doActTask";
            var userIds = [];
            $("#userTab input[name='userId']").each(function () {
                userIds.push($(this).val());
            });
            if ((typeof (userIds) == 'undefined' || userIds.length < 1) && processInfo.isSelectUser != false) {
                krt.layer.msg("至少选择一个下级审批用户");
                return false;
            }
            var params = {
                'busId': processInfo.busId,
                'taskId': processInfo.taskId,
                'instanceId': processInfo.instanceId,
                'defId': processInfo.defId,
                'varValue': processInfo.varValue,
                'varName': processInfo.varName,
                'nodeType': processInfo.nodeType,
                'nextUserIds': userIds.join(","),
                'callback':processInfo.callback
            };
            var fileArr = processInfo.changeField.split(",");
            for (var i = 0; i < fileArr.length; i++) {
                var fieldName = fileArr[i];
                if (fieldName === '') {
                    continue;
                }
                //父级搜索表单
                var fieldValue =  "";
                var rr = top.krt.tab.getContentWindow().$("#formIframe")[0].contentWindow.$("#krtForm input[name='" + fieldName + "'][type=checkbox],input[name='" + fieldName + "'][type=radio]");
                if (!rr.length) {
                    fieldValue = top.krt.tab.getContentWindow().$("#formIframe")[0].contentWindow.$("#" + fieldName + "").val();
                }else{
                    fieldValue = top.krt.tab.getContentWindow().$("#formIframe")[0].contentWindow.$("input[name='"+fieldName+"']:checked").val();
                }
                params[fieldName] = fieldValue
            }
            var remark = top.krt.tab.getContentWindow().$("#formIframe")[0].contentWindow.$("#remark").val();
            if(remark === ""){
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            params["remark"] = remark;
            krt.ajax({
                url:url,
                method:"POST",
                data:params,
                success:function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code === 200) {
                        var index = krt.layer.getFrameIndex(); //获取窗口索引
                        top.krt.tab.closeThisTabs(function (contentWindow) {
                            contentWindow.krt.table.reloadTable();
                        });
                        krt.layer.close(index);
                    }
                }
            });
        }
    </script>
</@footer>
