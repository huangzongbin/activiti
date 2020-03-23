package com.krt.biz.service;

import com.krt.biz.entity.QltOrganDepartment;
import com.krt.common.base.IBaseService;
import java.util.List;
import java.util.Map;


/**
 * 内设机构设置服务接口层
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年03月14日
 */
public interface IQltOrganDepartmentService extends IBaseService<QltOrganDepartment>{

    /**
     * 根据pid查询内设机构设置
     *
     * @param para 内设机构设置pid
     * @return {@link List<QltOrganDepartment>}
     */
    List<QltOrganDepartment> selectByPid(Map para);

    /**
     * 根据机构名称查询下级
     *
     * @return {@link List<QltOrganDepartment>}
     */
    List<QltOrganDepartment> selectByPara(Map para);

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
    public String selectLastCode(Map para);

    /**
     * 根据code查询内设机构设置
     *
     * @param para 内设机构设置pid
     * @return {@link List<QltOrganDepartment>}
     */
    QltOrganDepartment selectByCode(Map para);


}
