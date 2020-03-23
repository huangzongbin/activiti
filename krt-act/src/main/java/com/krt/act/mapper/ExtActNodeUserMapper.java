package com.krt.act.mapper;

import com.krt.common.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.krt.act.entity.ExtActNodeUser;

import java.util.List;

/**
 * 节点可选人映射层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Mapper
public interface ExtActNodeUserMapper extends BaseMapper<ExtActNodeUser>{

    /**
     * 根据节点id查询审批范围
     *
     * @param nodeId 节点id
     * @return List<ExtActNodeUser>
     */
    List<ExtActNodeUser> selectNodeUserByNodeId(String nodeId);
}
