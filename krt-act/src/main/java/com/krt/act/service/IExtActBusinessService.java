package com.krt.act.service;

import com.krt.act.entity.ExtActBusiness;
import com.krt.common.base.IBaseService;

import java.util.List;

/**
 * 业务流程  对应的 业务表接口层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
public interface IExtActBusinessService extends IBaseService<ExtActBusiness> {

    /**
     * 根据pid查询业务流程  对应的 业务表
     *
     * @param pid 业务流程  对应的 业务表pid
     * @return {@link List<ExtActBusiness>}
     */
    List<ExtActBusiness> selectByPid(Integer pid);

    /**
     * 根据extend_act_model中的modelid查询对应的业务
     *
     * @param modelId 模型id
     * @return ExtActBusiness
     */
    ExtActBusiness selectActBusByModelId(String modelId);

}
