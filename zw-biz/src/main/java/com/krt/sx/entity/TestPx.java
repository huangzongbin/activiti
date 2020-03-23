package com.krt.sx.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
    import java.util.Date;
    import org.springframework.format.annotation.DateTimeFormat;

/**
 * 报销流程测试实体类
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年04月18日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("test_px")
public class TestPx extends BaseEntity {

    /**
     * 报销人
     */
    private Integer userId;

    /**
     * 报销人
     */
    private String userName;

    /**
     * 报销标题
     */
    private String title;

    /**
     * 报销金额
     */
    private Integer day;

    /**
     * 报销原因
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
     * remark
     */
    private String remark;

}