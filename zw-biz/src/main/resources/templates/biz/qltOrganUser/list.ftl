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
                <label for="name" class="control-label ">姓名:</label>
    <input type="text" id="name" name="name" placeholder="姓名" class="form-control">
            </div>
            <div class="form-group">
                <label for="phone" class="control-label ">手机号码:</label>
    <input type="text" id="phone" name="phone" placeholder="手机号码" class="form-control">
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
                    <@shiro.hasPermission name="qlt_organ_user:qltOrganUser:insert">
                        <button title="添加" type="button" id="insertBtn" data-placement="left" data-toggle="tooltip" class="btn btn-success btn-sm">
                            <i class="fa fa-plus"></i> 添加
                        </button>
                    </@shiro.hasPermission>

                </div>
                <table id="datatable" class="table table-striped table-bordered table-hover"></table>
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
            ajax: {
                url: "${basePath}/biz/qltOrganUser/list",
                type: "post",
                data: function (d) {
                d.name = $("#name").val();
                d.phone = $("#phone").val();
                d.orderName = krt.util.camel2Underline(d.columns[d.order[0].column].data);
                d.orderType = d.order[0].dir;
                }
            },
            columns: [
         {title: 'id', data: "id",visible:false},
        {title: '<input type="checkbox" id="checkAll" class="icheck">',
            data: "id", class:"td-center", width:"40",orderable: false,
            render: function (data) {
                return '<input type="checkbox" class="icheck check" value="' + data + '">';
            }
        },
                {title: "序号","data" : null,
                    "render" : function(data, type, full, meta){
                        return meta.row + 1 + meta.settings._iDisplayStart;
                    }
                },
                {title: "账号",data: "username"},
                {title: "姓名",data: "name"},
                {title: "科室或岗位",data: "depName"},
                {title: "手机号码",data: "phone"},
                {title: "出生",data: "birthday"},

                {
                    title: "操作", data: "id", orderable: false,
                    "render": function (data, type, row) {
                        return ' <@shiro.hasPermission name="qlt_organ_user:qltOrganUser:update">'
                            + '<button class="btn btn-xs btn-warning updateBtn" rid="' + row.id + '">'
                            + '<i class="fa fa-edit fa-btn"></i>修改'
                            + '</button>'
                            + '</@shiro.hasPermission>'
                            + '<@shiro.hasPermission name="qlt_organ_user:qltOrganUser:delete">'
                            + '<button class="btn btn-xs btn-danger deleteBtn" rid="' + row.id + '">'
                            + '<i class="fa fa-trash fa-btn"></i>删除'
                            + '</button>'
                            + '</@shiro.hasPermission>';
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
            top.krt.tab.openTab( "新增内设机构人员表", "${basePath}/biz/qltOrganUser/insert");
        });

        //修改
        $(document).on("click", ".updateBtn", function () {
            var id = $(this).attr("rid");
            top.krt.tab.openTab( "修改内设机构人员表", "${basePath}/biz/qltOrganUser/update?id=" + id);
        });

        //删除
        $(document).on("click", ".deleteBtn", function () {
            var id = $(this).attr("rid");
            krt.layer.confirm("你确定删除吗？", function () {
                krt.ajax({
                    type: "POST",
                    url: "${basePath}/biz/qltOrganUser/delete",
                    data: {"id": id},
                    success: function (rb) {
                        krt.layer.msg(rb.msg);
                        if (rb.code == 200) {
                            refreshTable(datatable);
                        }
                    }
                });
            });
        });



    });
</script>
</@footer>
