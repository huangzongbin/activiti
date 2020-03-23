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
                                <label for="type" class="control-label">类型：</label>
                                <select id="type" class="form-control select2">
                                    <option value="1">用户</option>
                                    <option value="2">角色</option>
                                    <option value="3">组织</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="name" class="control-label">名称：</label>
                                <input type="text" class="form-control" placeholder="名称" id="name">
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
    <input type="hidden" id="sName" value="${RequestParameters['sName']!}"/>
    <input type="hidden" id="sId" value="${RequestParameters['sId']!}"/>
</@body>
<@footer>
    <script type="text/javascript">
        var datatable;
        $(function () {
            //初始化datatable
            datatable = $('#datatable').DataTable({
                ajax: {
                    url: "${basePath}/act/model/userList",
                    type: "post",
                    data: function (d) {
                        d.orderName = krt.util.camel2Underline(d.columns[d.order[0].column].data);
                        d.orderType = d.order[0].dir;
                        d.type = $("#type").val();
                        d.name = $("#name").val();
                    }
                },
                columns: [
                    {title: 'id', data: "id", visible: false},
                    {
                        title: '<input type="checkbox" id="checkAll" class="icheck">',
                        data: "id", class: "td-center", width: "40", orderable: false,
                        render: function (data, type, row) {
                            return '<input type="checkbox" class="icheck cCheck" rid="' + data + '" rname="' + row.name + '">';
                        }
                    },
                    {
                        title: "类型", data: "id",
                        render: function () {
                            return $("#type option:selected").text();
                        }
                    },
                    {title: "名称", data: "name"}
                ],
                fnDrawCallback: function () {
                    cCheck();
                }
            });
        });
        //搜索
        $("#searchBtn").on('click', function () {
            $("#sName").val("");
            $("#sId").val("");
            datatable.ajax.reload();
        });
    </script>
</@footer>
