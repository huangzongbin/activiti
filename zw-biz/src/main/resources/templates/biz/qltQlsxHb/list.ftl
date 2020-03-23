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
                <label for="agentCode" class="control-label ">目录名称</label>
                <input type="text" id="folderName" name="folderName" placeholder="目录名称" class="form-control">
            </div>
            <div class="form-group">
                <label for="agentName" class="control-label ">承办机构名称:</label>
                <input type="text" id="agentName" name="agentName" placeholder="承办机构名称" class="form-control">
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
                url: "${basePath}/biz/qltQlsxHb/list",
                type: "post",
                data: function (d) {
                d.folderName = $("#folderName").val();
                d.agentName = $("#agentName").val();
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
                {title: "事项名称",data: "name"},
                {title: "项目编码",data: "itemCode"},
                {title: "承办机构名称",data: "agentName"},
                {title: "事项实施单位名称",data: "orgName"},
                {title: "事项区划名称",data: "regionName"},
                {
                    title: "操作", data: "rowguid", orderable: false,
                    "render": function (data, type, row) {
                            return '<button class="btn btn-xs btn-info setBtn" rid="' + row.rowguid + '">'
                                    + '<i class="fa fa-eye fa-btn"></i>配置事项属性'
                                    + '</button>';

                    }
                }
            ]
        });

        //搜索
        $("#searchBtn").on('click', function () {
            datatable.ajax.reload();
        });

        //配置
        $(document).on("click", ".setBtn", function () {
            var rowguid = $(this).attr("rid");
            //top.krt.layer.openDialogView("事项管理-->事项配置", "${basePath}/biz/qltQlsxHb/seting?rowguid="+rowguid,"1300px", "850px")
            top.krt.tab.openTab( "事项配置", "${basePath}/biz/qltQlsxHb/seting?rowguid="+rowguid);
        });



    });
</script>
</@footer>
