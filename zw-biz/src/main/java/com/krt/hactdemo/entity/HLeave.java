package com.krt.hactdemo.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
    import java.util.Date;
    import org.springframework.format.annotation.DateTimeFormat;

/**
 * 请假流程测试实体类
 *
 * @author hzb
 * @version 1.0
 * @date 2020年04月06日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("h_leave")
public class HLeave extends BaseEntity {

    /**
     * 请假人
     */
    private Integer userId;

    /**
     * 请假人
     */
    private String userName;

    /**
     * 请假标题
     */
    private String title;

    /**
     * 请假天数
     */
    private Integer day;

    /**
     * 原因
     */
    private String reason;

    /**
     * 业务流程状态  1=草稿 2=审批中 3=结束
     */
    private String status;

    /**
     * 业务状态
     */
    private String bizStatus;

    /**
     * 流程发起时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 流程实例id
     */
    private String instanceId;

    /**
     * 流程定义id
     */
    private String defId;

    /**
     * 流程发起人
     */
    private String startUserId;

    /**
     * 业务流程单据编号
     */
    private String code;

    /**
     * 审批结果 1为同意,2为不同意,3为审批中
     */
    private String actResult;

    /**
     * recieve_code
     */
    private String recieveCode;

    /**
     * remark
     */
    private String remark;

}