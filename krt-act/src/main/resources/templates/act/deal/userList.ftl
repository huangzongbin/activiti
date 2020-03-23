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
                                <label for="userName" class="control-label">姓名：</label>
                                <input type="text" class="form-control" placeholder="姓名" id="userName">
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
    <!-- 参数 -->
    <input type="hidden" id="userIds" value="${RequestParameters['userIds']!}"/>
    <input type="hidden" id="userNames" value="${RequestParameters['userNames']!}"/>
    <input type="hidden" id="roleNames" value="${RequestParameters['roleNames']!}"/>
    <input type="hidden" id="organNames" value="${RequestParameters['organNames']!}"/>
    <input type="hidden" id="nodeId" value="${RequestParameters['nodeId']!}"/>
</@body>
<@footer>
    <script type="text/javascript">
        var datatable;
        $(function () {
            //初始化datatable
            datatable = $('#datatable').DataTable({
                ajax: {
                    url: "${basePath}/act/deal/userList",
                    type: "post",
                    data: function (d) {
                        d.orderName = krt.util.camel2Underline(d.columns[d.order[0].column].data);
                        d.orderType = d.order[0].dir;
                        d.nodeId = $("#nodeId").val();
                        d.userName = $("#userName").val();
                    }
                },
                columns: [
                    {title: 'id', data: "userId", visible: false},
                    {
                        title: '<input type="checkbox" id="checkAll" class="icheck">',
                        data: "userId", class: "td-center", width: "40", orderable: false,
                        render: function (data, type, row) {
                            return '<input type="checkbox" class="icheck cCheck" userId="'+row.userId+'" userName="'+row.userName+'" roleName="'+row.roleName+'" organName="'+row.organName+'">';
                        }
                    },
                    {title: "姓名", data: "userName"},
                    {title: "角色", data: "roleName"},
                    {title: "部门", data: "organName"}
                ],
                fnDrawCallback: function () {
                    ccCheck();
                }
            });
        });


        function ccCheck() {
            //初始化全选
            var checkAll = $("#checkAll");
            $(".checkAll").unbind();
            checkAll.iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            });
            checkAll.iCheck('uncheck');
            //初始化子集
            $(".cCheck").unbind();
            $(".cCheck").iCheck({
                checkboxClass: 'icheckbox_minimal-blue',
                radioClass: 'iradio_minimal-blue'
            });
            //绑定全选事件
            checkAll.on('ifChecked', function (event) {
                $(".cCheck").iCheck('check');
            }).on('ifUnchecked', function (event) {
                $(".cCheck").iCheck('uncheck');
            });
            //参数定义
            var userIds = new Array();
            var userNames = new Array();
            var roleNames = new Array();
            var organNames = new Array();
            //子集点击事件
            $('.cCheck').on('ifChecked', function (event) {
                var userIdsVal = $("#userIds").val();
                var userNamesVal = $("#userNames").val();
                var roleNamesVal = $("#roleNames").val();
                var organNamesVal = $("#organNames").val();
                if (userIdsVal !== '') {
                    userIds = userIdsVal.split(",");
                    userNames = userNamesVal.split(",");
                    roleNames = roleNamesVal.split(",");
                    organNames = organNamesVal.split(",");
                }
                userIds.push($(this).attr("userId"));
                userNames.push($(this).attr("userName"));
                roleNames.push($(this).attr("roleName"));
                organNames.push($(this).attr("organName"));
                $("#userIds").val(userIds.join(","));
                $("#userNames").val(userNames.join(","));
                $("#roleNames").val(roleNames.join(","));
                $("#organNames").val(organNames.join(","));
            });
            //子集取消选中事件
            $('.cCheck').on('ifUnchecked', function (event) {
                var userIdsVal = $("#userIds").val();
                var userNamesVal = $("#userNames").val();
                var roleNamesVal = $("#roleNames").val();
                var organNamesVal = $("#organNames").val();
                if (userIdsVal !== '') {
                    userIds = userIdsVal.split(",");
                    userNames = userNamesVal.split(",");
                    roleNames = roleNamesVal.split(",");
                    organNames = organNamesVal.split(",");
                }
                var index = userIds.indexOf($(this).attr("userId"));
                if (index > -1) {
                    userIds.splice(index, 1);
                    userNames.splice(index, 1);
                    roleNames.splice(index, 1);
                    organNames.splice(index, 1);
                    $("#userIds").val(userIds.join(","));
                    $("#userNames").val(userNames.join(","));
                    $("#roleNames").val(roleNames.join(","));
                    $("#organNames").val(organNames.join(","));
                }
            });
        }

        //搜索
        $("#searchBtn").on('click', function () {
            $("#userIds").val("");
            $("#userNames").val("");
            $("#roleNames").val("");
            $("#organNames").val("");
            datatable.ajax.reload();
        });
    </script>
</@footer>
