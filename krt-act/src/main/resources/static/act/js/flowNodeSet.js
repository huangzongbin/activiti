/**
 * JS动态设置select 后 触发 onchange事件
 * @param el
 */
function simulateClick(el) {
    if (el) {
        var evt;
        if (document.createEvent) { // DOM Level 2 standard
            evt = document.createEvent("MouseEvents");
            evt.initMouseEvent("change", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
            el.dispatchEvent(evt);
        } else if (el.fireEvent) { // IE
            el.fireEvent('onchange');
        }
    }
}

/***
 * 判断 一个字符串是否 在 字符串数组中
 * @param search 一个字符串
 * @param array 字符串数组
 * @returns
 */
function in_array(search, array) {
    for (var i in array) {
        if (array[i] == search) {
            return true;
        }
    }
    return false;
}

/**
 * 根据Json字符串填充表单
 * @param form
 * @param data
 */
function formload(form, data) {
    for (var name in data) {
        var val = data[name];
        if (name === 'buttonCodes' || name === "buttonNames") {
            continue;
        }
        var rr = $("#" + form + " input[name='" + name + "'][type=checkbox],input[name='" + name + "'][type=radio]");
        var icheck = $(".icheck");
        $(".icheck").unbind();
        icheck.iCheck({
            checkboxClass: 'icheckbox_minimal-blue',
            radioClass: 'iradio_minimal-blue'
        });
        $(this).iCheck('uncheck');
        rr.each(function () {
            var valarray = String(val).split(",");
            if (in_array($(this).val(), valarray)) {
                $(this).iCheck('check');
            }
        });
        if (!rr.length) {
            $("#" + form + " input[name=\"" + name + "\"]").val(val);
            $("#" + form + " textarea[name=\"" + name + "\"]").val(val);
            simulateClick($("#" + form + " select[name=\"" + name + "\"]").val(val)[0]);
        }
    }
}
