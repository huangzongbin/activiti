<#include "/common/layoutList.ftl">
<@header></@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <div class="row row-space15">
                <div class="col-sm-3">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">业务流程树</h3>
                            <div class="box-tools">
                                <button class="btn btn-box-tool" id="expandTreeBtn" title="展开">
                                    <i class="fa  fa-chevron-down"></i>
                                </button>
                                <button class="btn btn-box-tool" id="collapseTreeBtn" title="折叠" style="display:none;">
                                    <i class="fa  fa-chevron-up"></i>
                                </button>
                                <button class="btn btn-box-tool" id="refTreeBtn" title="刷新">
                                    <i class="fa fa-refresh"></i>
                                </button>
                            </div>
                        </div>
                        <div class="box-body">
                            <div id="modelTree" class="ztree"></div>
                            <!-- 参数 -->
                            <input type="hidden" id="pid" value="0">
                        </div>
                    </div>
                </div>
                <div class="col-sm-9">
                    <div class="box">
                        <div class="box-header with-border">
                            <h3 class="box-title">节点设置</h3>
                        </div>
                        <form role="form" class="form-horizontal krt-form krt-width-form" id="krtForm">
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="code" class="control-label col-sm-4">
                                                名称：
                                            </label>
                                            <div class="col-sm-8">
                                                <input id="name" name="name" type="text" class="form-control" readonly/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="code" class="control-label col-sm-4">
                                                类型：
                                            </label>
                                            <div class="col-sm-8">
                                                <select name="nodeType" id="nodeType" class="form-control" readonly>
                                                    <option value="1">开始节点</option>
                                                    <option value="2">审批节点</option>
                                                    <option value="3">分支</option>
                                                    <option value="4">连线</option>
                                                    <option value="5">结束节点</option>
                                                    <option value="6">并发</option>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-6" isHide="2">
                                        <div class="form-group">
                                            <label for="code" class="control-label col-sm-4">
                                                行为：
                                            </label>
                                            <div class="col-sm-8">
                                                <select name="nodeAction" id="nodeAction" class="form-control">
                                                    <@dic type="act_node_action";actNodeActionList>
                                                        <#list actNodeActionList as actNodeAction>
                                                            <option value="${actNodeAction.code}" }>${actNodeAction.name}</option>
                                                        </#list>
                                                    </@dic>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="form-group" isHide="2">
                                            <label for="code" class="control-label col-sm-4">
                                                预警时间：
                                            </label>
                                            <div class="col-sm-8">
                                                <div class="input-group">
                                                    <input type="text" name="warnLine" id="warnLine" class="form-control">
                                                    <span class="input-group-addon input-group-addon-right-radius">小时</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="form-group" isHide="25">
                                            <label for="code" class="control-label col-sm-2">
                                                表单url：
                                            </label>
                                            <div class="col-sm-10">
                                                <input id="url" name="url" type="text" class="form-control"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="form-group" isHide="2">
                                            <label class="control-label col-sm-2">
                                                可修改信息：
                                            </label>
                                            <div class="col-sm-10">
                                                <#list writes as field>
                                                    <input type="checkbox" class="icheck" name="changeField" value="${field.value}"> ${field.name}
                                                </#list>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- 审批设置 -->
                                <div class="row" style="margin: 0" isHide="2">
                                    <div style="margin-bottom: 10px;">
                                        <button class="btn btn-success btn-sm" type="button" onclick="userWindow()"><i class="fa fa-users"></i> 审批范围</button>
                                    </div>
                                    <table id="userTab" class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th style="width: 33.3%">类型</th>
                                            <th style="width: 33.3%">名称</th>
                                            <th style="width: 33.3%">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                                <!-- 审批按钮 -->
                                <div class="row" style="margin: 0" isHide="2">
                                    <div style="margin-bottom: 10px;">
                                        <button class="btn btn-success btn-sm" type="button" onclick="addBtn()"><i class="fa fa-list-ul"></i> 审批按钮</button>
                                    </div>
                                    <table id="buttonTab" class="table table-bordered table-hover">
                                        <thead>
                                        <tr>
                                            <th style="width: 25%">类型</th>
                                            <th style="width: 25%">名称</th>
                                            <th style="width: 25%">回调</th>
                                            <th style="width: 25%">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                </div>
                            </div>
                            <!-- 条件 -->
                            <div class="row" ishide="4" id="actRules">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">条件1：</label>
                                        <div class="col-sm-3">
                                            <select name="judgeList[0].fieldName" class="form-control">
                                                <option value="">==请选择==</option>
                                                <#if judges?? && (judges?size>0)>
                                                    <#list judges as judge>
                                                        <option value="${judge.value}" }>${judge.name}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="col-sm-2">
                                            <select name="judgeList[0].rule" class="form-control">
                                                <option value="">==请选择==</option>
                                                <@dic type="act_judg";judgeList>
                                                    <#list judgeList as judge>
                                                        <option value="${judge.code}" }>${judge.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                        <div class="col-sm-3">
                                            <input name="judgeList[0].fieldVal" class="form-control" placeholder="">
                                        </div>
                                        <div class="col-sm-2">
                                            <select name="judgeList[1].elOperator" class="form-control">
                                                <option value="">==请选择==</option>
                                                <@dic type="act_el_operator";elList>
                                                    <#list elList as el>
                                                        <option value="${el.code}" }>${el.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="control-label col-sm-2">条件2：</label>
                                        <div class="col-sm-3">
                                            <select name="judgeList[1].fieldName" class="form-control">
                                                <option value="">==请选择==</option>
                                                <#if judges?? && (judges?size>0)>
                                                    <#list judges as judge>
                                                        <option value="${judge.value}" }>${judge.name}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="col-sm-2">
                                            <select name="judgeList[1].rule" class="form-control">
                                                <option value="">==请选择==</option>
                                                <@dic type="act_judg";judgeList>
                                                    <#list judgeList as judge>
                                                        <option value="${judge.code}" }>${judge.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                        <div class="col-sm-3">
                                            <input name="judgeList[1].fieldVal" class="form-control" placeholder="">
                                        </div>
                                        <div class="col-sm-2">
                                            <select name="judgeList[2].elOperator" class="form-control">
                                                <option value="">==请选择==</option>
                                                <@dic type="act_el_operator";elList>
                                                    <#list elList as el>
                                                        <option value="${el.code}" }>${el.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label class="control-label col-md-2">条件3：</label>
                                        <div class="col-md-3">
                                            <select name="judgeList[2].fieldName" class="form-control">
                                                <option value="">==请选择==</option>
                                                <#if judges?? && (judges?size>0)>
                                                    <#list judges as judge>
                                                        <option value="${judge.value}" }>${judge.name}</option>
                                                    </#list>
                                                </#if>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <select name="judgeList[2].rule" class="form-control">
                                                <option value="">==请选择==</option>
                                                <@dic type="act_judg";judgeList>
                                                    <#list judgeList as judge>
                                                        <option value="${judge.code}" }>${judge.name}</option>
                                                    </#list>
                                                </@dic>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <input name="judgeList[2].fieldVal" class="form-control">
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 按钮 -->
                            <div class="row" style="padding: 25px 0px;">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <div style="text-align: center">
                                            <button class="btn btn-primary btn-sm" type="button" onclick="submitForm()">保 存</button>
                                            <button class="btn btn-default btn-sm" type="reset">重置</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 参数 -->
                            <div style="display: none">
                                <input name="nodeId"/>
                                <input name="id" id="id"/>
                                <input name="modelId"/>
                            </div>
                        </form>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col-xs-10 -->
            </div>
            <!-- /.row -->
        </section>
        <!-- /.content -->
    </div>
    <!-- 按钮模板 -->
    <tpl id="btnTpl" style="display: none">
        <trr>
            <tdd>
                <select class="form-control buttonCode" name="buttonCodes" id="select_index" style="max-width: 200px;" idd="index" onchange="changeText(this)">
                    <@dic type = "act_button";dicList>
                        <option value="">== 请选择==</option>
                        <#list dicList as button>
                            <option value="${button.code}">${button.name}</option>
                        </#list>
                    </@dic>
                </select>
            </tdd>
            <tdd>
                <input name="buttonNames" id="buttonName_index" class="form-control" style="max-width: 200px;"/>
            </tdd>
            <tdd>
                <select name="callBacks" id="callBack_index" class="form-control" style="max-width: 200px;">
                    <option value="">==请选择==</option>
                    <#if statusCallback?? && (statusCallback?size > 0)>
                        <optgroup label="状态回调">
                            <#list statusCallback as callback>
                                <option value="${callback.code}">${callback.name}</option>
                            </#list>
                        </optgroup>
                    </#if>
                    <#if callbacks?? && (callbacks?size > 0)>
                        <optgroup label="自定义回调">
                            <#list callbacks as callback>
                                <option value="${callback.classUrl}">${callback.name}</option>
                            </#list>
                        </optgroup>
                    </#if>
                </select>
            </tdd>
            <tdd>
                <button type="button" onclick="delBtn(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删除</button>
            </tdd>
        </trr>
    </tpl>
