package com.krt.act.service;

import com.krt.act.entity.ExtActNodeUser;
import com.krt.common.base.IBaseService;

import java.util.List;


/**
 * 节点可选人服务接口层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
public interface IExtActNodeUserService extends IBaseService<ExtActNodeUser> {

    /**
     * 根据节点id查询审批范围
     *
     * @param nodeId 节点id
     * @return List<ExtActNodeUser>
     */
    List<ExtActNodeUser> selectNodeUserByNodeId(String nodeId);
}
