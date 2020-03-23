package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 业务流程  对应的 业务表实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_business")
public class ExtActBusiness extends BaseEntity {

    /**
     * 父节点id
     */
    private Integer pid;

    /**
     * 业务流程名字
     */
    private String name;

    /**
     * 流程key
     */
    private String actKey;

    /**
     * 0=根节点 1=分组 2=业务类 3=回调
     */
    private String type;

    /**
     * 类路径
     */
    private String classUrl;

    /**
     * 同一级节点中的序号
     */
    private String sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否包含子集 'true' 'false'
     */
    @TableField(exist = false)
    private String hasChild;

    /**
     * 是否可写
     */
    @TableField(exist = false)
    private List<Map<String, Object>> writes;

    /**
     * 可设置为条件
     */
    @TableField(exist = false)
    private List<Map<String, Object>> judges;
}