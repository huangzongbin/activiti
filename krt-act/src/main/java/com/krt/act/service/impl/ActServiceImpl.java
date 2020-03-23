package com.krt.act.service.impl;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Joiner;
import com.krt.act.dto.ProcessNodeDTO;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.act.entity.*;
import com.krt.act.mapper.ActMapper;
import com.krt.act.service.*;
import com.krt.common.annotation.ActField;
import com.krt.common.bean.PageHelper;
import com.krt.common.bean.Query;
import com.krt.common.bean.ReturnBean;
import com.krt.common.callback.ActCallback;
import com.krt.common.constant.ActConstant;
import com.krt.common.constant.GlobalConstant;
import com.krt.common.exception.KrtException;
import com.krt.common.session.SessionUser;
import com.krt.common.util.ActUtils;
import com.krt.common.util.AnnotationUtils;
import com.krt.common.util.GenUnderline2Camel;
import com.krt.common.util.ShiroUtils;
import com.krt.common.validator.Assert;
import com.krt.sys.entity.Role;
import com.krt.sys.service.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiTaskAlreadyClaimedException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 流程通用服务实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Slf4j
@Service
public class ActServiceImpl implements IActService {

    @Autowired
    private ActMapper actMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IExtActModelService modelService;

    @Autowired
    private IExtActNodeSetService nodeSetService;

    @Autowired
    private IExtActBusinessService businessService;

    @Autowired
    private IExtActFlowBusinessService flowBusinessService;

    @Autowired
    private IExtActNodeFieldService nodeFieldService;

    @Autowired
    private IExtActTaskLogService taskLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private ActCallback actCallBack;

    @Autowired
    private HttpServletRequest request;

    /**
     * 创建流程模型
     *
     * @param extActModel 流程模型
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createModeler(ExtActModel extActModel) {
        try {
            //editorInfo
            ObjectNode editorNode = objectMapper.createObjectNode();
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            //构建模型
            Model model = repositoryService.newModel();
            //metaInfo 元信息
            ObjectNode metaNode = extActModel.getMetaNode(objectMapper, model);
            model.setName(extActModel.getModelName());
            model.setKey(extActModel.getModelKey());
            model.setMetaInfo(metaNode.toString());
            //保存模型
            repositoryService.saveModel(model);
            String charsetName = "utf-8";
            repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes(charsetName));
            //保存模型扩展表
            extActModel.setModelVersion(model.getVersion());
            extActModel.setModelId(model.getId());
            extActModel.setStatus(StringUtils.isEmpty(model.getDeploymentId()) ? GlobalConstant.YesNo.YES.getValue() : GlobalConstant.YesNo.NO.getValue());
            extActModel.setDeploymentId(model.getDeploymentId());
            modelService.insert(extActModel);
        } catch (Exception e) {
            log.error("创建流程模型异常", e);
        }
    }

    /**
     * 部署模型
     *
     * @param modelId 模型id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deploy(String modelId) {
        log.debug("##########开始部署流程##########");
        try {
            ModelEntity model = (ModelEntity) repositoryService.getModel(modelId);
            //开始部署
            JsonNode jsonNode = objectMapper.readTree(repositoryService.getModelEditorSource(model.getId()));
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
            //获取流程名称
            List<Process> processe = bpmnModel.getProcesses();
            if (processe.size() == 0) {
                throw new KrtException(ReturnBean.error("没有设置流程图"));
            }
            Process process = processe.get(0);
            //设置流程属性
            ExtActModel extActModel = modelService.selectOne(Wrappers.<ExtActModel>lambdaQuery().eq(ExtActModel::getModelId, modelId));
            String key = extActModel.getModelKey();
            process.setId(key);
            process.setName(extActModel.getModelName());
            process.setDocumentation(extActModel.getDescription());
            ObjectNode objectNode = new BpmnJsonConverter().convertToJson(bpmnModel);
            //更新模型信息
            String charsetName = "utf-8";
            repositoryService.addModelEditorSource(modelId, objectNode.toString().getBytes(charsetName));
            //更新模型
            model.setName(extActModel.getModelName());
            model.setKey(key);
            model.setVersion(model.getRevisionNext());
            //元元素metaInfo
            ObjectNode metaNode = extActModel.getMetaNode(objectMapper, model);
            model.setMetaInfo(metaNode.toString());
            //转换bpmnModel为可部署的xml形式
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            DeploymentBuilder db = repositoryService.createDeployment().name(model.getName());
            String processName = model.getName() + ".bpmn20.xml";
            Deployment deployment = db.addString(processName, new String(bpmnBytes, charsetName)).deploy();
            //更新模型部署id
            model.setDeploymentId(deployment.getId());
            repositoryService.saveModel(model);
            //修改扩展模型状态
            ExtActModel actModel = new ExtActModel();
            actModel.setModelId(modelId);
            actModel.setStatus(GlobalConstant.YesNo.NO.getValue());
            actModel.setModelVersion(model.getRevisionNext());
            actModel.setDeploymentId(model.getDeploymentId());
            actModel.setId(extActModel.getId());
            modelService.update(actModel, Wrappers.<ExtActModel>lambdaQuery().eq(ExtActModel::getModelId, modelId));
            repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
            return true;
        } catch (Exception e) {
            log.error("部署失败", e);
            return false;
        }
    }

    /**
     * 获取流程图所有节点和连线
     *
     * @param modelId 模型id
     * @return List 返回所有的连线 和 节点
     * @throws Exception 异常
     */
    @Override
    public List<Map<String, String>> getFlows(String modelId) throws Exception {
        String contextPath = request.getContextPath();
        //转换
        JsonNode jsonNode = objectMapper.readTree(repositoryService.getModelEditorSource(modelId));
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        //取第一个流程,注：不包括子流程 待开发
        if (bpmnModel.getProcesses().size() < 1) {
            return null;
        }
        Process process = bpmnModel.getProcesses().get(0);
        Collection<FlowElement> flowElements = process.getFlowElements();
        //取得其中关键数据
        List<Map<String, String>> lists = new ArrayList<>();
        Map<String, String> tempMap;
        Map<String, Map<String, String>> allMap = new LinkedHashMap<>(16);
        for (FlowElement flowElement : flowElements) {
            tempMap = new HashMap<>(16);
            tempMap.put("treeId", flowElement.getId());
            tempMap.put("modelId", modelId);
            if (flowElement instanceof StartEvent) {
                tempMap.put("treeName", "开始节点");
                tempMap.put("type", ActConstant.NodeType.START.getValue());
                tempMap.put("icon", contextPath + "/editor-app/images/none.png");
            } else if (flowElement instanceof UserTask) {
                tempMap.put("type", ActConstant.NodeType.EXAMINE.getValue());
                tempMap.put("treeName", flowElement.getName() == null ? "" : flowElement.getName());
                tempMap.put("icon", contextPath + "/editor-app/images/typeuser.png");
            } else if (flowElement instanceof ExclusiveGateway) {
                tempMap.put("type", ActConstant.NodeType.BRUNCH.getValue());
                tempMap.put("treeName", flowElement.getName() == null ? "" : flowElement.getName());
                tempMap.put("icon", contextPath + "/editor-app/images/exclusive.png");
            } else if (flowElement instanceof SequenceFlow) {
                tempMap.put("type", ActConstant.NodeType.LINE.getValue());
                tempMap.put("treeName", flowElement.getName() == null ? "" : flowElement.getName());
                tempMap.put("icon", contextPath + "/editor-app/images/sequenceflow.png");
            } else if (flowElement instanceof EndEvent) {
                tempMap.put("type", ActConstant.NodeType.END.getValue());
                if (StringUtils.isNotEmpty(flowElement.getName())) {
                    tempMap.put("treeName", flowElement.getName() == null ? "" : flowElement.getName());
                } else {
                    tempMap.put("treeName", "结束");
                }
                tempMap.put("icon", contextPath + "/editor-app/images/endnone.png");
            }
            String pid = GlobalConstant.DEFAULT_PID.toString();
            if (flowElement instanceof SequenceFlow) {
                pid = ((SequenceFlow) flowElement).getSourceRef();
                tempMap.put("tarid", ((SequenceFlow) flowElement).getTargetRef());
                lists.add(tempMap);
            } else {
                List<SequenceFlow> sqlist = ((FlowNode) flowElement).getIncomingFlows();
                if (sqlist != null && sqlist.size() > 0) {
                    SequenceFlow tem1 = sqlist.get(0);
                    pid = tem1.getSourceRef();
                }
            }
            //满足type类型的才要设置
            if (tempMap.containsKey("type")) {
                tempMap.put("treePid", pid);
                allMap.put(flowElement.getId(), tempMap);
                log.debug("{}--{}", tempMap.get("treeId"), tempMap);
            }
        }
        for (Map<String, String> map : lists) {
            String pid = map.get("treePid");
            //如果该元素的父节点不为空 ，且父节点是 分支类型的
            if (allMap.get(pid) != null && ActConstant.NodeType.BRUNCH.getValue().equals(allMap.get(pid).get("type"))) {
                allMap.get(map.get("tarid")).put("treePid", map.get("treeId"));
            } else {
                allMap.remove(map.get("treeId"));
            }
        }
        lists.clear();
        for (Map.Entry<String, Map<String, String>> entry : allMap.entrySet()) {
            String type = entry.getValue().get("type");
            if (ActConstant.NodeType.EXAMINE.getValue().equals(type)) {
                entry.getValue().put("treePid", GlobalConstant.DEFAULT_PID.toString());
            } else if (ActConstant.NodeType.START.getValue().equals(type)) {
                continue;
            }
            lists.add(entry.getValue());
        }
        return lists;
    }

