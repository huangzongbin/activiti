package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 节点可选人实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_node_user")
public class ExtActNodeUser extends BaseEntity {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 用户id 根据类型
     */
    private Integer userId;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;

    /**
     * 用户类型 1=用户 2=角色 3=组织
     */
    private String userType;

}