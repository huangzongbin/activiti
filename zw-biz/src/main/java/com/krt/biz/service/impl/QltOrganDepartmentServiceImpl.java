package com.krt.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.krt.biz.entity.QltOrganDepartment;
import com.krt.biz.mapper.QltOrganDepartmentMapper;
import com.krt.biz.service.IQltOrganDepartmentService;
import com.krt.common.base.BaseServiceImpl;
import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 内设机构设置服务接口实现层
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年03月14日
 */
@Service
public class QltOrganDepartmentServiceImpl extends BaseServiceImpl<QltOrganDepartmentMapper, QltOrganDepartment> implements IQltOrganDepartmentService {
    /**
     * 根据pid查询内设机构设置
     *
     * @param para 内设机构设置pid
     * @return {@link List<QltOrganDepartment>}
     */
    @Override
    public List<QltOrganDepartment> selectByPid(Map para) {
        return baseMapper.selectByPid(para);
    }

    @Override
    public List<QltOrganDepartment> selectByPara(Map para) {
        return baseMapper.selectByPara(para);
    }

    /**
     * 查询内设机构树列表
     *
     * @param para
     * @return {@link List<QltOrganDepartment>}
     */
    @Override
    public  List<QltOrganDepartment> selectList(Map para){
        return baseMapper.selectList(para);
    }

    /**
     * 递归删除内设机构设置
     *
     * @param id 内设机构设置id
     * @return {@link boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Serializable id) {
        boolean result = super.deleteById(id);
        List<QltOrganDepartment> list = baseMapper.selectChildList(id);
        for (QltOrganDepartment entity : list) {
            deleteById(entity.getId());
        }
        return result;
    }

    /**
     * 获取最大的编码
     * @param para
     * @return
     */
    @Override
    public String selectLastCode(Map para){
      return   baseMapper.selectLastCode(para);
    }

    /**
     * 根据code查询内设机构设置
     *
     * @param par 内设机构设置code
     * @return {@link List<QltOrganDepartment>}
     */
    @Override
    public QltOrganDepartment selectByCode(Map par){
        return   baseMapper.selectByCode(par);
    }


}
