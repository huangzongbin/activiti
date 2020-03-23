package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.base.BaseEntity;
import com.krt.common.util.DateUtils;
import com.krt.common.validator.Assert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 任务日志实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_task_log")
public class ExtActTaskLog extends BaseEntity {

    /**
     * 业务id
     */
    private Integer busId;

    /**
     * 流程定义id
     */
    private String defId;

    /**
     * 流程实例Id
     */
    private String instanceId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 流程任务名称
     */
    private String taskName;

    /**
     * 预处理人
     */
    private String advanceId;

    /**
     * 预处理人
     */
    @TableField(exist = false)
    private String advanceName;

    /**
     * 办理人
     */
    private Integer dealId;

    /**
     * 办理人
     */
    @TableField(exist = false)
    private String dealName;

    /**
     * 办理时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dealTime;

    /**
     * 预警时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date warnTime;

    /**
     * 代理人
     */
    private String agenId;

    /**
     * 审批意见
     */
    private String appOpinion;

    /**
     * 审批行为 同意、不同意、驳回、会签 等
     */
    private String appAction;

    /**
     * 是否显示签名
     */
    private String isSign;

    /**
     * 业务表更改的字段记录
     */
    private String columns;

    /**
     * 设置预警时间
     *
     * @param nodeSet 下一个节点
     */
    public void setWarn(ExtActNodeSet nodeSet) {
        if (!Assert.isNull(nodeSet.getWarnLine())) {
            int minute = (int) (nodeSet.getWarnLine() * 60);
            Date warnTime = DateUtils.dateAdd("minute", minute, new Date());
            this.setWarnTime(warnTime);
        }
    }
}