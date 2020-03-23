<#assign basePath=request.contextPath />
<div class="row" style="margin:0">
    <table id="table-list" class="table table-bordered table-hover">
        <thead>
        <tr>
            <th style="text-align: center">序号</th>
            <th>环节名称</th>
            <th>预处理人</th>
            <th>办理人</th>
            <th>处理结果</th>
            <th>审批意见</th>
            <th>更改记录</th>
            <th>办理时间</th>
            <th>创建时间</th>
        </tr>
        </thead>
        <tbody>
        <#list taskLogs as log>
            <tr id="log_${log_index}">
                <td style="text-align: center">${log_index+1}</td>
                <td>${log.taskName!}</td>
                <td>${log.advanceName!}</td>
                <td>${log.dealName!}</td>
                <td>
                    <@dic type="act_task_result" ; dicList>
                        <#list dicList as item>
                            <#if (item.code==log.appAction)!false>${item.name}</#if>
                        </#list>
                    </@dic>
                </td>
                <td>${log.appOpinion!}</td>
                <td>${log.columns!}</td>
                <td>${(log.dealTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                <td>${(log.insertTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
