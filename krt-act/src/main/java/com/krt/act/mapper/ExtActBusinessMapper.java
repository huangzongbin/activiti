package com.krt.act.mapper;

import com.krt.act.entity.ExtActBusiness;
import com.krt.common.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 业务流程  对应的 业务表映射层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
@Mapper
public interface ExtActBusinessMapper extends BaseMapper<ExtActBusiness> {

    /**
     * 根据pid查询业务流程  对应的 业务表
     *
     * @param pid 业务流程  对应的 业务表pid
     * @return {@link List<ExtActBusiness>}
     */
    List<ExtActBusiness> selectByPid(Integer pid);

    /**
     * 根据ext_act_model中的modelId查询对应业务
     *
     * @param modelId 模型id
     * @return ExtActBusiness
     */
    ExtActBusiness selectActBusByModelId(String modelId);
}
