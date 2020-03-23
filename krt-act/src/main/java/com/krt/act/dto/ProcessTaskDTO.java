package com.krt.act.dto;

import com.krt.common.constant.GlobalConstant;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 办理流程任务dto
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Data
public class ProcessTaskDTO {

    /**
     * 流程定义id
     */
    private String defId;
    /**
     * 流程实例id
     */
    private String instanceId;

    /**
     * 业务流程状态  1=草稿 2=审批中 3=结束
     */
    private String status;

    /**
     * 流程定义状态
     */
    private String pdStatus;

    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 表单地址
     */
    private String url;

    /**
     * 任务回调
     */
    private String callback;

    /**
     * 当前节点id
     */
    private String nodeId;

    /**
     * 下一个节点id
     */
    private String nextNodeId;

    /**
     * 下一个流向办理人 以逗号隔开
     */
    private String nextUserIds;
    /**
     * 下一个办理人名字 以逗号隔开
     */
    private String nextUserNames;
    /**
     * 流程actKey
     */
    private String actKey;
    /**
     * 业务id
     */
    private Integer busId;

    /**
     * 业务表名
     */
    private String tableName;

    /**
     * 业务单的单据号
     */
    private String code;
    /**
     * 完成任务备注
     */
    private String remark;

    /**
     * 任务创建时间
     */
    private Date createTime;

    /**
     * 任务处理时间
     */
    private Date dealTime;

    /**
     * 流程发起人
     */
    private String startUserName;

    /**
     * 任务处理人
     */
    private String dealName;

    /**
     * 任务预处理人
     */
    private String advanceName;

    /**
     * 任务处理人id
     */
    private Integer dealId;

    /**
     * 业务名称
     */
    private String busName;

    /**
     * 下一级流程变量名,以逗号隔开
     */
    private String varName;

    /**
     * 下一级流程变量值,以逗号隔开
     */
    private String varValue;

    /**
     * 子表json
     */
    private Map<String,String> childForm;

    /**
     * 获取流程变量
     *
     * @return Map 获取流程变量
     */
    public Map<String, Object> getElMap() {
        Map<String, Object> elMap = new HashMap<>(16);
        if (StringUtils.isNotEmpty(this.getVarName())) {
            String[] varNames = this.getVarName().split(GlobalConstant.COMMA);
            String[] varValues = this.getVarValue().split(GlobalConstant.COMMA);
            for (int i = 0; i < varNames.length; i++) {
                if (StringUtils.isEmpty(varNames[i])) {
                    continue;
                }
                elMap.put(varNames[i], varValues[i]);
            }
        }
        return elMap;
    }

}
