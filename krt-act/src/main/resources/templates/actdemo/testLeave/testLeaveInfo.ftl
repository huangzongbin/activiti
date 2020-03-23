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
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            <span class="form-required">*</span>请假标题：
                                        </label>
                                        <div class="col-sm-10">
                                            <input type="text" id="title" name="title" value="${testLeave.title!}" class="form-control" readonlyrequired>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            <span class="form-required">*</span>请假天数：
                                        </label>
                                        <div class="col-sm-10">
                                            <input type="text" id="day" name="day" value="${testLeave.day!}" class="form-control" readonlyrequired required>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-2">
                                            原因：
                                        </label>
                                        <div class="col-sm-10">
                                            <textarea rows="2" name="reason" class="form-control" readonly required>${testLeave.reason!}</textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 隐藏域 -->
                            <input type="hidden" name="id" value="${testLeave.id}">
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

