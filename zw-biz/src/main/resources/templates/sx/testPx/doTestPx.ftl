<#include "/common/layoutForm.ftl">
<@header>
<style>
    html, body {
        height: auto;
    }
</style>
</@header>
<@body >
<div class="wrapper">
    <section class="content">
        <div class="row">
            <div class="col-md-12">
                <form role="form" class="form-horizontal krt-form" id="krtForm">
                    <div class="box-body">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        id：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="id" name="id" value="${testPx.id!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        报销人：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="userId" name="userId" value="${testPx.userId!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        报销人：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="userName" name="userName" value="${testPx.userName!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        报销标题：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="title" name="title" value="${testPx.title!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        报销金额：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="day" name="day" value="${testPx.day!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        报销原因：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="reason" name="reason" value="${testPx.reason!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        业务流程状态  1=草稿 2=审批中 3=结束：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="status" name="status" value="${testPx.status!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">
                                        业务状态：
                                    </label>
                                    <div class="col-sm-8">
                                        <input type="text" id="bizStatus" name="bizStatus" value="${testPx.bizStatus!}" class="form-control">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 隐藏域 -->
                        <!-- 工作流按钮 -->
                        <#if RequestParameters['flag']=='2'>
                            <#include "/common/button.ftl">
                        </#if>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>
</@body>
<@footer>
<script type="text/javascript" src="${basePath}/plugin/iframeResizer/iframeResizer.contentWindow.min.js"></script>
<script type="text/javascript">
    var validateForm;
    $(function () {
        //验证表单
        validateForm = $("#krtForm").validate({});
    });
</script>
</@footer>

