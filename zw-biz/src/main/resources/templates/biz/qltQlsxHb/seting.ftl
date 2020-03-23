<#include "/common/layoutList.ftl">
<@header>
<style>
    /**
     历史选项卡颜色值：#692f6c
    **/
    a:hover,a:focus{
        outline: none;
        text-decoration: none;
    }
    .tab .nav-tabs{
        padding-left: 15px;
        border-bottom: 4px solid #eee;
    }
    .tab .nav-tabs li a{
        color: #fff;
        padding: 10px 20px;
        margin-right: 10px;
        background: #3c8dbc;
        text-shadow: 1px 1px 2px #000;
        border: none;
        border-radius: 0;
        opacity: 0.5;
        position: relative;
        transition: all 0.3s ease 0s;
    }
    .tab .nav-tabs li a:hover{
        background: #3c8dbc;
        opacity: 0.8;
    }
    .tab .nav-tabs li.active a{
        opacity: 1;
    }
    .tab .nav-tabs li.active a,
    .tab .nav-tabs li.active a:hover,
    .tab .nav-tabs li.active a:focus{
        color: #fff;
        background: #3c8dbc;
        border: none;
        border-radius: 0;
    }
    .tab .nav-tabs li a:before,
    .tab .nav-tabs li a:after{
        content: "";
        border-top: 42px solid transparent;
        position: absolute;
        top: -2px;
    }
    .tab .nav-tabs li a:before{
        border-right: 15px solid #3c8dbc;
        left: -15px;
    }
    .tab .nav-tabs li a:after{
        border-left: 15px solid #3c8dbc;
        right: -15px;
    }
    .tab .nav-tabs li a i,

    .tab .nav-tabs li.active a i{
        display: inline-block;
        padding-right: 5px;
        font-size: 15px;
        text-shadow: none;
    }
    .tab .nav-tabs li a span{
        display: inline-block;
        font-size: 14px;
        /* 隐藏文字*/
        /*
       letter-spacing: -9px;
       opacity: 0;
       */
        transition: all 0.3s ease 0s;
    }
    .tab .nav-tabs li a:hover span,
    .tab .nav-tabs li.active a span{
        letter-spacing: 1px;
        opacity: 1;
        transition: all 0.3s ease 0s;
    }
    .tab .tab-content{
        padding: 30px;
        background: #fff;
        font-size: 16px;
        color: #6c6c6c;
        line-height: 25px;
    }
    .tab .tab-content h3{
        font-size: 24px;
        margin-top: 0;
    }
    @media only screen and (max-width: 479px){
        .tab .nav-tabs li{
            width: 100%;
            margin-bottom: 5px;
            text-align: center;
        }
        .tab .nav-tabs li a span{
            letter-spacing: 1px;
            opacity: 1;
        }
    }
</style>
</@header>

<@body >
<input type="hidden" id="rowguid" name="rowguid" value="${qltQlsxHb.rowguid}">
<br><br/>
<div class="container" style="height: 550px;">
    <div class="row" style="width: 1350px;margin-left: -100px;">
        <div class="col-sm-12" style="width: 100%; height: 100%">
            <div class="tab" role="tabpanel">
                <!-- Nav tabs -->
                <ul class="nav nav-tabs" role="tablist">
                    <li role="presentation"  class="active"><a href="#Section0" onclick="setval('0')" role="tab" data-toggle="tab"><i class="fa fa-home"></i><span>事项属性配置</span></a></li>
                    <li role="presentation"><a href="#Section1" onclick="setval('1')" role="tab" data-toggle="tab"><i class="fa fa-home"></i><span>受理材料配置</span></a></li>
                    <li role="presentation"><a href="#Section2" onclick="setval('2')" role="tab" data-toggle="tab"><i class="fa fa-globe"></i><span>审批流程配置</span></a></li>
                    <li role="presentation"><a href="#Section3" onclick="setval('3')" role="tab" data-toggle="tab"><i class="fa fa-briefcase"></i><span>信息共享核验</span></a></li>
                    <li role="presentation"><a href="#Section4" onclick="setval('4')" role="tab" data-toggle="tab"><i class="fa fa-briefcase"></i><span>审批结果配置</span></a></li>
                    <li role="presentation"><a href="#Section5" onclick="setval('5')" role="tab" data-toggle="tab"><i class="fa fa-briefcase"></i><span>事项收费配置</span></a></li>
                </ul>
                <!-- Tab panes -->
                <div style="height: 500px;">
                    <div role="tabpanel" class="tab-pane fade in active" id="Section1">
                        <iframe id="ifrlog" class="ifrlog" src="${basePath}/biz/qltQlsxHb/see?rowguid=${qltQlsxHb.rowguid}" style="border: 0px; width:100%; height:850px;"></iframe>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</@body>
<@footer>
<script type="text/javascript">

    function setval(type){
        var rowguid=$("#rowguid").val();
        //alert("rowguid="+rowguid);
        var url="";
        if(type==0){ //事项属性配置
            url="${basePath}/biz/qltQlsxHb/see?rowguid="+rowguid;
        }else if(type==1){  //受理材料配置
            url="${basePath}/sx/qltQlsxHbFile/list?rowguid="+rowguid;
        }else if(type==2){//审批流程配置
            if(rowguid=='1E9D4764FCB0410BAC1F24E1B25BFC74'){
                url="${basePath}/activiti/modeler.html?modelId=d221996a-4971-11e9-9b1b-fcaa14e56864";
            }else {
                url="${basePath}/activiti/modeler.html?modelId=d24e9cdc-4971-11e9-9b1b-fcaa14e56864";
                //url = "${basePath}/biz/qltQlsxHb/process?rowguid=" + rowguid;
            }
        }else if(type==3){//信息共享核验
            url="${basePath}/sx/qltQlsxHbShare/list?rowguid="+rowguid;
        }else if(type==4){//审批结果配置
            url="${basePath}/sx/qltQlsxHbCheck/list?rowguid="+rowguid;
        }else if(type==5){//事项收费配置
           //url="${basePath}/sx/qltQlsxHbMoney/list?rowguid="+rowguid;
            url="${basePath}/sx/qltQlsxHbMoney/insert?rowguid="+rowguid;
        }else{    //审批通过的合同信息
            url="${basePath}/biz/qltQlsxHb/see?rowguid="+rowguid;
            }
        $("#ifrlog").attr("src",url);
    }
</script>
</@footer>
