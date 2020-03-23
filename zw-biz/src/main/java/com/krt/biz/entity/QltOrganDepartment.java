package com.krt.biz.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 内设机构设置实体类
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年03月14日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("qlt_organ_department")
public class QltOrganDepartment extends BaseEntity {

    /**
     * 父ID(树形结构)
     */
    private Integer pid;

    /**
     * 单位id(QLT_ORGAN外键）
     */
    private String organId;

    /**
     * 内设机构名称
     */
    private String name;

    /**
     * 组织机构的CODE
     */
    private String code;
    /**
     * 0 公司   1科室   2 科室下的岗位 type
     */
    private String type;

    /**
     * 岗位数
     */
    private Integer postNum;

    /**
     * 人员数
     */
    private Integer userNum;

    /**
     * 是否包含子集 'true' 'false'
     */
    @TableField(exist = false)
    private String hasChild;

}