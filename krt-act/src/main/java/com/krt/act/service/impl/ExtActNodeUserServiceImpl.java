package com.krt.act.service.impl;

import com.krt.act.entity.ExtActNodeUser;
import com.krt.act.mapper.ExtActNodeUserMapper;
import com.krt.act.service.IExtActNodeUserService;
import com.krt.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 节点审批人员服务接口实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Service
public class ExtActNodeUserServiceImpl extends BaseServiceImpl<ExtActNodeUserMapper, ExtActNodeUser> implements IExtActNodeUserService {

    /**
     * 根据节点id查询审批范围
     *
     * @param nodeId 节点id
     * @return List<ExtActNodeUser>
     */
    @Override
    public List<ExtActNodeUser> selectNodeUserByNodeId(String nodeId) {
        return baseMapper.selectNodeUserByNodeId(nodeId);
    }
}