</@body>
<@footer>
    <script type="text/javascript" src="${basePath}/act/js/flowNodeSet.js"></script>
    <script>
        var setting = {
            view: {dblClickExpand: false},
            data: {
                key: {name: "treeName"},
                simpleData: {
                    enable: true,
                    idKey: "treeId",
                    pIdKey: "treePid"
                }
            },
            callback: {onClick: zTreeOnClick}
        };

        function setCheck() {
            var ztreeobj = $.fn.zTree.init($("#modelTree"), setting, ${flows});
            ztreeobj.expandAll(true);
        }

        //初始化ztree
        setCheck();

        //节点单击事件
        function zTreeOnClick(event, treeId, treeNode) {
            //清空表单信息
            $("#krtForm")[0].reset();
            $("#krtForm input[name='name']").val(treeNode.treeName);
            $("#krtForm input[name='nodeId']").val(treeNode.treeId);
            $("#krtForm input[name='modelId']").val(treeNode.modelId);
            var selectChange = $("#krtForm select[name='nodeType']").val(treeNode.type)[0];
            simulateClick(selectChange);
            krt.ajax({
                type: "GET",
                url: "${basePath}/act/model/flowSetInfo",
                data: {
                    nodeId: treeNode.treeId,
                    type: treeNode.type
                },
                success: function (rb) {
                    var result = rb.data;
                    var nodeSet = result.nodeSet;
                    if (nodeSet) {
                        formload("krtForm", nodeSet);
                    }
                    //审批范围
                    var userLists = result.userList;
                    if (userLists) {
                        var html = "";
                        for (var i = 0; i < userLists.length; i++) {
                            html += "<tr id='" + userLists[i].id + "'>";
                            html += "<td>" + krt.util.getDic('act_user_type', userLists[i].userType) + "</td>";
                            html += "<td style='display: none'>";
                            html += "<input name='userTypes' value='" + userLists[i].userType + "'/>";
                            html += "<input name='userIds' value='" + userLists[i].userId + "'/>";
                            html += "</td>";
                            html += "<td>" + userLists[i].userName + "</td>";
                            html += '<td><button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 </button></td>';
                            html += "</tr>";
                        }
                        $("#userTab tbody").html(html);
                    }
                    //审批按钮
                    var buttonList = result.buttonList;
                    if (buttonList) {
                        var html = "";
                        $("#buttonTab tbody").html(html);
                        for (var i = 0; i < buttonList.length; i++) {
                            var tr = $("#btnTpl").html();
                            tr = tr.replace(/tdd/g, "td").replace(/trr/g, "tr").replace(/index/g, i);
                            html = html + tr;
                        }
                        $("#buttonTab tbody").html(html);
                        for (var i = 0; i < buttonList.length; i++) {
                            $("#select_" + i).val(buttonList[i].buttonCode);
                            $("#buttonName_" + i).val(buttonList[i].buttonName);
                            $("#callBack_" + i).val(buttonList[i].callback);
                        }
                    }
                    //节点条件
                    var fields = result.fields;
                    if (fields) {
                        for (var i = 0; i < fields.length; i++) {
                            simulateClick($("#krtForm select[name='judgeList[" + i + "].fieldName']").val(fields[i].fieldName)[0]);
                            $("#krtForm select[name='judgeList[" + i + "].rule']").val(fields[i].rule);
                            $("#krtForm input[name='judgeList[" + i + "].fieldVal']").val(fields[i].fieldVal);
                            simulateClick($("#krtForm select[name='judgeList[" + i + "].elOperator']").val(fields[i].elOperator)[0]);
                        }
                    }
                }
            });
        };


        $(function () {
            //页面加载都隐藏
            $("[isHide]").hide();
            $("#krtForm select[name='nodeType']").on("change", function () {
                $("[isHide]").hide();
                $("[isHide*='" + $(this).val() + "'").show();
            });
        });

        //选择审批范围
        function userWindow() {
            var url = "${basePath}/act/model/userList";
            //弹框层
            krt.layer.open({
                type: 2,
                title: ["审批者选择范围", true],
                area: ['900px', '700px'], //宽高
                content: url,
                shadeClose: false,
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0];
                    var userTab = $('body').find("#userTab tbody");
                    var html = "";
                    userTab.html(html);
                    var ids = iframeWin.contentWindow.$("#sId").val();
                    var names = iframeWin.contentWindow.$("#sName").val();
                    var idArr = ids.split(",");
                    var nameArr = names.split(",");
                    for (var i = 0; i < idArr.length; i++) {
                        var id = idArr[i];
                        var name = nameArr[i];
                        var flag = true;
                        userTab.find("tr").each(function () {
                            var trid = $(this).attr("id");
                            if (trid === id) {
                                flag = false;
                            }
                        });
                        var userType = iframeWin.contentWindow.$("#type option:selected").text();
                        var type = iframeWin.contentWindow.$("#type option:selected").val();
                        if (flag) {
                            html += "<tr id='" + id + "'>";
                            html += "<td>" + userType + "</td>";
                            html += "<td style='display: none'>";
                            html += "<input name='userTypes' value='" + type + "'/>";
                            html += "<input name='userIds' value='" + id + "'/>";
                            html += "</td>";
                            html += "<td>" + name + "</td>";
                            html += '<td><button type="button" onclick="delUser(this)" class="btn btn-xs btn-white btn-danger"><i class="fa fa-trash-o"></i> 删 除 </button></td>';
                            html += "</tr>";
                        }
                    }
                    userTab.append(html);
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

        //选择操作按钮
        function addBtn() {
            var table = $("#buttonTab");
            var tr = $("#btnTpl").html();
            var index = (new Date()).valueOf();
            tr = tr.replace(/tdd/g, "td").replace(/trr/g, "tr").replace(/index/g, index);
            table.find("tbody").append(tr);
        }

        //改变按钮事件
        function changeText(_this) {
            var idd = $(_this).attr("idd");
            var val = $(_this).find("option:selected").val();
            var btns = $('.buttonCode');
            var flag = true;
            btns.each(function (i, btn) {
                var iddd = btn.getAttribute("idd");
                var vall = $("#select_" + iddd).find("option:selected").val();
                var text = $("#select_" + iddd).find("option:selected").text();
                if (idd != iddd && val === vall && text !== "终止") {
                    krt.layer.msg("按钮重复");
                    $("#select_" + idd).val("");
                    flag = false;
                }
            });
            if (flag) {
                $("#buttonName_" + idd).val($(_this).find("option:selected").text());
            }
        }

        //删除按钮
        function delBtn(_this) {
            $(_this).parent().parent().remove();
        }

        //保存节点信息
        function submitForm() {
            krt.ajax({
                type: "POST",
                url: "${basePath}/act/model/saveNode",
                data: $('#krtForm').serialize(),// 要提交的表单
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        $("#id").val(rb.data);
                    }
                }
            });
        }

    </script>
</@footer>