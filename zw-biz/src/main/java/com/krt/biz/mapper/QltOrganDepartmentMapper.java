package com.krt.biz.mapper;

import com.krt.common.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.krt.biz.entity.QltOrganDepartment;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 内设机构设置映射层
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年03月14日
 */
@Mapper
public interface QltOrganDepartmentMapper extends BaseMapper<QltOrganDepartment>{

    /**
     * 根据pid查询内设机构设置
     *
     * @param para 内设机构设置pid
     * @return {@link List<QltOrganDepartment>}
     */
    List<QltOrganDepartment> selectByPid(Map para);

    /**
     * 根据pid查询内设机构设置
     *
     * @param id 内设机构设置pid
     * @return {@link List<QltOrganDepartment>}
     */
    List<QltOrganDepartment> selectChildList(@Param("id")Serializable id);


    /**
     * 查询内设机构树列表
     *
     * @param para
     * @return {@link List<QltOrganDepartment>}
     */
    List<QltOrganDepartment> selectList(Map para);

    /**
     * 获取最大的编码
     * @param para
     * @return
     */
    String selectLastCode(Map para);


    /**
     * 根据code查询内设机构设置
     *
     * @param para 内设机构设置code
     * @return {@link List<QltOrganDepartment>}
     */
    QltOrganDepartment selectByCode(Map para);

    List<QltOrganDepartment> selectByPara(Map para);
}
