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
                                        <label for="pname" class="control-label col-sm-4">单位编码：</label>
                                        <div class="col-sm-5">
                                            ${(qltOrgan.code)!}
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">单位名称：</label>
                                        <div class="col-sm-5">
                                            ${(qltOrgan.name)!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">单位简称：</label>
                                        <div class="col-sm-5">
                                            ${(qltOrgan.shortName)!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">区划编码：</label>
                                        <div class="col-sm-5">
                                            ${(qltOrgan.regionCode)!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4">区划名称：</label>
                                        <div class="col-sm-5">
                                            ${(qltOrgan.regionName)!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4"> 二级单位数量：</label>
                                        <div class="col-sm-5">
                                        ${(qltOrgan.childs)!}
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                            <div class="col-sm-12">
                                <div class="form-group">
                                    <label for="pname" class="control-label col-sm-4"> 父级单位编码：</label>
                                    <div class="col-sm-5">
                                    ${(qltOrgan.parentCode)!}
                                    </div>
                                </div>
                            </div>
                        </div>

                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="form-group">
                                        <label for="pname" class="control-label col-sm-4"> 排序号：</label>
                                        <div class="col-sm-5">
                                        ${(qltOrgan.sortOrder)!}
                                        </div>
                                    </div>
                                </div>
                            </div>


                        <!-- 参数 -->
                        <input type="hidden" id="id" name="id" value="${(region.id)!}">
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

