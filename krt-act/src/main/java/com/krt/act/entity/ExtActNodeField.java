package com.krt.act.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 流程节点对应的字段权限实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_node_field")
public class ExtActNodeField extends BaseEntity {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型  1=可写(可写的也能读) 2=参与连线判断
     */
    private String fieldType;

    /**
     * 判断规则 
     */
    private String rule;

    /**
     * 条件值
     */
    private String fieldVal;

    /**
     * el表达式 运算符 &&=并且，||=或
     */
    private String elOperator;

    /**
     * 同一节点条件排序
     */
    private Integer sort;

}