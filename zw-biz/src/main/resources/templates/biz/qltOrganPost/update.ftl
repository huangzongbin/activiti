<#include "/common/layoutForm.ftl">
<@header></@header>
<@body class="body-bg-default">
<div class="wrapper">
    <section class="content">
        <div class="row">
            <div class="col-md-12">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">修改内设机构的岗位表</h3>
                    </div>
                    <form role="form" class="form-horizontal krt-form" id="krtForm">
                        <div class="box-body">
        <div class="row">
            <input type="hidden" id="organId" name="organId" value="${qltOrganPost.organId!}" class="form-control">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         岗位名称：
                    </label>
                    <div class="col-sm-8">
                     <input type="text" id="postName" name="postName" value="${qltOrganPost.postName!}" class="form-control">
                    </div>
                </div>
            </div>
           </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         备注：
                    </label>
                    <div class="col-sm-8">
                        <input type="text" id="mark" name="mark" value="${qltOrganPost.mark!}" class="form-control">
                    </div>
                </div>
            </div>
    <!-- 隐藏域 -->
                        </div>
                        <div class="box-footer">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                    <div class=" col-sm-offset-2 col-sm-10">
                                        <button type="button" id="submitBtn" class="btn btn-primary"><i class="fa fa-check"></i> 提交</button>
                                        <button type="button" id="closeIframeBtn" class="btn btn-danger"><i class="fa fa-close"></i> 关闭</button>
                                        <button type="reset" id="resetBtn" class="btn btn-default"><i class="fa fa-refresh"></i> 重置</button>
                                    </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</div>
</@body>
<@footer>
<script type="text/javascript">
    var validateForm;
    $(function () {
        //验证表单
        validateForm = $("#krtForm").validate({

        });

        //提交表单
        $("#submitBtn").click(function () {
            krt.ajax({
                type: "POST",
                url: "${basePath}/biz/qltOrganPost/update",
                data: $('#krtForm').serialize(),
                validateForm:validateForm,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        top.krt.tab.closeThisTabs(function (contentWindow) {
                            contentWindow.datatable.ajax.reload(null, false);
                        })
                    }
                }
            });
        });
    });
</script>
</@footer>

