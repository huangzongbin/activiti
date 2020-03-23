<#include "/common/layoutForm.ftl">
<@header></@header>
<@body class="body-bg-default">
<div class="wrapper">
    <section class="content">
        <div class="row">
            <div class="col-md-12">
                <form role="form" class="form-horizontal krt-form" id="krtForm">
                    <div class="box-body">
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">事项名称：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.name)!}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">事项编码：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.itemCode)!}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">实施主体：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.orgName)!}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">服务主题：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.folderName)!}
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">受理条件：</label>
                                    <div class="col-sm-5">
                                        1、申请人员应具备允许设置大型户外广告条件； 2、申请场所应符合《城市市容和环境管理条例》的要求。
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办事对象：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.folderName)!}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办件类型：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.folderName)!}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办理时限：</label>
                                    <div class="col-sm-5">
                                    ${(qltQlsxHb.folderName)!}
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12" >
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办理时限：</label>
                                    <div class="col-sm-5">
                                        5   个工作日
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">数量限制：</label>
                                    <div class="col-sm-5">
                                        1次
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办理流程图：</label>
                                    <div class="col-sm-5">
                                        上传
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">办理窗口：</label>
                                    <div class="col-sm-5">
                                        定南县行政服务中心城管局办证窗口	0797—4295776
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">咨询途径：</label>
                                    <div class="col-sm-5">
                                        定南县行政服务中心城管局办证窗口	0797—4295776
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4">监督投诉：</label>
                                    <div class="col-sm-5">
                                        定南县行政服务中心城管局办证窗口	0797—4289738
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- 参数 -->
                        <input type="hidden" id="id" name="id" value="">
                </form>
            </div>
        </div>
</div>
</section>
</div>
</@body>
<@footer>
</script>
</@footer>

