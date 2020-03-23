<#assign basePath=request.contextPath />
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>科睿特专项事项审批系统V1.1</title>
    <link rel="icon" type="image/ico" href="${basePath}/plugin/res/images/favicon.ico" />
    <link rel="stylesheet" href="${basePath}/plugin/res/css/reset.css" />
    <link rel="stylesheet" href="${basePath}/plugin/res/css/common.css" />
    <link rel="stylesheet" href="${basePath}/plugin/res/layui/css/layui.css">
    <link rel="stylesheet" href="${basePath}/plugin/res/css/admin.css" />
    <!--[if lt IE 9]>
    <script src="${basePath}/plugin/res/lib/html5shiv.min.js"></script>
    <script src="${basePath}/plugin/res/lib/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="mainPage">
    <div class="comLayout clearfix">
        <div class="comLayoutRight fr">
            <div class="dateWrap" id="dateContainer"><span id="dateIn"></span></div>
        </div>
        <div class="comLayoutLeft">
            <div class="runningOuterBox">
                <div class="runningBox">
                    <div class="comSecHead">
                        <h2 class="comSubTit">实时运行情况</h2>
                    </div>
                    <div class="runningCalBox clearfix">
                        <div class="rCalInterface">
                            <h3 class="runItemTit">接口状态</h3>
                            <div class="runContBox interfaceStatus">
                                <div class="interfaceItem">
                                    <i class="runIcon r1"></i><em class="txt1">在线</em><em class="txt2">56</em>
                                </div>
                                <div class="interfaceItem">
                                    <i class="runIcon r2"></i><em class="txt1">异常</em><em class="txt2">01</em>
                                </div>
                                <div class="interfaceItem">
                                    <i class="runIcon r3"></i><em class="txt1">断链</em><em class="txt2">03</em>
                                </div>
                            </div>
                        </div>
                        <div class="rCalAlert">
                            <h3 class="runItemTit">预警督办</h3>
                            <div class="runContBox alertView">
                                <div class="alertViewItem">
                                    <i class="runIcon r4"></i><em class="txt1">超时待办件</em><em class="txt2">56</em>
                                </div>
                                <div class="alertViewItem">
                                    <i class="runIcon r5"></i><em class="txt1">异常待核准件</em><em class="txt2">03</em>
                                </div>
                            </div>
                        </div>
                        <div class="rCalRate">
                            <h3 class="runItemTit">当前办结率</h3>
                            <div class="compRateBox">
                                <span class="dashBoard"><i class="indicatorIcon" id="indicator"></i><em class="num" id="indicatorNum">0%</em></span>
                                <p class="unCompNum">待完成件<em class="num">7459</em></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="comLayout clearfix mt20">
        <div class="comLayoutRight fr">
            <div class="siteNoticeBox">
                <div class="comSecHead clearfix">
                    <h2 class="comSubTit fl">站内通知</h2>
                    <span class="moreDot fr"><a href="#" class="layui-icon layui-icon-more"></a></span>
                </div>
                <div class="siteNoticeList mt20">
                    <div class="sNoticeItem">
                        <span class="date fr">07-28</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">管理员</em><a href="#">关于系统升级维护通知</a></span>
                    </div>
                    <div class="sNoticeItem">
                        <span class="date fr">05-02</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">城管员</em><a href="#">专用审批流程变更事项说明通知..</a></span>
                    </div>
                    <div class="sNoticeItem">
                        <span class="date fr">02-13</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">城管员</em><a href="#">关于系统升级维护通知</a></span>
                    </div>
                    <div class="sNoticeItem">
                        <span class="date fr">07-28</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">管理员</em><a href="#">关于系统升级维护通知</a></span>
                    </div>
                    <div class="sNoticeItem">
                        <span class="date fr">05-02</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">城管员</em><a href="#">专用审批流程变更事项说明通知..</a></span>
                    </div>
                    <div class="sNoticeItem">
                        <span class="date fr">02-13</span>
                        <span class="notice clearfix"><i class="dot"></i><em class="prefix">城管员</em><a href="#">关于系统升级维护通知</a></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="comLayoutLeft">
            <div class="halfWrap">
                <div class="halfItemBox">
                    <div class="effiBox">
                        <div class="comSecHead clearfix">
                            <h2 class="comSubTit fl">站内通知</h2>
                        </div>
                        <div class="theChartBox">
                            <div class="dateConBox mt15">
                                <span class="dateType" id="dateTypeChange"><em>今日</em><em>本月</em><em>全年</em></span>
                                <span class="dateRange ml10">
                  <em>选择日期</em>
                  <span class="dateFromTo">
                    <i class="dateChoIcon"></i><input type="text" id="dateFrom" class="dateCho" placeholder="开始时间" readonly="readonly">
                    <i class="dateChoIcon"></i><input type="text" id="dateTo" class="dateCho" placeholder="结束时间" readonly="readonly">
                  </span>
                  </span>
                            </div>
                            <div class="chartCalc clearfix mt20">
                <span class="numCol">
                  <em class="num">6564</em>
                  <em class="txt">办件总数</em>
                </span>
                                <span class="numCol">
                  <em class="num">15</em>
                  <em class="txt">逾期数</em>
                </span>
                                <span class="numCol">
                  <em class="num">876514</em>
                  <em class="txt">在线时长</em>
                </span>
                            </div>
                            <div class="theChart1" id="theChart"></div>
                        </div>
                    </div>
                </div>
                <div class="halfItemBox">
                    <div class="rightChartBox">
                        <div class="rHotBox clearfix">
                            <div class="rHotTab" id="rHotTab"><em class="cur">热门业务</em><em>热门单位</em></div>
                            <div class="rHotList mt10">
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>1</i><em class="txt">户外广告审批</em></span></div>
                                    <div class="hotBar"><i style="width: 89%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">27232</span><span class="hotTxt"><i>2</i><em class="txt">高龄老人补贴</em></span></div>
                                    <div class="hotBar"><i style="width: 82%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">23335</span><span class="hotTxt"><i>3</i><em class="txt">高龄老人补贴</em></span></div>
                                    <div class="hotBar"><i style="width: 76%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">15636</span><span class="hotTxt"><i>4</i><em class="txt">植物检疫证书核发</em></span></div>
                                    <div class="hotBar"><i style="width: 62%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">11451</span><span class="hotTxt"><i>5</i><em class="txt">《就业失业登记》办理</em></span></div>
                                    <div class="hotBar"><i style="width: 43%"></i></div>
                                </div>
                            </div>
                            <div class="rHotList mt10" style="display: none;">
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>1</i><em class="txt">热门户外广告审批</em></span></div>
                                    <div class="hotBar"><i style="width: 80%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>2</i><em class="txt">高龄老人补贴</em></span></div>
                                    <div class="hotBar"><i style="width: 70%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>3</i><em class="txt">高龄老人补贴</em></span></div>
                                    <div class="hotBar"><i style="width: 60%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>4</i><em class="txt">植物检疫证书核发</em></span></div>
                                    <div class="hotBar"><i style="width: 50%"></i></div>
                                </div>
                                <div class="rHotItem">
                                    <div class="hotTxtBox clearfix"><span class="num fr">32342</span><span class="hotTxt"><i>5</i><em class="txt">《就业失业登记》办理</em></span></div>
                                    <div class="hotBar"><i style="width: 40%"></i></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${basePath}/plugin/res/lib/jquery-1.12.4.min.js"></script>
