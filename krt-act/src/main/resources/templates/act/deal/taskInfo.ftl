<#include "/common/layoutForm.ftl">
<@header>
    <style>
        .tab-footer {
            width: 100%;
            text-align: center;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
            border-bottom-right-radius: 3px;
            border-bottom-left-radius: 3px;
            border-top: 1px solid #f4f4f4;
            padding: 10px;
            background-color: #ffffff;
        }
    </style>
</@header>
<@body class="body-bg-default">
    <div class="wrapper">
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <form action="#" method="post" id="krtForm" class="form-horizontal">
                        <div class="nav-tabs-custom">
                            <ul class="nav nav-tabs">
                                <li class="active"><a id="busInfo" href="#tab_1" data-toggle="tab">任务审批</a></li>
                                <li><a id="flowInfo" href="#tab_2" data-toggle="tab">流程信息</a></li>
                                <li><a id="flowImg" href="#tab_3" data-toggle="tab">流程图</a></li>
                                <li><a id="fileInfo" href="#tab_4" data-toggle="tab">材料列表</a></li>

                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane active" id="tab_1">
                                    <#if processTaskDTO.url?index_of("?")!=-1>
                                        <iframe src="${basePath}/${processTaskDTO.url!}&busId=${processTaskDTO.busId!}&taskId=${processTaskDTO.taskId!}&instanceId=${processTaskDTO.instanceId!}&defId=${processTaskDTO.defId!}&nodeId=${nodeSet.nodeId!}&flag=${RequestParameters['flag']!}"
                                                frameborder="0" scrolling="no" width="100%" id="formIframe"></iframe>
                                    </#if>
                                    <#if processTaskDTO.url?index_of("?")==-1>
                                        <iframe src="${basePath}/${processTaskDTO.url!}?busId=${processTaskDTO.busId!}&taskId=${processTaskDTO.taskId!}&instanceId=${processTaskDTO.instanceId!}&defId=${processTaskDTO.defId!}&nodeId=${nodeSet.nodeId!}&flag=${RequestParameters['flag']!}"
                                                frameborder="0" scrolling="no" width="100%" id="formIframe"></iframe>
                                    </#if>
                                </div>
                                <div class="tab-pane" id="tab_2"></div>
                                <div class="tab-pane" id="tab_3">
                                    <img src="${basePath}/act/deal/showFlowImg?instanceId=${RequestParameters['instanceId']!}"/>
                                </div>
                                <div class="tab-pane" id="tab_4"></div>
                            </div>
                            <#if (RequestParameters['flag']=='1')!false>
                                <div class="tab-footer">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="form-group" style="margin-bottom:0px;">
                                                <div style="text-align: center">
                                                    <button type="button" id="closeIframeBtn" class="btn btn-danger"><i class="fa fa-close"></i> 关闭</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</@body>
<@footer>
    <script type="text/javascript" src="${basePath}/plugin/iframeResizer/iframeResizer.min.js"></script>
    <script type="text/javascript">
        //iframe 自适应
        iFrameResize({log: true});

        //显示流程信息
        $("#flowInfo").on("click", function () {
            krt.ajax({
                method: "GET",
                url: "${basePath}/act/deal/flowInfoHtml",
                data: {
                    'busId': '${processTaskDTO.busId!}',
                    'instanceId': '${processTaskDTO.instanceId!}'
                },
                success: function (data) {
                    $("#tab_2").html(data);
                }
            })
        });
        //显示材料列表
        $("#fileInfo").on("click", function () {
            krt.ajax({
                method: "GET",
                url: "${basePath}/sx/sxGe/file/fileInfoHtml",
                data: {
                    'busId': '${processTaskDTO.busId!}',
                    'instanceId': '${processTaskDTO.instanceId!}',
                    'actKey': '${processTaskDTO.actKey!}'
                },
                success: function (data) {
                    $("#tab_4").html(data);
                }
            })
        });
    </script>
</@footer>
