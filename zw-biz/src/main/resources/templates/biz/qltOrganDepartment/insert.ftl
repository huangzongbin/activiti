<#include "/common/layoutForm.ftl">
<@header></@header>
<@body class="body-bg-default">
<div class="wrapper">
    <section class="content">
        <div class="row">
            <div class="col-md-12">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">新增内设机构设置</h3>
                    </div>
                    <form role="form" class="form-horizontal krt-form" id="krtForm">
                   <div class="box-body">

          <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         <span class="form-required">*</span>上级名称：
                    </label>
                    <div class="col-sm-8">
                      <input type="text" id="name" name="name" class="form-control" value="${pQltOrganDepartment.name!}" disabled>
                    </div>
                </div>
            </div>
         </div>

           <div class="row">
                 <div class="col-sm-6">
                       <div class="form-group">
                            <label for="pname" class="control-label col-sm-4">
                                       <span class="form-required">*</span>内设机构名称或岗位：
                             </label>
                             <div class="col-sm-8">
                                 <input type="text" id="name" name="name" class="form-control" required>

                              </div>
                       </div>
                  </div>
           </div>

           <div class="row">
                <div class="col-sm-6">
                     <div class="form-group">
                         <label for="pname" class="control-label col-sm-4">
                             类型：
                          </label>
                          <div class="col-sm-8">
                               <input type="radio" class="icheck" name="type" value="1"  checked> 科室
                               <input type="radio" class="icheck" name="type" value="2"> 科室下的岗位
                           </div>
                      </div>
                 </div>
            </div>

        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         岗位数：
                    </label>
                    <div class="col-sm-8">
                      <input type="text" id="postNum" name="postNum" class="form-control">
                    </div>
                </div>
            </div>
         </div>

          <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         人员数：
                    </label>
                    <div class="col-sm-8">
                       <input type="text" id="userNum" name="userNum" class="form-control">
                    </div>
                </div>
            </div>
          </div>

                <!-- 隐藏域 -->
                <input type="hidden" id="pid" name="pid" value="${pQltOrganDepartment.id!}">
                <input type="hidden" id="organId" name="organId" value="${pQltOrganDepartment.organId!}">
               <input type="hidden" id="code" name="code" value="${lastCode!}">

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
                url: "${basePath}/biz/qltOrganDepartment/insert",
                data: $('#krtForm').serialize(),
                validateForm:validateForm,
                success: function (rb) {
                    krt.layer.msg(rb.msg);
                    if (rb.code == 200) {
                        top.krt.tab.closeThisTabs(function (contentWindow) {
                            contentWindow.reloadTable();
                        })
                    }
                }
            });
        });
    });
</script>
</@footer>

