package com.krt.actdemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.krt.common.annotation.ActField;
import com.krt.common.base.BaseActEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 请假流程测试实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("test_leave")
public class TestLeave extends BaseActEntity {

    /**
     * 请假人
     */
    private Integer userId;

    /**
     * 请假人
     */
    private String userName;

    /**
     * 请假标题
     */
    @ActField(name = "请假标题")
    private String title;

    /**
     * 请假天数
     */
    @ActField(name = "请假天数", isJudg = true)
    private Integer day;

    /**
     * 原因
     */
    private String reason;

    /**
     * remark
     */
    private String remark;

}