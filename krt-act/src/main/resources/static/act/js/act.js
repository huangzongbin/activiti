/**
 * 提交流程
 * @param actKey 流程key
 * @param busId 业务id
 */
function flowSumbit(actKey, busId) {
    var url = krt.util.getBasePath() + "/act/deal/initStartFlow?actKey=" + actKey + "&busId=" + busId;
    //弹框层
    top.krt.layer.openDialog("提交流程", url, "900px", "700px")
}


/**
 * 提交流程（不需要选择下一个节点操作人员）
 * @param actKey 流程key
 * @param busId 业务id
 */
function autoSumbit(actKey, busId) {
    krt.layer.confirm("你确定提交流程吗？", function () {
        krt.ajax({
            type: "POST",
            url: krt.util.getBasePath() + "/act/deal/autoStartFlow",
            data: {actKey: actKey, busId: busId},
            success: function (rb) {
                krt.layer.msg(rb.msg);
                if (rb.code === 200) {
                    krt.table.reloadTable();
                }
            }
        });
    });
}

/**
 * 审批记录
 * @param actKey 流程key
 * @param busId 业务id
 * @param instanceId 实例id
 */
function doTaskTab(actKey, busId, instanceId) {
    var url = krt.util.getBasePath() + "/act/deal/taskInfo?flag=1&actKey=" + actKey + "&busId=" + busId + "&instanceId=" + instanceId;
    //弹框层
    top.krt.tab.openTab("审批记录", url)
}
