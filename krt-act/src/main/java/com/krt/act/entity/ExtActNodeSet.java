package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 流程节点配置实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_node_set")
public class ExtActNodeSet extends BaseEntity {

    /**
     * 模型id
     */
    private String modelId;

    /**
     * 流程定义id
     */
    private String defId;

    /**
     * 流程节点id
     */
    private String nodeId;

    /**
     * 流程节点类型 =开始节点 2=审批节点 3=分支 4=连线 5=结束节点
     */
    private String nodeType;

    /**
     * 节点行为 2 的时候 ,1=审批 2=会签
     */
    private String nodeAction;

    /**
     * 可更改的字段数据，以逗号隔开
     */
    private String changeField;

    /**
     * 自定义表单url
     */
    private String url;

    /**
     * 预警期限（单位小时）
     */
    private Float warnLine;

    /**
     * 审批用户类型数组
     */
    @TableField(exist = false)
    private String[] userTypes;

    /**
     * 审批用户id
     */
    @TableField(exist = false)
    private Integer[] userIds;

    /**
     * 按钮编码
     */
    @TableField(exist = false)
    private String[] buttonCodes;

    /**
     * 按钮名称
     */
    @TableField(exist = false)
    private String[] buttonNames;

    /**
     * 按钮回调
     */
    @TableField(exist = false)
    private String[] callBacks;

    /**
     * 连线条件集合
     */
    @TableField(exist = false)
    private List<ExtActNodeField> judgeList;

}