package com.krt.act.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
    import java.util.Date;
    import org.springframework.format.annotation.DateTimeFormat;

/**
 * 业务流程关系表实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_flow_business")
public class ExtActFlowBusiness extends BaseEntity {

    /**
     * 业务ID
     */
    private Integer busId;

    /**
     * 业务流程状态  1=草稿 2=审批中 3=结束
     */
    private String status;

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
     * 流程key
     */
    private String actKey;

    /**
     * 业务表名
     */
    private String tableName;

}