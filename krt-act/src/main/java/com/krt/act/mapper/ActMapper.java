package com.krt.act.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 流程通用操作
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Mapper
public interface ActMapper {


    /**
     * 根据流程key查询
     *
     * @param actKey 流程key
     * @return {@link List}
     */
    List<Map<String, Object>> selectFlowsByActKey(String actKey);

    /**
     * 查询办理人
     *
     * @param para 参数
     * @return {@link List}
     */
    List<Map<String, Object>> selectUser(Map para);

    /**
     * 查询业务信息
     *
     * @param params 参数
     * @return {@link Map}
     */
    Map<String, Object> selectBusiByBusId(Map<String, Object> params);

    /**
     * 更新当前操作流程的业务表信息
     *
     * @param params 参数
     * @return {@link int}
     */
    int updateBusInfo(Map<String, Object> params);

    /**
     * 查询我的待办
     *
     * @param params 参数
     * @return {@link List<Map>}
     */
    List<Map> selectMyToDoList(Map params);

    /**
     * 查询我的待办
     *
     * @param params 参数
     * @return {@link List<Map>}
     */
    List<Map> selectMyDoneList(Map params);

    /**
     * 更新业务表
     *
     * @param params 业务表单参数
     */
    void updateChangeBusInfo(Map<String, Object> params);

    /**
     * 查询最近的上一个操作节点
     *
     * @param nodeIdList 上个节点集合
     * @return {@link Map}
     */
    Map selectNearNode(List<String> nodeIdList);

    /**
     * 更新业务表状态
     *
     * @param tableName 表名
     * @param busId     业务id
     * @param status    业务状态
     */
    void updateBizStatus(@Param("tableName") String tableName,@Param("busId") Integer busId,@Param("status") String status);
}