<script src="${basePath}/plugin/res/layui/layui.js"></script>
<script src="${basePath}/plugin/res/lib/echarts.min.js"></script>
<script src="${basePath}/plugin/res/lib/require.js"></script>
<script type="text/javascript">
    var g = (function(){
        var myChart1 = null,
            myChart2 = null,
            win = $(window),
            dateTypeEle = $('#dateTypeChange'),
            indicator = $('#indicator'),
            indicatorNum = $('#indicatorNum'),
            rHotTab = $('#rHotTab'),
            wave = $('#wave'),
            init = function() {
                loadChart1();
                loadChart2();
                loadCalendar();
                bind();
            },
            loadCalendar = function() {
                /*layui.use('laydate', function(){
		  var laydate = layui.laydate;
		  laydate.render({
		    elem: '#dateIn',
		    position: 'static',
		    showBottom: false,
		    change: function(value, date){ //监听日期被切换
		      lay('#dateContainer').html(value);
		    }
		  });
		});*/

                var arr = [];
                var ttm = ""; //记录点击日期
                var objCale = {
                    clickTarget: 'target', //触发弹层的dom元素ID
                    container: 'dateContainer', //日历容器的dom元素ID
                    angle: 5, //调整预判手势的灵敏度
                    isMask: false, // 是否需要弹层
                    beginTime: [], //如空数组默认设置成1970年1月1日开始,数组的每一位分别是年月日。
                    endTime: [], //如空数组默认设置成次年12月31日,数组的每一位分别是年月日。
                    recentTime: [new Date().getFullYear(), new Date().getMonth() + 1, new Date().getDate()], //如空数组默认设置成当月1日,数组的每一位分别是年月日。
                    isSundayFirst: true, // 周日是否要放在第一列
                    isShowNeighbor: true, // 是否展示相邻月份的月尾和月头
                    isToggleBtn: true, // 是否展示左右切换按钮
                    isToggleTitle: true, //是否显示标题
                    isChinese: true, // 是否是中文
                    monthType: 0, // 0:1月, 1:一月, 2:Jan, 3: April
                    canViewDisabled: true, // 是否可以阅读不在范围内的月份
                    monthData: arr, //整月的数据
                    beforeRenderArr: [{
                        stamp: new Date(new Date().toLocaleDateString()).getTime(),
                        className: 'time-on'
                    }],
                    width: $("#dateContainer").width(),
                    success: function success(item, arr, cal) {
                        var date = new Date(parseInt(item));
                        var month = undefined;
                        var day = undefined;
                        if(date.getMonth() < 9){
                            month = "0" + (date.getMonth()+1);
                        }else{
                            month = date.getMonth()+1;
                        }
                        if(date.getDate() < 10){
                            day = "0" + date.getDate();
                        }else{
                            day = date.getDate();
                        }
                        var nowdate = date.getFullYear() + "-" + month + "-" + day;
                        var d = new Date(Number(item)).toLocaleDateString().split("/");

                        $(".time-on-cls").removeClass("time-on-cls");
                        if (!$(".container-item-" + item).hasClass("time-on")) {
                            $(".container-item-" + item).addClass("time-on-cls");
                        }
                        ttm = item;
                    },
                    switchRender: function switchRender(year, month, cal) {
                        var data = [{
                            'stamp': new Date(new Date().toLocaleDateString()).getTime(),
                            'className': 'time-on'
                        }];
                        $(".container-item-" + ttm).addClass("time-on-cls");
                        if (Number($(".calendar-item2").attr("data-month")) === new Date().getMonth() + 1) {
                            cal.renderCallbackArr(data);
                        }
                    },
                    switchs: false
                };
                require(['${basePath}/plugin/res/lib/calendarES5.js'], function (calendar) {
                    new calendar(objCale);
                    $(".turnToday").click(function () {
                        objCale.recentTime = [2018, 7, 10];
                        new calendar(objCale);
                    });

                });
            },
            loadChart1 = function() {
                var xAxis_data = ['2.13', '2.14', '2.15', '2.15', '2.16', '2.17', '2.18', '2.19'];
                var series_data = [820, 932, 901, 934, 1290, 1330, 1320, 820, 932, 901, 934, 1290];

                myChart1 = echarts.init(document.getElementById('theChart'));
                var option = {
                    grid: {
                        top: '15%',
                        bottom: '12%',
                        right: '3%',
                    },
                    tooltip: {show: true},
                    xAxis: {
                        type: 'category',
                        boundaryGap: false,
                        axisLine: {"show": false},
                        axisTick: {"show": false},
                        splitLine: {"show": false},
                        //axisLabel: {interval: 0},
                        //boundaryGap: [0, 0],
                        interval: 0,
                        data: xAxis_data
                    },
                    yAxis: {
                        type: 'value',
                        axisLine: {'show': false},
                        axisTick: {"show": false},
                        splitLine: {"show": true, lineStyle: {'color': 'rgba(233,233,233,0.9)'}}
                    },
                    series: [{
                        data: series_data,
                        symbol: "circle",
                        symbolSize: 6,
                        type: 'bar',
                        barWidth: 10,
                        itemStyle: {barBorderRadius: [10, 10, 10, 10]},
                        color: '#0090ff',
                        lineStyle: {width: 1},
                        areaStyle: {color: "rgba(0,144,255,0.4)"}
                    }]
                };
                myChart1.setOption(option);
            },
            loadChart2 = function() {
                var percent = 0.65, deg = percent*180, perStr = percent*100+'%';
                indicator.css('transform', 'rotate('+deg+'deg)');
                indicatorNum.html(perStr);
            },
            bind = function() {
                win.resize(function () {
                    myChart1.resize();
                });

                dateTypeEle.on('click', 'em', function(e){
                    var ele = $(this),
                        index = ele.index();
                    if(ele.hasClass('cur')) return false;
                    ele.addClass('cur').siblings().removeClass('cur');
                    console.log('index', index);
                    if(index==0) {// 今日

                    } else if(index==1) { // 本月

                    } else if(index==2) { // 全年

                    }
                });

                rHotTab.on('click', 'em', function(e){
                    if($(this).hasClass('cur')) return false;
                    var index = $(this).index();
                    $(this).addClass('cur').siblings().removeClass('cur');
                    $('.rHotList').hide().eq(index).show();
                });

                layui.use('laydate', function(){
                    var laydate = layui.laydate;
                    //初始赋值
                    laydate.render({
                        elem: '#dateFrom',
                        done: function(value, date){
                            //layer.alert('你选择的日期是：' + value + '<br>获得的对象是' + JSON.stringify(date));
                        }
                    });
                    laydate.render({
                        elem: '#dateTo',
                        isInitValue: true
                    });
                });
            }
        return {
            init: init
        }
    })();
    window.onload = function() {
        g.init();
    }
</script>
</body>
</html>
