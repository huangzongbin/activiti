package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 审批按钮实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年04月01日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_node_button")
public class ExtActNodeButton extends BaseEntity {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 审批按钮编码
     */
    private String buttonCode;

    /**
     * 自定义按钮名称
     */
    private String buttonName;

    /**
     * 回调
     */
    private String callback;

}