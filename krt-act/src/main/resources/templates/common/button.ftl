<#assign basePath=request.contextPath />
<form action="" id="actFieldForm">
    <div class="row">
        <div class="col-sm-12">
            <div class="form-group">
                <label for="pname" class="control-label col-sm-2">
                    <span class="form-required">*</span>审批意见：
                </label>
                <div class="col-sm-10">
                    <textarea id="remark" name="remark" class="form-control" rows="3"></textarea>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <div class="form-group" style="margin-bottom:0px;">
                <div style="text-align: center" id="buttons"></div>
            </div>
        </div>
    </div>
</form>
<script>

    //流程相关信息类
    function processInfo(busId, actKey, taskId, instanceId, changeFields, defId) {
        this.busId = busId;//业务id
        this.actKey = actKey;//流程key也是业务key
        this.taskId = taskId;//任务id
        this.instanceId = instanceId;//流程实例
        this.changeFields = changeFields;//可更改的字段
        this.defId = defId;//流程定义id
    }

    var processInfo = new processInfo();

    //初始化
    $(function () {
        //获取业务可更改的字段，和流程业务基本信息
        processInfo.busId = '${processTaskDTO.busId}';
        processInfo.taskId = '${processTaskDTO.taskId}';
        processInfo.instanceId = '${processTaskDTO.instanceId}';
        processInfo.defId = '${processTaskDTO.defId}';
        getButtons();
        changeFields();
    });

    //获取流程变量
    function getVars() {
        processInfo.varValue = [];
        processInfo.varName = [];
        var vars = processInfo.vars;
        if (vars != null) {
            for (var i = 0; i < vars.length; i++) {
                var val = "";
                var rr = $("#krtForm input[name='" + vars[i] + "'][type=checkbox],input[name='" + vars[i] + "'][type=radio]");
                if (!rr.length) {
                    val = $("#" + vars[i] + "").val();
                } else {
                    val = $("input[name='" + vars[i] + "']:checked").val();
                }
                processInfo.varName.push(vars[i]);
                processInfo.varValue.push(val);
            }
        }
    }

    //获取修改字段的值
    function getChangeFields(params) {
        var fileArr = processInfo.changeFields;
        for (var i = 0; i < fileArr.length; i++) {
            var fieldName = fileArr[i];
            if (fieldName === '') {
                continue;
            }
            var fieldValue = "";
            var rr = $("#krtForm input[name='" + fieldName + "'][type=checkbox],input[name='" + fieldName + "'][type=radio]");
            if (!rr.length) {
                fieldValue = $("#" + fieldName + "").val();
            } else {
                fieldValue = $("input[name='" + fieldName + "']:checked").val();
            }
            params[fieldName] = fieldValue;
        }
        return params;
    }

    //获取流程节点按钮
    function getButtons() {
        krt.ajax({
            method: "GET",
            url: "${basePath}/act/deal/getButtons",
            data: {
                nodeId: '${RequestParameters['nodeId']!}',
                instanceId: processInfo.instanceId
            },
            success: function (rb) {
                if (rb.code === 200) {
                    var data = rb.data.buttons;
                    var button = "";
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].buttonCode === "0" && !rb.data.isSuspended) {
                            button = button + '<button class="btn btn-primary" type="button" callback="' + data[i].callback + '" onclick="autoSubmit(this)"><i class="fa fa-check"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "1" && !rb.data.isSuspended) {
                            button = button + '<button class="btn btn-primary" type="button" callback="' + data[i].callback + '" onclick="clickSubmit(this)"><i class="fa fa-check"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "2" && !rb.data.isSuspended) {
                            button = button + '<button class="btn bg-navy" type="button" callback="' + data[i].callback + '" onclick="endProcess(this)"><i class="fa fa-power-off"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "3" && !rb.data.isSuspended) {
                            button = button + '<button class="btn bg-purple" type="button" callback="' + data[i].callback + '" onclick="backStartUser(this)"><i class="fa fa-reply-all"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "4" && !rb.data.isSuspended) {
                            button = button + '<button class="btn bg-olive" type="button" callback="' + data[i].callback + '" onclick="backPrevious(this)"><i class="fa fa-reply"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "5" && !rb.data.isSuspended) {
                            //挂起
                            button = button + '<button class="btn bg-orange" type="button" callback="' + data[i].callback + '" tip="' + data[i].buttonName + '" onclick="updatePdStatus(\'suspend\',this)"><i class="fa fa-hourglass-end"></i> ' + data[i].buttonName + '</button>';
                        }
                        if (data[i].buttonCode === "6" && rb.data.isSuspended) {
                            //激活
                            button = button + '<button class="btn bg-purple" type="button" callback="' + data[i].callback + '" tip="' + data[i].buttonName + '" onclick="updatePdStatus(\'active\',this)"><i class="fa fa-hourglass-start"></i> ' + data[i].buttonName + '</button>';
                        }
                    }
                    button = button + '<button class="btn btn-danger" type="button"  onclick="closeTab()"><i class="fa fa-close"></i> 关闭</button>';
                    $("#buttons").html(button);
                } else {
                    krt.layer.msg(rb.msg);
                }
            }
        });
    }

    //获取业务可更改的字段，和流程业务基本信息,和流程节点变量
    function changeFields() {
        krt.ajax({
            method: "POST",
            url: "${basePath}/act/deal/changeFields",
            data: {
                'busId': processInfo.busId,
                'taskId': processInfo.taskId,
                'instanceId': processInfo.instanceId,
                'defId': processInfo.defId
            },
            success: function (rb) {
                if (rb.code === 200) {
                    //可修改的属性
                    processInfo.changeFields = rb.data.changeFields;
                    //流程变量
                    processInfo.vars = rb.data.vars;
                    for (var i = 0; i < processInfo.changeFields.length; i++) {
                        var fieldName = processInfo.changeFields[i];
                        if (fieldName != null && fieldName !== '') {
                            $("#" + fieldName).attr("readOnly", false);
                        }
                    }
                } else {
                    krt.layer.msg(rb.msg);
                }
            }
        });
    }

    //办理(手动选择下一个办理人)
    function clickSubmit(_this) {
        //验证表单
        if (validateForm.form()) {
            var remark = $("#remark").val();
            if (remark === "") {
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            var callback = $(_this).attr("callback");
            var params = "busId=" + processInfo.busId + "&taskId=" + processInfo.taskId + "&defId=" + processInfo.defId + "&instanceId=" + processInfo.instanceId + "&callback=" + callback;
            //流程变量
            getVars();
            params += "&varName=" + processInfo.varName.join(",") + "&varValue=" + processInfo.varValue.join(",");
            krt.layer.openDialog("选择下一步审批人", "${basePath}/act/deal/nextActTask?" + params, "900px", "700px");
        }
    }

    //自动办理
    function autoSubmit(_this) {
        //验证表单
        if (validateForm.form()) {
            var remark = $("#remark").val();
            if (remark === "") {
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            //流程变量
            getVars();
            var params = {
                'busId': processInfo.busId,
                'taskId': processInfo.taskId,
                'instanceId': processInfo.instanceId,
                'defId': processInfo.defId,
                'varValue': processInfo.varValue.join(","),
                'varName': processInfo.varName.join(","),
                'remark': remark,
                'callback': $(_this).attr("callback")
            };
            //获取修改字段
            getChangeFields(params);
            krt.ajax({
                method: "POST",
                url: "${basePath}/act/deal/autoDoActTask",
                data: params,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code === 200) {
                        top.krt.tab.closeThisTabs(function (contentWindow) {
                            contentWindow.krt.table.reloadTable();
                        })
                    }
                }
            });
        }
    }

    //终止流程
    function endProcess(_this) {
        //验证表单
        if (validateForm.form()) {
            var remark = $("#remark").val();
            if (remark === "") {
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            krt.layer.confirm("确定终止流程吗？", function () {
                var params = {
                    'busId': processInfo.busId,
                    'taskId': processInfo.taskId,
                    'instanceId': processInfo.instanceId,
                    'defId': processInfo.defId,
                    'remark': remark,
                    'callback': $(_this).attr("callback")
                };
                getChangeFields(params);
                krt.ajax({
                    method: "POST",
                    url: "${basePath}/act/deal/endProcess",
                    data: params,
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code === 200) {
                            top.krt.tab.closeThisTabs(function (contentWindow) {
                                contentWindow.krt.table.reloadTable();
                            })
                        }
                    }
                });
            });
        }
    }

    //驳回到任务发起人，重新编辑之后再提交
    function backStartUser(_this) {
        //验证表单
        if (validateForm.form()) {
            var remark = $("#remark").val();
            if (remark === "") {
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            krt.layer.confirm("确定驳回到任务发起人吗？", function () {
                var params = {
                    'busId': processInfo.busId,
                    'taskId': processInfo.taskId,
                    'instanceId': processInfo.instanceId,
                    'defId': processInfo.defId,
                    'remark': remark,
                    'callback': $(_this).attr("callback")
                };
                getChangeFields(params);
                krt.ajax({
                    method: "POST",
                    url: "${basePath}/act/deal/backStartUser",
                    data: params,
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code === 200) {
                            top.krt.tab.closeThisTabs(function (contentWindow) {
                                contentWindow.krt.table.reloadTable();
                            })
                        }
                    }
                });
            });
        }
    }

    //退回到上一步
    function backPrevious(_this) {
        //验证表单
        if (validateForm.form()) {
            var remark = $("#remark").val();
            if (remark === "") {
                krt.layer.msg("请填写审批意见!");
                return false;
            }
            krt.layer.confirm("确定退回到上一步吗？", function () {
                var params = {
                    'busId': processInfo.busId,
                    'taskId': processInfo.taskId,
                    'instanceId': processInfo.instanceId,
                    'defId': processInfo.defId,
                    'remark': remark,
                    'callback': $(_this).attr("callback")
                };
                getChangeFields(params);
                krt.ajax({
                    method: "POST",
                    url: "${basePath}/act/deal/backPrevious",
                    data: params,
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code === 200) {
                            top.krt.tab.closeThisTabs(function (contentWindow) {
                                contentWindow.krt.table.reloadTable();
                            })
                        }
                    }
                });
            });
        }
    }

    //挂起/激活
    function updatePdStatus(status, _this) {
        var tips = $(_this).attr("tip");
        krt.layer.confirm("确定" + tips + "吗？", function () {
            krt.ajax({
                method: "POST",
                url: "${basePath}/act/deal/updatePdStatus",
                data: {
                    'pdStatus': status,
                    'busId': processInfo.busId,
                    'taskId': processInfo.taskId,
                    'instanceId': processInfo.instanceId,
                    'defId': processInfo.defId,
                    'callback': $(_this).attr("callback")
                },
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code === 200) {
                        window.location.href = window.location.href;
                    }
                }
            });
        });
    }

    //关闭tab
    function closeTab() {
        top.krt.tab.closeThisTabs(function (contentWindow) {

        })
    }
</script>

