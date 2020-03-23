/*! common.js
 * ===============================================
 * 框架共用js封装
 * ===============================================
 *
 * @Author  殷帅
 * @version 1.0.0
 */
var layer_loading;//加载框
var layer_window;//弹出框
var layer_confirm;//询问框

/**
 * 显示加载框
 */
function loading() {
    layer_loading = top.layer.load({
        shade: 0.5
    });
}

/**
 * 关闭加载框
 */
function closeloading() {
    top.layer.close(layer_loading);
}

/**
 * 询问框
 * @param tips
 * @param fun
 */
function confirmx(tips, fun) {
    layer_confirm = top.layer.confirm(tips, {
        btn: ['确定', '取消']
        //按钮
    }, fun, function () {
        layer.close(layer_confirm);
    });
}

/**
 * 打开对话框
 * @param title 标题
 * @param url 地址
 * @param width 宽度
 * @param height 高度
 * @param isMax 是否最大化
 */
function openDialog(title, url, width, height, isMax) {
    layer_window = top.layer.open({
        type: 2,
        area: [width, height],
        title: title,
        maxmin: true, //开启最大化最小化按钮
        content: url,
        btn: ['确定', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            iframeWin.contentWindow.doSubmit();
        },
        cancel: function (index) {
            top.layer.close(layer_window);
        }
    });
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端，直接最大化
        top.layer.full(layer_window);
    }else{
        if(isMax){
            top.layer.full(layer_window);
        }
    }
}

/**
 * 打开查看对话框
 * @param title
 * @param url
 * @param width
 * @param height
 */
function openDialogView(title, url, width, height,isMax) {
    layer_window = top.layer.open({
        type: 2,
        area: [width, height],
        title: title,
        maxmin: true, //开启最大化最小化按钮
        content: url,
        btn: ['关闭'],
        cancel: function (layer_window) {
            top.layer.close(layer_window);
        }
    });
    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端，直接最大化
        top.layer.full(layer_window);
    }else{
        if(isMax){
            top.layer.full(layer_window);
        }
    }
}

/**
 * 刷新Iframe
 */
function refreshIframe() {
    var target = top.$(".J_iframe:visible");
    if (target[0] == null || target[0] == 'undefined') {
        parent.location.href = parent.location.href;
    } else {
        var url = target[0].contentWindow.location.href;
        //显示loading提示
        var loading = layer.load();
        target.attr('src', url).load(function () {
            //关闭loading提示
            layer.close(loading);
        });
    }
}

/**
 * 刷新dataTable
 * @param datatableObj
 */
function refreshTable(datatableObj) {
    if (datatable == null || datatable == 'undefined') {//添加、修改
        var target = top.$(".J_iframe:visible");
        if (target[0] == null || target[0] == 'undefined') {
            var datatable = parent.window.datatable;
            datatable.ajax.reload(null, false);
        } else {
            var datatable = target[0].contentWindow.datatable;
            datatable.ajax.reload(null, false);
        }
    } else {//删除
        start = $("#datatable").dataTable().fnSettings()._iDisplayStart;
        total = $("#datatable").dataTable().fnSettings().fnRecordsDisplay();
        if ((total - start) == 1) {
            if (start > 0) {
                $("#datatable").dataTable().fnPageChange('previous', true);
            }
        }
        datatableObj.ajax.reload(null, false);
    }
}


/**
 * ajax session失效
 */
$.ajaxSetup({
    complete: function (XMLHttpRequest, textStatus) {
        if (XMLHttpRequest.getResponseHeader('Session-Status') == 'timeout') {
            top.layer.msg("登陆超时！请重新登陆！");
            setTimeout(function () {
                top.location.href = top.location.href;
            }, 1000);
        } else if (textStatus == "parsererror") {
            top.layer.msg("JSON解析错误！");
        }
    }
});

/**
 * pace监听ajax
 * 使用前一定要先引入pace.min.js
 */
$(document).ajaxStart(function() {
    Pace.restart();
});

/**
 * 分钟格式华
 * type: d-h-ni:x天X时x分 h-ni:X时x分  h1:x.x时 h2:x.xx时
 */
function subduration(duration,type) {
    //console.log("duration:"+duration);
    if (!isNaN(duration) && duration != 0 && duration != "0") {
        if (type == "d-h-ni") {
            var days = Math.floor(parseInt(duration) / 420);
            var hours = Math.floor(parseInt(duration) / 60 - days * 7);
            var minites = parseInt(duration) % 60;
            var str = "";
            if (days > 0) {
                str = str + "" + days + "天";
            }
            if (hours > 0) {
                str = str + "" + hours + "时";
            }
            if (minites > 0) {
                str = str + "" + minites + "分";
            }
            return str;
        } else if (type == "h1") {
            return hours = parseFloat((parseInt(duration) / 60).toFixed(1));
        } else if (type == "h2") {
            return hours = parseFloat((parseInt(duration) / 60).toFixed(2));
        } else {
            var hours = Math.floor(parseInt(duration) / 60);
            var minites = parseInt(duration) % 60;
            var str = "";
            if (hours > 0) {
                str = str + "" + hours + "时";
            }
            if (minites > 0) {
                str = str + "" + minites + "分";
            }
            return str;
        }


    } else {
        return 0;
    }
}

/**乘法函数，用来得到精确的乘法结果
 *说明：javascript的乘法结果会有误差，在两个浮点数相乘的时候会比较明显。这个函数返回较为精确的乘法结果。
 *调用：accMul(arg1,arg2)
 *返回值：arg1乘以arg2的精确结果
 */
    function accMul(arg1,arg2)
    {
        var m=0,s1=arg1.toString(),s2=arg2.toString();
        try{m+=s1.split(".")[1].length}catch(e){}
        try{m+=s2.split(".")[1].length}catch(e){}
        return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
    }


/**加法函数，用来得到精确的加法结果
*说明：javascript的加法结果会有误差，在两个浮点数相加的时候会比较明显。这个函数返回较为精确的加法结果。
*调用：accAdd(arg1,arg2)
*返回值：arg1加上arg2的精确结果
 */
    function accAdd(arg1,arg2){
        var r1,r2,m;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2))
        return (arg1*m+arg2*m)/m
    }

    /**减法
     *
     * @param arg1
     * @param arg2
     * @returns {number}
     * @constructor
     */
    function Subtr(arg1,arg2){
        var r1,r2,m,n;
        try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
        try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
        m=Math.pow(10,Math.max(r1,r2));
        //last modify by deeka
        //动态控制精度长度
        //n=(r1>=r2)?r1:r2;
        return ((arg1*m-arg2*m)/m);
    }

/**
 * 通过日期计算属于星期几
 * @param thisDate
 * @returns {string}
 */
function getweekday(thisDate) {
        var arys1 = new Array();
        arys1 = thisDate.split('-');     //日期为输入日期，格式为 2018-12-10
        var ssdate = new Date(arys1[0], parseInt(arys1[1] - 1), arys1[2]);
        var  week1=String(ssdate.getDay()).replace("0","日").replace("1","一").replace("2","二").replace("3","三").replace("4","四").replace("5","五").replace("6","六");//就是你要的星期几
        return "星期"+week1; //就是你要的星期几
    }
