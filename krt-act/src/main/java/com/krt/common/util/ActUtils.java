package com.krt.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.krt.common.constant.ActConstant;
import com.krt.common.bean.ReturnBean;
import com.krt.common.exception.KrtException;
import com.krt.common.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程工具类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2018年04月22日
 */
@Slf4j
public class ActUtils {


    private static RepositoryService repositoryService = SpringUtils.getBean("repositoryService");
    private static HistoryService historyService = SpringUtils.getBean("historyService");
    private static RuntimeService runtimeService = SpringUtils.getBean("runtimeService");
    private static TaskService taskService = SpringUtils.getBean("taskService");
    private static ObjectMapper objectMapper = SpringUtils.getBean("objectMapper");
    private static ProcessEngineConfiguration processEngineConfiguration = SpringUtils.getBean("processEngineConfiguration");

    /**
     * 根据流程实例Id,获取实时流程图片
     *
     * @param processInstanceId 流程实例id
     * @return 流程图片输入流
     */
    public static InputStream getFlowImgByInstantId(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            return null;
        }
        //获取流程图输入流
        InputStream inputStream;
        // 查询历史
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        // 该流程已经结束
        if (historicProcessInstance.getEndTime() != null) {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();
            inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        } else {
            // 查询当前的流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            List<String> highLightedFlows = new ArrayList<>();
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
            List<String> historicActivityInstanceList = new ArrayList<>();
            for (HistoricActivityInstance hai : historicActivityInstances) {
                historicActivityInstanceList.add(hai.getActivityId());
            }
            List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
            historicActivityInstanceList.addAll(highLightedActivities);
            for (ActivityImpl activity : processDefinitionEntity.getActivities()) {
                int index = historicActivityInstanceList.indexOf(activity.getId());
                if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                    List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                    for (PvmTransition pvmTransition : pvmTransitionList) {
                        String destinationFlowId = pvmTransition.getDestination().getId();
                        if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
                            highLightedFlows.add(pvmTransition.getId());
                        }
                    }
                }
            }
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            List<String> activeActivityIds = new ArrayList<>();
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            for (org.activiti.engine.task.Task task : tasks) {
                activeActivityIds.add(task.getTaskDefinitionKey());
            }
            inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows, "宋体", "宋体", null, null, 1.0);
        }
        return inputStream;
    }

    /**
     * 根据节点id获取下一流向(也就是线)
     *
     * @param defId  流程定义id
     * @param nodeId 节点id
     * @return List<PvmTransition> 线集合
     */
    public static List<PvmTransition> getNextPvmTransitions(String defId, String nodeId) {
        List<PvmTransition> pvmTransitions = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(defId).singleResult();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        //获取所有节点
        List<ActivityImpl> activityList = def.getActivities();
        //然后循环activityList 并判断出当前流程所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
        for (ActivityImpl activityImpl : activityList) {
            String id = activityImpl.getId();
            if (nodeId.equals(id)) {
                pvmTransitions = activityImpl.getOutgoingTransitions();
                //获取可用的线
                pvmTransitions = getNextActivities(pvmTransitions);
            }
        }
        return pvmTransitions;
    }

    /**
     * 根据流向,取下级所有流向 (目前只获取用户任务、互斥网关留下)
     *
     * @param pvmTransitions 线列表
     * @return List<PvmTransition> 线集合
     */
    private static List<PvmTransition> getNextActivities(List<PvmTransition> pvmTransitions) {
        List<PvmTransition> pvmActivities = new ArrayList<>();
        for (PvmTransition p : pvmTransitions) {
            PvmActivity pvmActivity = p.getDestination();
            String type = (String) pvmActivity.getProperty("type");
            if (ActConstant.PvmType.USER_TASK.getValue().equals(type)) {
                //用户任务
                pvmActivities.add(p);
            } else {
                //条件分支
                if (ActConstant.PvmType.EXCLUSIVE_GATEWAY.getValue().equals(type)) {
                    List<PvmTransition> outgoingTransitions = pvmActivity.getOutgoingTransitions();
                    pvmActivities.addAll(outgoingTransitions);
                }
            }
        }
        return pvmActivities;
    }

    /**
     * 根据节点Id取得当前节点的下一流向流程节点,如果Id为空则默认为首节点（条件路由则只返回符合条件的流向）
     *
     * @param defId  流程定义Id
     * @param nodeId 流程节点
     * @param elMap  流程变量el表达式集合
     * @return List<PvmActivity> 节点结合
     */
    public static List<PvmActivity> getNextActNodes(String defId, String nodeId, Map<String, Object> elMap) {
        List<PvmActivity> pvmActivities = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(defId).singleResult();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        //获取所有节点
        List<ActivityImpl> activityList = def.getActivities();
        //然后循环activitiList 并判断出当前流程所处节点，然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，然后根据路径获得下一个节点实例：
        for (ActivityImpl activityImpl : activityList) {
            String id = activityImpl.getId();
            if ("".equals(nodeId)) {
                Object o = activityImpl.getProperties().get("type");
                if (o.equals(ActConstant.PvmType.START_EVENT.getValue())) {
                    //startEvent节点
                    PvmTransition startEvent = activityImpl.getOutgoingTransitions().get(0);
                    List<PvmTransition> pvmTransitions = startEvent.getDestination().getOutgoingTransitions();
                    pvmActivities = getNextActivities(pvmTransitions, elMap);
                    return pvmActivities;
                }
            } else {
                if (nodeId.equals(id)) {
                    List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
                    pvmActivities = getNextActivities(pvmTransitions, elMap);
                    return pvmActivities;
                }
            }

        }
        return pvmActivities;
    }

    /**
     * 各种情况的下级节点
     *
     * @param pvmTransitions 流向 列表
     * @param elMap          分支节点变量
     * @return List<PvmActivity> 节点结合
     */
    private static List<PvmActivity> getNextActivities(List<PvmTransition> pvmTransitions, Map<String, Object> elMap) {
        List<PvmActivity> pvmActivities = new ArrayList<>();
        for (PvmTransition p : pvmTransitions) {
            PvmActivity pvmActivity = p.getDestination();
            String type = (String) pvmActivity.getProperty("type");
            if (ActConstant.PvmType.USER_TASK.getValue().equals(type)) {
                //当前接点下一流向为userTask，则加入下一流向
                pvmActivities.add(pvmActivity);
            } else if (ActConstant.PvmType.EXCLUSIVE_GATEWAY.getValue().equals(type)) {
                List<PvmTransition> outgoingTransitions = pvmActivity.getOutgoingTransitions();
                for (PvmTransition tr1 : outgoingTransitions) {
                    //获取分支节点流向中的判断el表达式
                    String conditionText = (String) tr1.getProperty("conditionText");
                    //进行解析el表达式
                    ExpressionFactory factory = new ExpressionFactoryImpl();
                    SimpleContext context = new SimpleContext();
                    for (String key : elMap.keySet()) {
                        if (conditionText.contains(key)) {
                            context.setVariable(key, factory.createValueExpression(elMap.get(key), String.class));
                        }
                    }
                    ValueExpression e = factory.createValueExpression(context, conditionText, boolean.class);
                    //判断该流向是否符合传入的参数条件
                    if ((Boolean) e.getValue(context)) {
                        pvmActivities.add(tr1.getDestination());
                        break;
                    }
                }
            } else if (ActConstant.PvmType.PARALLEL_GATEWAY.getValue().equals(type)) {
                //并行路由
                List<PvmTransition> outgoingTransitions = pvmActivity.getOutgoingTransitions();
                for (PvmTransition tr2 : outgoingTransitions) {
                    pvmActivities.add(tr2.getDestination());
                }
            } else if (ActConstant.PvmType.END_EVENT.getValue().equals(type)) {
                //结束
                pvmActivities.add(pvmActivity);
            }
        }
        return pvmActivities;
    }


    /**
     * 增加流程连线条件
     *
     * @param modelId   模型id
     * @param nodeId    流程对象id
     * @param condition el 条件表达式
     * @throws Exception 异常
     */
    public static void setSequenceFlowCondition(String modelId, String nodeId, String condition) throws Exception {
        //获取模型--设置连线条件 到 流程中
        byte[] bytes = repositoryService.getModelEditorSource(modelId);
        JsonNode jsonNode = objectMapper.readTree(bytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        FlowElement flowElement = bpmnModel.getFlowElement(nodeId);
        if (!(flowElement instanceof SequenceFlow)) {
            throw new KrtException(ReturnBean.error("不是连线，不能设置条件"));
        }
        SequenceFlow sequenceFlow = (SequenceFlow) flowElement;
        sequenceFlow.setConditionExpression(condition);
        ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
        String charsetName = "utf-8";
        repositoryService.addModelEditorSource(modelId, objectNode.toString().getBytes(charsetName));
    }

    /*************************回退开始***************************/


    /**
     * 获取上一个节点 来源线
     *
     * @param defId  流程定义id
     * @param nodeId 节点id
     * @return List<PvmTransition> 线集合
     */
    public static List<PvmTransition> getLastPvmTransitions(String defId, String nodeId) {
        List<PvmTransition> pvmTransitions = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(defId).singleResult();
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        //获取所有节点
        List<ActivityImpl> activitiList = def.getActivities();
        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (nodeId.equals(id)) {
                pvmTransitions = activityImpl.getIncomingTransitions();
                //判断线的可用性
                pvmTransitions = getLastActivities(pvmTransitions);
            }
        }
        return pvmTransitions;
    }

    /**
     * 过滤获取上一个节点 只获取用户任务节点,如果是条件节点 则获取条件节点的上一个节点
     *
     * @param pvmTransitions 线集合
     * @return List<PvmTransition> 线集合
     */
    private static List<PvmTransition> getLastActivities(List<PvmTransition> pvmTransitions) {
        List<PvmTransition> pvmActivities = new ArrayList<>();
        for (PvmTransition p : pvmTransitions) {
            PvmActivity pvmActivity = p.getSource();
            String type = (String) pvmActivity.getProperty("type");
            log.debug("type:{}", type);
            if (ActConstant.PvmType.USER_TASK.getValue().equals(type)) {
                //任务节点
                pvmActivities.add(p);
            } else {
                //条件分支
                if (ActConstant.PvmType.EXCLUSIVE_GATEWAY.getValue().equals(type)) {
                    List<PvmTransition> incomingTransitions = pvmActivity.getIncomingTransitions();
                    pvmActivities.addAll(incomingTransitions);
                }
            }
        }
        return pvmActivities;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return TaskEntity 任务
     */
    private static TaskEntity findTaskById(String taskId) {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new KrtException(ReturnBean.error("任务实例未找到!"));
        }
        return task;
    }

    /**
     * 根据任务ID和节点ID获取活动节点
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID <br>
     *                   如果为null或""，则默认查询当前活动节点 <br>
     *                   如果为"end"，则查询结束节点 <br>
     * @return ActivityImpl 节点
     */
    public static ActivityImpl findActivityImpl(String taskId, String activityId) {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
        // 获取当前活动节点ID
        if (StringUtils.isEmpty(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据流程定义，获取该流程实例的结束节点
        String end = "END";
        if (end.equalsIgnoreCase(activityId)) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                String type = (String) activityImpl.getProperty("type");
                if (type.equals(ActConstant.PvmType.END_EVENT.getValue())) {
                    return activityImpl;
                }
            }
        }
        // 根据节点ID，获取对应的活动节点
        return processDefinition.findActivity(activityId);
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return ProcessDefinitionEntity 流程定义
     */
    public static ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());
        if (processDefinition == null) {
            throw new KrtException(ReturnBean.error("流程定义未找到!"));
        }
        return processDefinition;
    }

    /**
     * 流程转向操作
     *
     * @param taskId     当前任务ID
     * @param activityId 目标节点任务ID
     * @param variables  流程变量
     */
    public static void turnTransition(String taskId, String activityId, Map<String, Object> variables, String comment) {
        // 当前节点
        ActivityImpl currActivity = findActivityImpl(taskId, null);
        // 清空当前流向
        List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);
        // 创建新流向
        TransitionImpl newTransition = currActivity.createOutgoingTransition();
        // 目标节点
        ActivityImpl pointActivity = findActivityImpl(taskId, activityId);
        // 设置新流向的目标节点
        newTransition.setDestination(pointActivity);
        //查询当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (comment == null) {
            comment = "";
        }
        taskService.addComment(taskId, task.getProcessInstanceId(), comment);
        // 执行转向任务
        task.setDescription("callback");
        taskService.saveTask(task);
        taskService.complete(taskId, variables);
        // 删除目标节点新流入
        pointActivity.getIncomingTransitions().remove(newTransition);
        // 还原以前流向
        restoreTransition(currActivity, oriPvmTransitionList);
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private static List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        List<PvmTransition> oriPvmTransitionList = new ArrayList<>(pvmTransitionList);
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private static void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        pvmTransitionList.addAll(oriPvmTransitionList);
    }
    /*************************回退结束***************************/
}
