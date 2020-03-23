package com.krt.sx.entity;

import com.krt.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 当前使用的事项信息实体类
 *
 * @author 李祥
 * @version 1.0
 * @date 2019年04月23日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("z_current_item")
public class ZCurrentItem extends BaseEntity {

    /**
     * 事项编号
     */
    private String itemCode;

    /**
     * 事项名称
     */
    private String itemName;

    /**
     * 办件类型
     */
    private String bjType;

    /**
     * 法定办结时限
     */
    private Integer lawTime;

    /**
     * 承诺办结时限
     */
    private Integer promiseTime;

    /**
     * 是否收费
     */
    private String isPay;

    /**
     * 单位编号
     */
    private String agentCode;

    /**
     * 单位名称
     */
    private String agentName;

    /**
     * 区域编号
     */
    private String areaCode;

    /**
     * 项目名称
     */
    private String areaName;

    /**
     * 排序
     */
    private Integer px;

    /**
     * 是否热门事项
     */
    private Integer state;

    /**
     * 备注
     */
    private String remark;

    /**
     * 跳转地址
     */
    private String goUrl;

    /**
     * 是否需要承诺
     */
    private Integer isPromise;

    /**
     * 承诺内容
     */
    private String promiseContent;

    /**
     * 股室名称
     */
    private String departName;

    /**
     * 股室编号
     */
    private String departCode;

    /**
     * 流程KEY
     */
    private String actKey;

}