package com.krt.act.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krt.act.dto.ProcessNodeDTO;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.act.entity.ExtActModel;
import com.krt.act.entity.ExtActNodeSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 流程通用服务层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
public interface IActService {

    /**
     * 创建流程模型
     *
     * @param extActModel 流程模型
     */
    void createModeler(ExtActModel extActModel);

    /**
     * 部署模型
     *
     * @param modelId 模型id
     * @return {@link boolean}
     */
    boolean deploy(String modelId);

    /**
     * 获取流程图所有节点和连线
     *
     * @param modelId 模型id
     * @return {@link List}
     * @throws Exception 异常
     */
    List<Map<String, String>> getFlows(String modelId) throws Exception;

    /**
     * 根据流程key查询流程
     *
     * @param actKey 流程key
     * @return {@link List}
     */
    List<Map<String, Object>> selectFlowsByActKey(String actKey);

    /**
     * 获取第一个节点（不包括开始节点）
     *
     * @param deployId 部署id
     * @return {@link Map}
     * @throws IOException 异常
     */
    Map getStartFlowInfo(String deployId) throws IOException;

    /**
     * 用户
     *
     * @param para 参数
     * @return {@link Page}
     */
    Page<Map<String, Object>> selectUser(Map para);

    /**
     * 启动流程
     *
     * @param processTaskDTO 流程任务DTO
     * @param nodeSet        下一个节点
     * @throws Exception 异常
     */
    void startFlow(ProcessTaskDTO processTaskDTO, ExtActNodeSet nodeSet) throws Exception;

    /**
     * 我的待办列表
     *
     * @param para 参数
     * @return IPage
     */
    IPage<Map> selectMyToDoList(Map<String, Object> para);

    /**
     * 我的已办列表
     *
     * @param para 参数
     * @return IPage
     */
    IPage<Map> selectMyDoneList(Map<String, Object> para);

    /**
     * 获取流程实例流程图
     *
     * @param instanceId 流程实例id
     * @return InputStream
     */
    InputStream getFlowImgByInstantId(String instanceId);

    /**
     * 根据当前节点,获取下一流向所有的字段变量名
     *
     * @param nodeId 当前节点id
     * @param defId  流程定义id
     * @return {@link Set<String>}
     */
    Set<String> getNextVarNames(String nodeId, String defId);

    /**
     * 根据流程节点id 获取流程下一流向节点集合
     *
     * @param processTaskDTO 流程任务
     * @return {@link List<ProcessNodeDTO>}
     */
    List<ProcessNodeDTO> getNextActNodes(ProcessTaskDTO processTaskDTO);

    /**
     * 办理任务
     *
     * @param processTaskDTO 流程任务
     * @param params         参数
     * @throws Exception 异常
     */
    void doActTask(ProcessTaskDTO processTaskDTO, Map<String, Object> params) throws Exception;

    /**
     * 不同意,直接结束流程
     *
     * @param processTaskDTO 流程任务
     * @param params         参数
     * @param flag           是否终止流程  false:业务记录进入可编辑状态，可以修改业务数据后再提交流程
     * @throws Exception 异常
     */
    void endFailFlow(ProcessTaskDTO processTaskDTO, Map<String, Object> params, boolean flag) throws Exception;

    /**
     * 签收任务
     *
     * @param taskId 任务id
     */
    void claim(String taskId);

    /**
     * 反签
     *
     * @param taskId 任务id
     */
    void unClaim(String taskId);

    /**
     * 退回上一步
     *
     * @param processTaskDTO 流程DTO
     * @param params         参数
     * @throws Exception 异常
     */
    void backPrevious(ProcessTaskDTO processTaskDTO, Map<String, Object> params) throws Exception;

    /**
     * 更新流程状态
     *
     * @param processTaskDTO 流程DTO
     * @throws Exception 异常
     */
    void updatePdStatus(ProcessTaskDTO processTaskDTO) throws Exception;

    /**
     * 自动启动流程
     *
     * @param actKey 流程key
     * @param busId  业务id
     * @throws Exception 异常
     */
    void autoStartFlow(String actKey, Integer busId) throws Exception;

    /**
     * 办理任务（自动处理下一节点的预办理人）
     *
     * @param processTaskDTO 流程任务DTO
     * @param params         业务参数
     * @throws Exception 异常
     */
    void autoDoActTask(ProcessTaskDTO processTaskDTO, Map<String, Object> params) throws Exception;

    /**
     * 更新业务表状态
     *
     * @param tableName 表名
     * @param busId     业务id
     * @param status    业务状态
     */
    void updateBizStatus(String tableName, Integer busId, String status);
}
