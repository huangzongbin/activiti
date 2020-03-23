package com.krt.common.base;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 工作流业务基类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2016年7月16日
 */
@Data
public class BaseActEntity extends BaseEntity {

    /**
     * 业务流程状态  1=草稿 2=审批中 3=结束
     */
    private String status;

    /**
     * 审批结果 1为同意,2为不同意,3为审批中,4为终止
     */
    private String actResult;

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
    private Integer startUserId;

    /**
     * 业务流程单据编号
     */
    private String code;

    /**
     * 业务状态
     */
    private String bizStatus;

    /**
     * 接收的一窗式业务流流水号
     */
    private String recieveCode;

}