    /**
     * 根据流程key查询流程
     *
     * @param actKey 流程key
     * @return {@link List}
     */
    @Override
    public List<Map<String, Object>> selectFlowsByActKey(String actKey) {
        return actMapper.selectFlowsByActKey(actKey);
    }

    /**
     * 获取第一个节点（不包括开始节点）
     *
     * @param deployId 部署id
     */
    @Override
    public Map getStartFlowInfo(String deployId) throws IOException {
        Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deployId).singleResult();
        Model model = repositoryService.createModelQuery().deploymentId(deployment.getId()).singleResult();
        byte[] bytes = repositoryService.getModelEditorSource(model.getId());
        JsonNode jsonNode = objectMapper.readTree(bytes);
        BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
        Process process = bpmnModel.getProcesses().get(0);
        List<FlowElement> flowElements = (List<FlowElement>) process.getFlowElements();
        //流程的开始节点
        StartEvent startEvent = null;
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof StartEvent) {
                startEvent = (StartEvent) flowElement;
                break;
            }
        }
        FlowElement fe = null;
        //获取开始的出口流向
        assert startEvent != null;
        SequenceFlow sequenceFlow = startEvent.getOutgoingFlows().get(0);
        for (FlowElement flowElement : flowElements) {
            //抛出异常，开始节点后的第一个节点不为userTask
            if (flowElement.getId().equals(sequenceFlow.getTargetRef())) {
                if (flowElement instanceof UserTask || flowElement instanceof EndEvent) {
                    fe = flowElement;
                    break;
                } else {
                    throw new KrtException(ReturnBean.error("流程设计错误，【开始】之后只能是审批节点或结束节点"));
                }
            }
        }
        if (fe != null) {
            //查询该节点的类型
            ExtActNodeSet extActNodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery()
                    .eq(ExtActNodeSet::getNodeId, fe.getId())
                    .eq(ExtActNodeSet::getModelId, model.getId()));
            if (extActNodeSet == null || StringUtils.isEmpty(extActNodeSet.getNodeType())) {
                throw new KrtException(ReturnBean.error("流程设计错误，【开始】之后只能是审批节点或结束节点"));
            }
            Map<String, Object> map = new HashMap<>(4);
            map.put("nodeId", fe.getId());
            map.put("modelId", model.getId());
            map.put("nodeType", extActNodeSet.getNodeType());
            map.put("nodeAction", extActNodeSet.getNodeAction());
            return map;
        }
        return null;
    }

    /**
     * 用户
     *
     * @param para 参数
     * @return {@link Page}
     */
    @Override
    public Page<Map<String, Object>> selectUser(Map para) {
        Query<Map<String, Object>> query = new Query<>(para);
        Page<Map<String, Object>> page = query.getPage();
        PageHelper.startPage(page);
        List<Map<String, Object>> list = actMapper.selectUser(para);
        for (Map<String, Object> map : list) {
            Integer userId = Integer.valueOf(map.get("userId").toString());
            List<String> roleNameList = roleService.selectUserRoles(userId).stream().map(Role::getName).collect(Collectors.toList());
            String roleName = Joiner.on(GlobalConstant.COMMA).join(roleNameList);
            map.put("roleName", roleName);
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 启动流程
     *
     * @param processTaskDTO 流程任务DTO
     * @param nodeSet        下一个节点
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startFlow(ProcessTaskDTO processTaskDTO, ExtActNodeSet nodeSet) throws Exception {
        //查询流程业务关联信息
        ExtActBusiness extActBusiness = businessService.selectOne(Wrappers.<ExtActBusiness>lambdaQuery().eq(ExtActBusiness::getActKey, processTaskDTO.getActKey()));
        Class<?> clazz = Class.forName(extActBusiness.getClassUrl());
        TableName tableName = clazz.getAnnotation(TableName.class);
        processTaskDTO.setTableName(tableName.value());
        Map<String, Object> params = new HashMap<>(3);
        params.put("tableName", tableName.value());
        params.put("id", processTaskDTO.getBusId());
        //查询当前要提交流程的业务记录
        Map<String, Object> busInfo = actMapper.selectBusiByBusId(params);
        Field[] fields = clazz.getDeclaredFields();
        //读取需要判断的条件字段，作为流程变量
        Map<String, Object> variables = new HashMap<>(5);
        for (Field field : fields) {
            ActField actField = field.getAnnotation(ActField.class);
            if (actField != null && actField.isJudg()) {
                String fieldName = field.getName();
                variables.put(fieldName, busInfo.get(fieldName));
            }
        }
        log.debug("############# 1、启动流程并设置启动变量 #############");
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processTaskDTO.getDefId(), processTaskDTO.getBusId().toString(), variables);
        processTaskDTO.setInstanceId(processInstance.getId());
        log.debug("############# 2、更新当前业务表 #############");
        Map<String, Object> busPara = new HashMap<>(8);
        busPara.put("instanceId", processInstance.getProcessInstanceId());
        busPara.put("defId", processTaskDTO.getDefId());
        busPara.put("startUserId", Objects.requireNonNull(ShiroUtils.getSessionUser()).getId());
        busPara.put("act_result", ActConstant.ActResult.DISAGREE.getValue());
        busPara.put("biz_status", "1");
        busPara.put("status", ActConstant.ActStauts.APPROVAL.getValue());
        busPara.put("startTime", new Date());
        busPara.putAll(params);
        actMapper.updateBusInfo(busPara);
        log.debug("############# 3、保存流程业务关系表 #############");
        ExtActFlowBusiness flowBus = new ExtActFlowBusiness();
        flowBus.setBusId(processTaskDTO.getBusId());
        flowBus.setDefId(processTaskDTO.getDefId());
        flowBus.setInstanceId(processTaskDTO.getInstanceId());
        flowBus.setStartTime(new Date());
        flowBus.setActKey(processTaskDTO.getActKey());
        flowBus.setCode((String) busInfo.get("code"));
        flowBus.setStatus(ActConstant.ActStauts.APPROVAL.getValue());
        flowBus.setStartUserId(ShiroUtils.getSessionUser().getId());
        flowBus.setTableName(tableName.value());
        flowBusinessService.insert(flowBus);
        log.debug("############# 4、保存提交日志 #############");
        ExtActTaskLog taskLog = new ExtActTaskLog();
        taskLog.setBusId(processTaskDTO.getBusId());
        taskLog.setDefId(processTaskDTO.getDefId());
        taskLog.setInstanceId(processTaskDTO.getInstanceId());
        taskLog.setTaskName(ActConstant.DEFAULT_OUTCOME);
        taskLog.setDealTime(new Date());
        taskLog.setDealId(ShiroUtils.getSessionUser().getId());
        taskLog.setAppAction(ActConstant.ActTaskResult.SUBMIT.getValue());
        taskLog.setAppOpinion(processTaskDTO.getRemark());
        taskLogService.insert(taskLog);
        log.debug("############# 5、设置下一个节点 #############");
        if (ActConstant.NodeType.EXAMINE.getValue().equals(nodeSet.getNodeType())) {
            log.debug("############# 下一个节点是审批节点 #############");
            toExamineNode(processTaskDTO, nodeSet);
        } else if (ActConstant.NodeType.END.getValue().equals(nodeSet.getNodeType())) {
            log.debug("############# 下一个节点是结束节点 #############");
            toEndNode(processTaskDTO, taskLog);
        } else {
            throw new KrtException(ReturnBean.error("流程设计错误!"));
        }
    }

    /**
     * 自动启动流程
     *
     * @param actKey 流程key
     * @param busId  业务id
     */
    @Override
    public void autoStartFlow(String actKey, Integer busId) throws Exception {
        //1、获取流程定义
        List<Map<String, Object>> defs = selectFlowsByActKey(actKey);
        boolean flag = defs == null || defs.size() == 0;
        Assert.validate(flag, "流程未定义");
        Assert.validate(defs.size() > 1, "相同的流程定义了多个");
        String deployId = (String) defs.get(0).get("deploymentId");
        String defId = (String) defs.get(0).get("defId");
        //2、获取第一个节点
        Map map = getStartFlowInfo(deployId);
        Assert.isNull(map, "获取第一个节点失败");
        //3、获取节点办理人员
        Map<String, Object> para = new HashMap<>(1);
        para.put("nodeId", map.get("nodeId"));
        List<Map<String, Object>> list = actMapper.selectUser(para);
        flag = list == null || list.size() == 0;
        Assert.validate(flag, "节点办理人员不可为空");
        List<Object> userIds = new ArrayList<>();
        List<Object> userNames = new ArrayList<>();
        for (Map userMap : list) {
            userIds.add(userMap.get("userId"));
            userNames.add(userMap.get("userName"));
        }
        String nextUserIds = Joiner.on(GlobalConstant.COMMA).join(userIds);
        String nextUserNames = Joiner.on(GlobalConstant.COMMA).join(userNames);
        ProcessTaskDTO processTaskDTO = new ProcessTaskDTO();
        processTaskDTO.setBusId(busId);
        processTaskDTO.setActKey(actKey);
        processTaskDTO.setDefId(defId);
        processTaskDTO.setNextUserIds(nextUserIds);
        processTaskDTO.setNextUserNames(nextUserNames);
        processTaskDTO.setNextNodeId(map.get("nodeId").toString());
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, processTaskDTO.getNextNodeId()));
        processTaskDTO.setNextNodeId(nodeSet.getNodeId());
        startFlow(processTaskDTO, nodeSet);
    }

    /**
     * 启动流程 第一个节点是审批节点
     *
     * @param processTaskDTO 流程DTO
     * @param nodeSet        下一个节点
     */
    private void toExamineNode(ProcessTaskDTO processTaskDTO, ExtActNodeSet nodeSet) {
        //获取下个任务
        Task task = taskService.createTaskQuery()
                .processDefinitionId(processTaskDTO.getDefId())
                .processInstanceId(processTaskDTO.getInstanceId())
                .list().get(0);
        //设置下一个任务处理人
        if (!processTaskDTO.getNextUserIds().contains(GlobalConstant.COMMA)) {
            //下一个处理人只有一个就设置，有多个则需要等待签收
            taskService.setAssignee(task.getId(), processTaskDTO.getNextUserIds());
        }
        //记录预处理任务日志
        ExtActTaskLog taskLog = new ExtActTaskLog();
        taskLog.setBusId(processTaskDTO.getBusId());
        taskLog.setDefId(processTaskDTO.getDefId());
        taskLog.setInstanceId(processTaskDTO.getInstanceId());
        taskLog.setTaskId(task.getId());
        taskLog.setTaskName(task.getName());
        taskLog.setAdvanceId(processTaskDTO.getNextUserIds());
        taskLog.setWarn(nodeSet);
        taskLogService.insert(taskLog);
        //提交之后，更改业务审批状态为审批中，审批结果也为审批中
        Map<String, Object> updateMap = new HashMap<>(5);
        updateMap.put("tableName", processTaskDTO.getTableName());
        updateMap.put("id", processTaskDTO.getBusId());
        updateMap.put("status", ActConstant.ActStauts.APPROVAL.getValue());
        updateMap.put("actResult", ActConstant.ActResult.DISAGREE.getValue());
        actMapper.updateBusInfo(updateMap);
    }

    /**
     * 启动流程 第一个节点是结束节点
     *
     * @param processTaskDTO 流程DTO
     */
    private void toEndNode(ProcessTaskDTO processTaskDTO, ExtActTaskLog taskLog) {
        taskLog.setAppOpinion("空流程结束");
        taskLogService.updateById(taskLog);
        //流程完成后，更改当前业务表的流程信息
        updateBIz(processTaskDTO, ActConstant.ActStauts.END.getValue(), ActConstant.ActStauts.END.getValue());
    }


    /**
     * 我的待办列表
     *
     * @param para 参数
     * @return IPage<Map>
     */
    @Override
    public IPage<Map> selectMyToDoList(Map<String, Object> para) {
        Query<Map> query = new Query<>(para);
        Page<Map> page = query.getPage();
        PageHelper.startPage(page);
        //超级管理员可查看所有待办
        SessionUser user = ShiroUtils.getSessionUser();
        assert user != null;
        if (!GlobalConstant.ADMIN.equals(user.getUsername())) {
            para.put("dealId", user.getId());
        }
        List<Map> list = actMapper.selectMyToDoList(para);
        page.setRecords(list);
        return page;
    }

    /**
     * 我的已办列表
     *
     * @param para 参数
     * @return IPage<Map>
     */
    @Override
    public IPage<Map> selectMyDoneList(Map<String, Object> para) {
        Query<Map> query = new Query<>(para);
        Page<Map> page = query.getPage();
        PageHelper.startPage(page);
        //超级管理员可查看所有已办
        SessionUser user = ShiroUtils.getSessionUser();
        assert user != null;
        if (!GlobalConstant.ADMIN.equals(user.getUsername())) {
            para.put("dealId", user.getId());
        }
        List<Map> list = actMapper.selectMyDoneList(para);
        page.setRecords(list);
        return page;
    }

    /**
     * 签收任务
     *
     * @param taskId 任务id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void claim(String taskId) {
        try {
            SessionUser user = ShiroUtils.getSessionUser();
            //设置任务办理人
            assert user != null;
            taskService.claim(taskId, user.getId().toString());
            //处理任务后，更新任务日志
            ExtActTaskLog taskLog = new ExtActTaskLog();
            taskLog.setTaskId(taskId);
            taskLog.setDealId(user.getId());
            taskLog.setDealName(user.getName());
            taskLogService.update(taskLog, Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskLog.getTaskId()));
        } catch (ActivitiTaskAlreadyClaimedException e) {
            throw new KrtException(ReturnBean.error("任务已被签收"));
        }
    }

    /**
     * 反签
     *
     * @param taskId 任务id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unClaim(String taskId) {
        ExtActTaskLog taskLog = taskLogService.selectOne(Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskId));
        taskService.claim(taskId, null);
        //处理任务后，更新任务日志
        taskLogService.update(null, Wrappers.<ExtActTaskLog>lambdaUpdate()
                .set(ExtActTaskLog::getDealId, null)
                .eq(ExtActTaskLog::getId, taskLog.getId()));
    }

    /**
     * 获取流程实例流程图
     *
     * @param instanceId 流程实例id
     * @return InputStream
     */
    @Override
    public InputStream getFlowImgByInstantId(String instanceId) {
        return ActUtils.getFlowImgByInstantId(instanceId);
    }

    /**
     * 根据当前节点,获取下一流向所有的字段变量名
     *
     * @param nodeId 当前节点id
     * @param defId  流程定义id
     * @return {@link Set<String>}
     */
    @Override
    public Set<String> getNextVarNames(String nodeId, String defId) {
        List<PvmTransition> nextPvmTransitions = ActUtils.getNextPvmTransitions(defId, nodeId);
        List<String> nextIds = new ArrayList<>();
        for (PvmTransition pvmTransition : nextPvmTransitions) {
            nextIds.add(pvmTransition.getId());
        }
        if (nextIds.size() < 1) {
            return null;
        }
        List<ExtActNodeField> nodeFields = nodeFieldService.selectList(Wrappers.<ExtActNodeField>lambdaQuery().in(ExtActNodeField::getNodeId, nextIds));
        //节点可更改的变量字段数组
        Set<String> vars = new HashSet<>();
        for (ExtActNodeField nodeField : nodeFields) {
            vars.add(nodeField.getFieldName());
        }
        return vars;
    }

    /**
     * 根据流程节点id 获取流程下一流向节点集合
     *
     * @param processTaskDTO 流程任务
     * @return {@link List<ProcessNodeDTO>}
     */
    @Override
    public List<ProcessNodeDTO> getNextActNodes(ProcessTaskDTO processTaskDTO) {
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        Map<String, Object> elMap = processTaskDTO.getElMap();
        List<PvmActivity> pvmActivities = ActUtils.getNextActNodes(processTaskDTO.getDefId(), task.getTaskDefinitionKey(), elMap);
        List<ProcessNodeDTO> listNode = new ArrayList<>();
        ProcessNodeDTO processNodeDTO;
        for (PvmActivity pvm : pvmActivities) {
            processNodeDTO = new ProcessNodeDTO();
            processNodeDTO.setNodeId(pvm.getId());
            processNodeDTO.setNodeName((String) pvm.getProperty("name"));
            ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, pvm.getId()));
            processNodeDTO.setNodeType(nodeSet.getNodeType());
            processNodeDTO.setNodeAction(nodeSet.getNodeAction());
            listNode.add(processNodeDTO);
        }
        return listNode;
    }

    /**
     * 办理任务
     *
     * @param processTaskDTO 流程任务DTO
     * @param map            改变的value
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doActTask(ProcessTaskDTO processTaskDTO, Map<String, Object> map) throws Exception {
        //根据流程定义id查询流程定义key
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processTaskDTO.getDefId())
                .singleResult();
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        //查询流程业务关联信息
        ExtActBusiness business = businessService.selectOne(Wrappers.<ExtActBusiness>lambdaQuery().eq(ExtActBusiness::getActKey, processDefinition.getKey()));
        //当前节点
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, task.getTaskDefinitionKey()));
        Class<?> clazz = Class.forName(business.getClassUrl());
        TableName tableName = clazz.getAnnotation(TableName.class);
        processTaskDTO.setTableName(tableName.value());
        //更改的值不为空
        String filedText = "";
        if (StringUtils.isNotEmpty(nodeSet.getChangeField())) {
            //保存流程更改过的业务记录信息
            log.debug("############# 更新业务记录信息 #############");
            filedText = changeFields(processTaskDTO, nodeSet.getChangeField(), business.getClassUrl(), map);
        }
        //流程变量
        Map<String, Object> elMap = processTaskDTO.getElMap();
        //获取下个节点信息
        List<PvmActivity> pvmActivities = ActUtils.getNextActNodes(processTaskDTO.getDefId(), task.getTaskDefinitionKey(), elMap);
        PvmActivity pvmActivity = null;
        if (pvmActivities != null && pvmActivities.size() != 0) {
            pvmActivity = pvmActivities.get(0);
        }
        //下一节点为结束节点时，完成任务更新业务表
        assert pvmActivity != null;
        //查询下个节点信息
        ExtActNodeSet nextNode = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, pvmActivity.getId()));
        processTaskDTO.setNextNodeId(nextNode.getNodeId());
        //审批节点
        if (ActConstant.NodeType.EXAMINE.getValue().equals(nextNode.getNodeType())) {
            doExamineTask(processTaskDTO, nextNode);
        }
        //结束节点
        if (ActConstant.NodeType.END.getValue().equals(nextNode.getNodeType())) {
            doEndTask(processTaskDTO);
        }
        log.debug("############# 更新本次任务日志 #############");
        ExtActTaskLog taskLog = new ExtActTaskLog();
        taskLog.setTaskId(task.getId());
        taskLog.setDealTime(new Date());
        taskLog.setAppOpinion(processTaskDTO.getRemark());
        taskLog.setDealId(Objects.requireNonNull(ShiroUtils.getSessionUser()).getId());
        taskLog.setColumns(filedText);
        taskLog.setAppAction(ActConstant.ActTaskResult.AGREE.getValue());
        taskLogService.update(taskLog, Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskLog.getTaskId()));
        log.debug("############# 执行按钮回调 #############");
        executeButtonCallBack(processTaskDTO);

    }

    /**
     * 办理任务（自动处理下一节点的预办理人）
     *
     * @param processTaskDTO 流程任务DTO
     * @param params         业务参数
     */
    @Override
    public void autoDoActTask(ProcessTaskDTO processTaskDTO, Map<String, Object> params) throws Exception {
        log.debug("processTaskDTO:{}", processTaskDTO);
        //1、获取下一个节点
        List<ProcessNodeDTO> nodes = getNextActNodes(processTaskDTO);
        boolean flag = nodes != null && nodes.size() > 0;
        Assert.validate(!flag, "流程设计错误");
        ProcessNodeDTO processNodeDTO = nodes.get(0);
        //2、获取下一个节点办理人
        Map<String, Object> para = new HashMap<>(1);
        para.put("nodeId", processNodeDTO.getNodeId());
        List<Map<String, Object>> list = actMapper.selectUser(para);
        flag = list == null || list.size() == 0 && !ActConstant.NodeType.END.getValue().equals(processNodeDTO.getNodeType());
        Assert.validate(flag, "节点办理人员不可为空");
        List<Object> userIds = new ArrayList<>();
        List<Object> userNames = new ArrayList<>();
        for (Map userMap : list) {
            userIds.add(userMap.get("userId"));
            userNames.add(userMap.get("userName"));
        }
        String nextUserIds = Joiner.on(GlobalConstant.COMMA).join(userIds);
        String nextUserNames = Joiner.on(GlobalConstant.COMMA).join(userNames);
        processTaskDTO.setNextUserIds(nextUserIds);
        processTaskDTO.setNextUserNames(nextUserNames);
        //办理任务
        doActTask(processTaskDTO, params);
    }

    /**
     * 处理审批任务
     *
     * @param processTaskDTO 流程业务实体DTO
     * @param nodeSet        下一个节点
     */
    private void doExamineTask(ProcessTaskDTO processTaskDTO, ExtActNodeSet nodeSet) {
        log.debug("############# 处理审批节点 #############");
        log.debug("############# 1、完成任务 #############");
        taskService.complete(processTaskDTO.getTaskId(), processTaskDTO.getElMap());
        Task nextTask = taskService.createTaskQuery().processInstanceId(processTaskDTO.getInstanceId()).list().get(0);
        //设置下一个任务处理人
        if (!processTaskDTO.getNextUserIds().contains(GlobalConstant.COMMA)) {
            //下一个处理人只有一个就设置，有多个则需要等待签收
            taskService.setAssignee(nextTask.getId(), processTaskDTO.getNextUserIds());
        }
        log.debug("############# 2、记录业务日志 #############");
        ExtActTaskLog taskLog = new ExtActTaskLog();
        taskLog.setAdvanceId(processTaskDTO.getNextUserIds());
        taskLog.setBusId(processTaskDTO.getBusId());
        taskLog.setDefId(processTaskDTO.getDefId());
        taskLog.setInstanceId(processTaskDTO.getInstanceId());
        taskLog.setTaskId(nextTask.getId());
        taskLog.setTaskName(nextTask.getName());
        taskLog.setWarn(nodeSet);
        taskLogService.insert(taskLog);
    }

    /**
     * 处理结束节点
     *
     * @param processTaskDTO 流程业务实体DTO
     */
    private void doEndTask(ProcessTaskDTO processTaskDTO) {
        log.debug("############# 处理结束节点 #############");
        log.debug("############# 1、完成任务 #############");
        taskService.complete(processTaskDTO.getTaskId(), processTaskDTO.getElMap());
        log.debug("############# 2、更新业务表 #############");
        updateBIz(processTaskDTO, ActConstant.ActStauts.END.getValue(), ActConstant.ActResult.AGREE.getValue());
    }

    /**
     * 更新业务
     *
     * @param processTaskDTO 办理流程任务dto
     * @param status         流程状态
     * @param actResult      流程任务审批结果
     */
    private void updateBIz(ProcessTaskDTO processTaskDTO, String status, String actResult) {
        Map<String, Object> updateMap = new HashMap<>(5);
        updateMap.put("tableName", processTaskDTO.getTableName());
        updateMap.put("pkName", "id");
        updateMap.put("id", processTaskDTO.getBusId());
        updateMap.put("status", status);
        updateMap.put("actResult", actResult);
        actMapper.updateBusInfo(updateMap);
        ExtActFlowBusiness flowBusiness = new ExtActFlowBusiness();
        flowBusiness.setStatus(status);
        flowBusiness.setBusId(processTaskDTO.getBusId());
        flowBusinessService.update(flowBusiness, Wrappers.<ExtActFlowBusiness>lambdaQuery().eq(ExtActFlowBusiness::getBusId, processTaskDTO.getBusId()));
    }


    /**
     * 保存流程更改过的业务记录信息
     *
     * @param processTaskDTO   流程业务DTO
     * @param changeFieldNames 改变的字段
     * @param classUrl         类地址
     * @param map              修改后的value
     */
    private String changeFields(ProcessTaskDTO processTaskDTO, String changeFieldNames, String classUrl, Map<String, Object> map) {
        if (StringUtils.isEmpty(changeFieldNames)) {
            return "";
        }
        //更改的业务字段文本描述
        StringBuilder filedText = new StringBuilder();
        //查询业务记录
        Map<String, Object> tempMap = new HashMap<>(16);
        tempMap.put("tableName", processTaskDTO.getTableName());
        tempMap.put("pkName", "id");
        tempMap.put("id", processTaskDTO.getBusId());
        Map<String, Object> busInfo = actMapper.selectBusiByBusId(tempMap);
        Map<String, String> textMap = new HashMap<>(16);
        List<Map<String, Object>> mapList = AnnotationUtils.getActFieldByClazz(classUrl);
        Map<String, String> childMap = new HashMap<>(16);
        for (Map remap : mapList) {
            ActField actField = (ActField) remap.get("actField");
            String keyName = (String) remap.get("keyName");
            if (actField != null) {
                if (actField.isChildForm()) {
                    childMap.put(keyName, (String) map.get(keyName));
                } else {
                    //子表单不记录修改值
                    textMap.put(keyName, actField.name());
                }
            }
        }
        processTaskDTO.setChildForm(childMap);
        //业务可更改的字段
        String[] changeFields = changeFieldNames.split(GlobalConstant.COMMA);
        //业务可更改的字段和对应更改后的值
        List<Map> fields = new ArrayList<>();
        for (String changeField : changeFields) {
            if (textMap.containsKey(changeField)) {
                //原值
                Object older = busInfo.get(changeField);
                //更改后的值
                Object newer = map.get(changeField);
                //字段text 例：请假天数
                String text = textMap.get(changeField);
                if (older == null) {
                    older = "";
                }
                if (!older.toString().equals(newer.toString())) {
                    filedText.append(text).append("的原值【").append(older).append("】,更改后【").append(newer).append("】<br/>");
                }
                Map<String, Object> field = new HashMap<>(2);
                field.put("fieldName", GenUnderline2Camel.camel2Underline(changeField));
                field.put("fieldValue", map.get(changeField));
                fields.add(field);
            }
        }
        //保存业务更改后的值
        Map<String, Object> params = new HashMap<>(4);
        params.putAll(tempMap);
        params.put("fields", fields);
        actMapper.updateChangeBusInfo(params);
        return filedText.toString();
    }

    /**
     * 不同意,直接结束流程
     *
     * @param processTaskDTO 流程任务
     * @param params         参数
     * @param flag           是否终止流程  false:业务记录进入可编辑状态，可以修改业务数据后再提交流程
     * @throws Exception 异常
     */
    @Override
    public void endFailFlow(ProcessTaskDTO processTaskDTO, Map<String, Object> params, boolean flag) throws Exception {
        //查询流程业务基本信息
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        String ackKey = ActUtils.findProcessDefinitionEntityByTaskId(task.getId()).getKey();
        ExtActBusiness actBus = businessService.selectOne(Wrappers.<ExtActBusiness>lambdaQuery().eq(ExtActBusiness::getActKey, ackKey));
        ActivityImpl endNode = ActUtils.findActivityImpl(processTaskDTO.getTaskId(), "end");
        log.debug("############# 1、跳到结束节点 #############");
        ActUtils.turnTransition(processTaskDTO.getTaskId(), endNode.getId(), null, processTaskDTO.getRemark());
        log.debug("############# 2、更新流程日志表 #############");
        ExtActTaskLog taskLog = new ExtActTaskLog();
        Date date = new Date();
        taskLog.setTaskId(processTaskDTO.getTaskId());
        taskLog.setAppOpinion(processTaskDTO.getRemark());
        taskLog.setDealId(Objects.requireNonNull(ShiroUtils.getSessionUser()).getId());
        taskLog.setDealTime(date);
        if (flag) {
            taskLog.setAppAction(ActConstant.ActTaskResult.END.getValue());
        } else {
            taskLog.setAppAction(ActConstant.ActTaskResult.TURN_DOWN.getValue());
        }
        taskLogService.update(taskLog, Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskLog.getTaskId()));
        Class<?> aClass = Class.forName(actBus.getClassUrl());
        TableName tableName = aClass.getAnnotation(TableName.class);
        processTaskDTO.setTableName(tableName.value());
        log.debug("############# 3、更新业务表 #############");
        Map<String, Object> updateMap = new HashMap<>(8);
        updateMap.put("tableName", tableName.value());
        updateMap.put("pkName", "id");
        updateMap.put("id", processTaskDTO.getBusId());
        updateMap.put("status", ActConstant.ActStauts.END.getValue());
        if (flag) {
            updateMap.put("actResult", ActConstant.ActResult.END.getValue());
        } else {
            updateMap.put("actResult", ActConstant.ActResult.NO_AGREE.getValue());
        }
        actMapper.updateBusInfo(updateMap);
        log.debug("############# 4、更新流程业务 #############");
        ExtActFlowBusiness flowBusiness = new ExtActFlowBusiness();
        flowBusiness.setStatus(ActConstant.ActStauts.END.getValue());
        flowBusiness.setBusId(processTaskDTO.getBusId());
        flowBusinessService.update(flowBusiness, Wrappers.<ExtActFlowBusiness>lambdaQuery().eq(ExtActFlowBusiness::getBusId, flowBusiness.getBusId()));
        log.debug("############# 5、执行按钮回调 #############");
        executeButtonCallBack(processTaskDTO);
    }

    /**
     * 退回上一步
     *
     * @param processTaskDTO 流程任务DTO
     * @param params         业务参数
     * @throws Exception 异常
     */
    @Override
    public void backPrevious(ProcessTaskDTO processTaskDTO, Map<String, Object> params) throws Exception {
        //查询当前任务
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        //查询节点
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, task.getTaskDefinitionKey()));
        //查询上一个节点
        List<PvmTransition> pvmTransitions = ActUtils.getLastPvmTransitions(processTaskDTO.getDefId(), nodeSet.getNodeId());
        if (pvmTransitions.size() == 0) {
            log.debug("############# 上一个节点是开始节点 #############");
            endFailFlow(processTaskDTO, params, false);
        } else {
            //上个节点有多个 查询业务最近的一个节点
            List<String> nodeIdList = new ArrayList<>();
            for (PvmTransition p : pvmTransitions) {
                nodeIdList.add(p.getSource().getId());
            }
            //查询节点
            Map lastNode = actMapper.selectNearNode(nodeIdList);
            String nodeId = (String) lastNode.get("nodeId");
            String taskId = (String) lastNode.get("taskId");
            log.debug("############# 退回上一个节点 #############");
            log.debug("上个节点:lastNode:{}", lastNode);
            ExtActNodeSet lastNodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, nodeId));
            ActUtils.turnTransition(processTaskDTO.getTaskId(), nodeId, processTaskDTO.getElMap(), processTaskDTO.getRemark());
            log.debug("############# 记录预处理业务日志 #############");
            //根据要跳转的任务ID获取其任务
            ExtActTaskLog lastTaskLog = taskLogService.selectOne(Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskId));
            Task nextTask = taskService.createTaskQuery().processInstanceId(processTaskDTO.getInstanceId()).list().get(0);
            //设置下一个任务处理人
            if (!lastTaskLog.getAdvanceId().contains(GlobalConstant.COMMA)) {
                //下一个处理人只有一个就设置，有多个则需要等待签收
                taskService.setAssignee(nextTask.getId(), lastTaskLog.getAdvanceId());
            }
            ExtActTaskLog tLog = new ExtActTaskLog();
            tLog.setAdvanceId(lastTaskLog.getAdvanceId());
            tLog.setBusId(lastTaskLog.getBusId());
            tLog.setDefId(lastTaskLog.getDefId());
            tLog.setInstanceId(lastTaskLog.getInstanceId());
            tLog.setTaskId(nextTask.getId());
            tLog.setTaskName(nextTask.getName());
            tLog.setWarn(lastNodeSet);
            taskLogService.insert(tLog);
            log.debug("############# 更新本次任务日志 #############");
            ExtActTaskLog taskLog = new ExtActTaskLog();
            taskLog.setTaskId(processTaskDTO.getTaskId());
            taskLog.setDealTime(new Date());
            taskLog.setAppOpinion(processTaskDTO.getRemark());
            taskLog.setDealId(Objects.requireNonNull(ShiroUtils.getSessionUser()).getId());
            taskLog.setAppAction(ActConstant.ActTaskResult.TURN_DOWN.getValue());
            taskLogService.update(taskLog, Wrappers.<ExtActTaskLog>lambdaQuery().eq(ExtActTaskLog::getTaskId, taskLog.getTaskId()));
            log.debug("############# 执行按钮回调 #############");
            executeButtonCallBack(processTaskDTO);
        }
    }

    /**
     * 更新流程状态
     *
     * @param processTaskDTO 流程任务DTO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePdStatus(ProcessTaskDTO processTaskDTO) throws Exception {
        ExtActTaskLog taskLog = new ExtActTaskLog();
        //1、更新流程实例
        if (ActConstant.PdStatus.ACTIVE.getValue().equals(processTaskDTO.getPdStatus())) {
            //激活
            runtimeService.activateProcessInstanceById(processTaskDTO.getInstanceId());
        } else if (ActConstant.PdStatus.SUSPEND.getValue().equals(processTaskDTO.getPdStatus())) {
            runtimeService.suspendProcessInstanceById(processTaskDTO.getInstanceId());
            //日志状态改成挂起
            taskLog.setAppAction(ActConstant.ActTaskResult.SUSPEND.getValue());
        }
        //2、更新业务日志
        log.debug("############# 更新本次任务日志 #############");
        taskLog.setTaskId(processTaskDTO.getTaskId());
        taskLog.setDealTime(new Date());
        taskLog.setDealId(Objects.requireNonNull(ShiroUtils.getSessionUser()).getId());
        taskLogService.update(taskLog, Wrappers.<ExtActTaskLog>lambdaUpdate()
                .set(Assert.isNull(taskLog.getAppAction()), ExtActTaskLog::getAppAction, null)
                .eq(ExtActTaskLog::getTaskId, taskLog.getTaskId()));
        //根据流程定义id查询流程定义key
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processTaskDTO.getDefId())
                .singleResult();
        //查询流程业务关联信息
        ExtActBusiness business = businessService.selectOne(Wrappers.<ExtActBusiness>lambdaQuery().eq(ExtActBusiness::getActKey, processDefinition.getKey()));
        Class<?> clazz = Class.forName(business.getClassUrl());
        TableName tableName = clazz.getAnnotation(TableName.class);
        processTaskDTO.setTableName(tableName.value());
        executeButtonCallBack(processTaskDTO);

    }

    /**
     * 更新业务表状态
     *
     * @param tableName 表名
     * @param busId     业务id
     * @param status    业务状态
     */
    @Override
    public void updateBizStatus(String tableName, Integer busId, String status) {
        actMapper.updateBizStatus(tableName, busId, status);
    }

    /**
     * 执行按钮回调
     *
     * @param processTaskDTO 流程任务DTO
     * @throws Exception 异常
     */
    private void executeButtonCallBack(ProcessTaskDTO processTaskDTO) throws Exception {
        if (StringUtils.isNotEmpty(processTaskDTO.getCallback())) {
            if (processTaskDTO.getCallback().contains(GlobalConstant.POINT)) {
                //自定义回调
                executeCallback(processTaskDTO.getCallback(), processTaskDTO);
            } else {
                //公共回调
                actCallBack.callback(processTaskDTO);
            }
        }
    }

    /**
     * 节点回调方法执行
     *
     * @param callBack       回调
     * @param processTaskDTO 流程任务信息
     * @throws Exception 异常
     */
    private void executeCallback(String callBack, ProcessTaskDTO processTaskDTO) throws Exception {
        int lastIndex = callBack.lastIndexOf(GlobalConstant.POINT);
        //方法名
        String methodStr = callBack.substring(lastIndex + 1);
        //类路径
        String classUrl = callBack.substring(0, lastIndex);
        Class<?> clazz = Class.forName(classUrl);
        Object o = clazz.newInstance();
        //回调方法参数 这里可扩展
        Method method = clazz.getMethod(methodStr, ProcessTaskDTO.class);
        method.invoke(o, processTaskDTO);
    }

}
