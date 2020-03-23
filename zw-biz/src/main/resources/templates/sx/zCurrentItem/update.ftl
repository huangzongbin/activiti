<#include "/common/layoutForm.ftl">
<@header></@header>
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
                         事项编号：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="itemCode" name="itemCode" value="${zCurrentItem.itemCode!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         事项名称：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="itemName" name="itemName" value="${zCurrentItem.itemName!}" class="form-control">
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         办件类型：
                    </label>
                    <div class="col-sm-8">
    <select class="form-control select2" style="width: 100%" id="bjType" name="bjType">
        <option value="">==请选择==</option>
            <@dic type="" ; dicList>
                <#list dicList as item>
                    <option value="${item.code}" ${((zCurrentItem.bjType==item.code)?string("selected",""))!}>${item.name}</option>
                </#list>
            </@dic>
    </select>
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         法定办结时限：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="lawTime" name="lawTime" value="${zCurrentItem.lawTime!}" class="form-control">
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         承诺办结时限：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="promiseTime" name="promiseTime" value="${zCurrentItem.promiseTime!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         是否收费：
                    </label>
                    <div class="col-sm-8">
      <#--  <@dic type="" ; dicList>
            <#list dicList as item>
                <input type="radio" name="isPay" class="icheck" value="${item.code}" ${((zCurrentItem.isPay==item.code)?string("checked",""))!}> ${item.name}
            </#list>
        </@dic>-->
        <input type="radio" class="icheck" name="sex" value="0" checked}> 是
            &nbsp;&nbsp;&nbsp;&nbsp;
		<input type="radio" class="icheck" name="sex" value="1"}> 否
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         单位编号：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="agentCode" name="agentCode" value="${zCurrentItem.agentCode!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         单位名称：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="agentName" name="agentName" value="${zCurrentItem.agentName!}" class="form-control">
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         区域编号：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="areaCode" name="areaCode" value="${zCurrentItem.areaCode!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         项目名称：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="areaName" name="areaName" value="${zCurrentItem.areaName!}" class="form-control">
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         排序：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="px" name="px" value="${zCurrentItem.px!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         是否热门事项：
                    </label>
                    <div class="col-sm-8">
     <#--   <@dic type="" ; dicList>
            <#list dicList as item>
                <input type="radio" name="state" class="icheck" value="${item.code}" ${((zCurrentItem.state==item.code)?string("checked",""))!}> ${item.name}
            </#list>
        </@dic>-->
<input type="radio" class="icheck" name="sex" value="0" checked}> 是
&nbsp;&nbsp;&nbsp;&nbsp;
<input type="radio" class="icheck" name="sex" value="1"}> 否
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-2">
                         备注：
                    </label>
                    <div class="col-sm-10">
    <textarea rows="2" name="remark" class="form-control">${zCurrentItem.remark!}</textarea>
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         跳转地址：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="goUrl" name="goUrl" value="${zCurrentItem.goUrl!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         是否需要承诺：
                    </label>
                    <div class="col-sm-8">
       <@dic type="" ; dicList>
            <#list dicList as item>
                <input type="radio" name="isPromise" class="icheck" value="${item.code}" ${((zCurrentItem.isPromise==item.code)?string("checked",""))!}> ${item.name}
            </#list>
        </@dic>

<input type="radio" class="icheck" name="sex" value="0" checked}> 是
&nbsp;&nbsp;&nbsp;&nbsp;
<input type="radio" class="icheck" name="sex" value="1"}> 否
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-2">
                         承诺内容：
                    </label>
                    <div class="col-sm-10">
    <@krt.editor  name="promiseContent" value="${zCurrentItem.promiseContent!}" validate=''></@krt.editor>
                    </div>
                </div>
            </div>
                </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         股室名称：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="departName" name="departName" value="${zCurrentItem.departName!}" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label for="pname" class="control-label col-sm-4">
                         股室编号：
                    </label>
                    <div class="col-sm-8">
    <input type="text" id="departCode" name="departCode" value="${zCurrentItem.departCode!}" class="form-control">
                    </div>
                </div>
            </div>
                </div>
    <!-- 隐藏域 -->
                        </div>
                    </form>
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
        validateForm = $("#krtForm").validate({});

    });
    //提交
    function doSubmit() {
        krt.ajax({
            type: "POST",
            url: "${basePath}/sx/zCurrentItem/update",
            data: $('#krtForm').serialize(),
            validateForm: validateForm,
            success: function (rb) {
                krt.layer.msg(rb.msg);
                if (rb.code === 200) {
                    var index = krt.layer.getFrameIndex(); //获取窗口索引
                    krt.table.reloadTable();
                    krt.layer.close(index);
                }
            }
        });
    }
</script>
</@footer>

