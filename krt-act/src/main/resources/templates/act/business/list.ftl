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
                        <div id="ztree" class="ztree"></div>
                        <!-- 参数 -->
                        <input type="hidden" id="pid" value="0">
                    </div>
                </div>
            </div>
            <div class="col-sm-9">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">业务流程</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body" id="boxbody">
                        <div class="table-toolbar" id="table-toolbar">
                            <@shiro.hasPermission name="act:business:insert">
                                <button title="添加" type="button" id="insertBtn" data-placement="left" data-toggle="tooltip" class="btn btn-success btn-sm">
                                    <i class="fa fa-plus"></i> 添加
                                </button>
                            </@shiro.hasPermission>
                            <button id="expandTableBtn" data-placement="left" class="btn bg-orange btn-sm">
                                <i class="fa fa-chevron-down"></i> 全部展开
                            </button>
                            <button id="collapseTableBtn" data-placement="left" class="btn bg-purple btn-sm" style="display:none;">
                                <i class="fa fa-chevron-up"></i> 全部收缩
                            </button>
                        </div>
                        <div class="table-responsive" id="table-body">
                            <table id="tree-table" class="table table-bordered table-hover"></table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
</@body>
<@footer>
<script type="text/javascript">

    /******************************** ztree事件 ********************************/

        //ztree设置
    var setting = {
            view: {
                selectedMulti: false,
                dblClickExpand: false
            },
            data: {
                simpleData: {
                    enable: true,
                    pIdKey: "pid",
                }
            },
            callback: {
                onClick: zTreeOnClick
            }
        };

    //ztree点击事件
    function zTreeOnClick(event, treeId, treeNode) {
        var pid = treeNode.id;
        $("#pid").val(pid);
        initTable(pid);
    }

    //加载树数据
    function initTree() {
        krt.ajax({
            type: "POST",
            url: "${basePath}/act/business/businessTree",
            async: false,
            success: function (rb) {
                if (rb.code == 200) {
                    $.fn.zTree.init($("#ztree"), setting, rb.data);
                } else {
                    krt.layer.msg(rb.msg);
                }
            }
        });
    }

    //初始化tree
    initTree();
    var tree = $.fn.zTree.getZTreeObj("ztree");

    //刷新树
    $("#refTreeBtn").click(function () {
        initTree();
    });

    //展开tree
    $("#expandTreeBtn").click(function () {
        tree.expandAll(true);
        $(this).hide();
        $('#collapseTreeBtn').show();
    });

    //折叠tree
    $("#collapseTreeBtn").click(function () {
        tree.expandAll(false);
        $(this).hide();
        $('#expandTreeBtn').show();
    });

    /******************************** treetable事件 ********************************/

    //初始化treeTable
    function initTreeTable() {
        $("#tree-table").treetable({
            expandable: true,
            onNodeExpand: nodeExpand,
            onNodeCollapse: nodeCollapse
        });
    }

    //刷新table
    function reloadTable() {
        var pid = $("#pid").val();
        initTable(pid);
    }

    //展开事件
    function nodeExpand() {
        getNodeViaAjax(this.id);
    }

    //收缩事件
    function nodeCollapse() {

    }

    //加载父表
    function initTable(pid) {
        $("#pid").val(pid);
        krt.ajax({
            type: "POST",
            url: "${basePath}/act/business/list",
            data: {"pid": pid},
            success: function (rb) {
                if (rb.code == 200) {
                    var treeList = rb.data;
                    var tbody = '';
                    for (var i = 0; i < treeList.length; i++) {
                        var row = treeList[i];
                        var hasChild = "";
                        if (row.hasChild == 'true') {
                            hasChild = 'data-tt-branch="true"';
                        }
                        var tr = '<tr data-tt-id="' + row.id + '" ' + hasChild + '>'
                            + "<td>" + krt.util.isNull(row.name) + "</td>"
                            + "<td>" + krt.util.isNull(row.actKey) + "</td>"
                            + "<td>" + krt.util.getDic('act_business_type', row.type) + "</td>"
                            + "<td>" + krt.util.isNull(row.classUrl) + "</td>"
                            + "<td>" + krt.util.isNull(row.sort) + "</td>"
                            + "<td>" + krt.util.isNull(row.remark) + "</td>"
                            + "<td>"
                            + '<@shiro.hasPermission name="act:business:update"><button class="btn btn-xs btn-warning updateBtn" rid="' + row.id + '" pid="' + row.pid + '"><i class="fa fa-edit fa-btn"></i>修改</button></@shiro.hasPermission>'
                            + '<@shiro.hasPermission name="act:business:delete"><button class="btn btn-xs btn-danger deleteBtn" rid="' + row.id + '"><i class="fa fa-trash fa-btn"></i>删除</button></@shiro.hasPermission>'
                            + '<@shiro.hasPermission name="act:business:insert"><button class="btn btn-xs btn-success addBtn" rid="' + row.id + '"><i class="fa fa-plus fa-btn"></i>添加子集</button></@shiro.hasPermission>'
                            + "</td>"
                            + "</tr>";
                        tbody = tbody + tr;
                    }
                    $("#tree-table").remove();
                    var html = '<table id="tree-table" class="table table-bordered table-hover table-krt"><thead><tr><th>业务名字</th><th>流程key </th><th>类型</th><th>类路径</th><th>排序</th><th>备注</th><th>操作</th></tr></thead><tbody id="tbody">' + tbody + '</tbody></table>';
                    $("#table-body").append(html);
                    initTreeTable();
                } else {
                    krt.layer.msg(rb.msg);
                }
            }
        });
    }

    //加载子节点
    function getNodeViaAjax(pid) {
        krt.ajax({
            type: 'POST',
            url: "${basePath}/act/business/list?pid=" + pid,
            dataType: 'json',
            success: function (rb) {
                if (rb.code == 200) {
                    var childNodes = rb.data;
                    if (childNodes) {
                        var parentNode = $("#tree-table").treetable("node", pid);
                        for (var i = 0; i < childNodes.length; i++) {
                            var node = childNodes[i];
                            var nodeToAdd = $("#tree-table").treetable("node", node.id);
                            if (!nodeToAdd) {
                                var hasChild = "";
                                if (node.hasChild == 'true') {
                                    hasChild = 'data-tt-branch="true"';
                                }
                                var row = '<tr data-tt-id="' + node.id + '" ' + hasChild + ' data-tt-parent-id="' + pid + '">'
                                    + "<td>" + krt.util.isNull(node.name) + "</td>"
                                    + "<td>" + krt.util.isNull(node.actKey) + "</td>"
                                    + "<td>" + krt.util.getDic('act_business_type', node.type) + "</td>"
                                    + "<td>" + krt.util.isNull(node.classUrl) + "</td>"
                                    + "<td>" + krt.util.isNull(node.sort) + "</td>"
                                    + "<td>" + krt.util.isNull(node.remark) + "</td>"
                                    + "<td>"
                                    + '<@shiro.hasPermission name="act:business:update"><button class="btn btn-xs btn-warning updateBtn" rid="' + node.id + '" pid="' + node.pid + '"><i class="fa fa-edit fa-btn"></i>修改</button></@shiro.hasPermission>'
                                    + '<@shiro.hasPermission name="act:business:delete"><button class="btn btn-xs btn-danger deleteBtn" rid="' + node.id + '"><i class="fa fa-trash fa-btn"></i>删除</button></@shiro.hasPermission>'
                                    + '<@shiro.hasPermission name="act:business:insert"><button class="btn btn-xs btn-success addBtn" rid="' + node.id + '"><i class="fa fa-plus fa-btn"></i>添加子集</button></@shiro.hasPermission>'
                                    + "</td>"
                                    + "</tr>";
                                $("#tree-table").treetable("loadBranch", parentNode, row);
                            }
                        }
                    }
                } else {
                    krt.layer.msg(rb.msg);
                }
            }
        });
    }

    //加载默认
    initTable(0);

    //收缩
    $("#collapseTableBtn").click(function () {
        $('#tree-table').treetable('collapseAll');
        $(this).hide();
        $('#expandTableBtn').show();
    });

    //展开
    $("#expandTableBtn").click(function () {
        $('#tree-table').treetable('expandAll');
        $(this).hide();
        $('#collapseTableBtn').show();
    })

    //新增
    $("#insertBtn").click(function () {
        var pid = $("#pid").val();
        top.krt.layer.openDialog("新增业务流程", "${basePath}/act/business/insert?pid=" + pid, "850px", "480px");
    });

    //修改
    $(document).on("click", ".updateBtn", function () {
        var id = $(this).attr("rid");
        top.krt.layer.openDialog("修改业务流程","${basePath}/act/business/update?id=" + id,"850px","480px");
    });

    //添加下一级
    $(document).on("click", ".addBtn", function () {
        var id = $(this).attr("rid");
        top.krt.layer.openDialog("新增业务流程","${basePath}/act/business/insert?pid=" + id,"850px","480px");
    });

    //删除
    $(document).on("click", ".deleteBtn", function () {
        var id = $(this).attr("rid");
        krt.layer.confirm("你确定删除此记录及下级记录吗？", function () {
            krt.ajax({
                type: "POST",
                url: "${basePath}/act/business/delete",
                data: {"id": id},
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        var pid = $("#pid").val();
                        initTable(pid);
                    }
                }
            });
        });
    });
</script>
</@footer>

