package com.krt.act.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.krt.act.dto.ProcessNodeDTO;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.act.entity.*;
import com.krt.act.service.*;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.common.constant.ActConstant;
import com.krt.common.constant.GlobalConstant;
import com.krt.common.validator.Assert;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.batik.dom.ExtendedNode;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 流程处理控制层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Slf4j
@Controller
public class ExtActDealController extends BaseController {

    @Autowired
    private IActService actService;

    @Autowired
    private IExtActModelService actModelService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IExtActTaskLogService taskLogService;

    @Autowired
    private IExtActNodeSetService nodeSetService;

    @Autowired
    private IExtActFlowBusinessService flowBusinessService;

    @Autowired
    private IExtActNodeButtonService nodeButtonService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    /**
     * 根据流程key 获取业务可用的流程
     *
     * @param actKey 流程key
     * @param busId  业务id
     * @return String
     */
    @GetMapping("act/deal/initStartFlow")
    public String initStartFlow(String actKey, String busId) {
        List<Map<String, Object>> defs = actService.selectFlowsByActKey(actKey);
        request.setAttribute("defList", defs);
        request.setAttribute("busId", busId);
        request.setAttribute("actKey", actKey);
        return "act/deal/initStartFlow";
    }

    /**
     * 获取流程第一个节点信息
     *
     * @param deployId 部署id
     * @return {@link ReturnBean}
     */
    @GetMapping(value = "act/deal/getStartFlowInfo")
    @ResponseBody
    public ReturnBean getStartFlowInfo(String deployId) throws IOException {
        Map map = actService.getStartFlowInfo(deployId);
        Assert.isNull(map, "流程图设计错误");
        return ReturnBean.ok(map);
    }

    /**
     * 流程选择审批人窗口
     *
     * @return String
     */
    @GetMapping(value = "act/deal/userList")
    public String userList() {
        return "act/deal/userList";
    }


    /**
     * 流程选择审批人窗口
     *
     * @return DataTable
     */
    @PostMapping(value = "act/deal/userList")
    @ResponseBody
    public DataTable userList(@RequestParam Map para) {
        IPage page = actService.selectUser(para);
        return DataTable.ok(page);
    }

    /**
     * 启动流程(需要手动选择人员)
     *
     * @param processTaskDTO 完成任务dto
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/startFlow")
    @ResponseBody
    public ReturnBean startFlow(ProcessTaskDTO processTaskDTO) throws Exception {
        Assert.isBlank(processTaskDTO.getActKey(), "流程actKey不能为空");
        Assert.isNull(processTaskDTO.getBusId(), "业务ID不能为空");
        Assert.isBlank(processTaskDTO.getDefId(), "流程定义ID不能为空");
        Assert.isBlank(processTaskDTO.getActKey(), "流程actKey不能为空");
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, processTaskDTO.getNextNodeId()));
        boolean flag = Assert.isBlank(processTaskDTO.getNextUserIds())
                && !ActConstant.NodeType.END.getValue().equals(nodeSet.getNodeType());
        Assert.validate(flag, "处理人不能为空");
        actService.startFlow(processTaskDTO, nodeSet);
        return ReturnBean.ok();
    }

    /**
     * 启动流程(自动选择人员)
     *
     * @param actKey 流程key
     * @param busId  业务id
     * @return {@link ReturnBean}
     * @throws Exception 异常
     */
    @PostMapping("act/deal/autoStartFlow")
    @ResponseBody
    public ReturnBean autoStartFlow(String actKey, Integer busId) throws Exception {
        actService.autoStartFlow(actKey, busId);
        return ReturnBean.ok();
    }


    /**
     * 待办列表页
     *
     * @return {@link String}
     */
    @GetMapping("act/deal/myToDoList")
    @RequiresPermissions("act:deal:myToDoList")
    public String myToDoList() {
        return "act/deal/myToDoList";
    }

    /**
     * 待办列表页
     *
     * @param para 参数
     * @return {@link DataTable}
     */
    @PostMapping("act/deal/myToDoList")
    @RequiresPermissions("act:deal:myToDoList")
    @ResponseBody
    public DataTable myToDoList(@RequestParam Map<String, Object> para) {
        IPage page = actService.selectMyToDoList(para);
        return DataTable.ok(page);
    }

    /**
     * 已办列表页
     *
     * @return String
     */
    @GetMapping("act/deal/myDoneList")
    @RequiresPermissions("act:deal:myDoneList")
    public String myDoneList() {
        return "act/deal/myDoneList";
    }

    /**
     * 已办列表页
     *
     * @param para 参数
     * @return DataTable
     */
    @PostMapping("act/deal/myDoneList")
    @RequiresPermissions("act:deal:myDoneList")
    @ResponseBody
    public DataTable myDoneList(@RequestParam Map<String, Object> para) {
        IPage page = actService.selectMyDoneList(para);
        return DataTable.ok(page);
    }


    /**
     * 签收任务
     */
    @RequiresPermissions("act:deal:claim")
    @GetMapping("act/deal/claim")
    @ResponseBody
    public ReturnBean claim(String taskId) {
        actService.claim(taskId);
        return ReturnBean.ok();
    }


    /**
     * 反签收任务
     */
    @RequiresPermissions("act:deal:unClaim")
    @GetMapping("act/deal/unClaim")
    @ResponseBody
    public ReturnBean unClaim(String taskId) {
        actService.unClaim(taskId);
        return ReturnBean.ok();
    }


    /**
     * 任务办理页
     *
     * @param processTaskDTO 办理流程任务dto
     * @return String
     */
    @GetMapping("act/deal/doActTaskUI")
    public String doActTaskUI(ProcessTaskDTO processTaskDTO) {
        request.setAttribute("processTaskDTO", processTaskDTO);
        return "act/deal/doActTaskUI";
    }

    /**
     * 任务详情
     *
     * @param processTaskDTO 办理流程任务dto
     * @return String
     */
    @GetMapping("act/deal/taskInfo")
    public String taskInfo(ProcessTaskDTO processTaskDTO, String flag) {
        //1：办理任务 2：查看任务
        boolean b = "2".equals(flag) || "1".equals(flag);
        Assert.validate(!b, "参数flag错误!");
        ExtActNodeSet nodeSet;
        String isDoTask = "2";
        if (isDoTask.equals(flag)) {
            //办理
            Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
            //查询节点
            nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, task.getTaskDefinitionKey()));
        } else {
            //查看 返回结束节点的url
            ExtActModel actModel = actModelService.selectOne(Wrappers.<ExtActModel>lambdaQuery().eq(ExtActModel::getModelKey, processTaskDTO.getActKey()));
            Model model = repositoryService.getModel(actModel.getModelId());
            nodeSet = nodeSetService.selectOne(
                    Wrappers.<ExtActNodeSet>lambdaQuery()
                            .eq(ExtActNodeSet::getModelId, model.getId())
                            .eq(ExtActNodeSet::getNodeType, ActConstant.NodeType.END.getValue()), false);
        }
        Assert.isBlank(nodeSet.getUrl(), "流程表单路径未填写");
        processTaskDTO.setUrl(nodeSet.getUrl());
        request.setAttribute("nodeSet", nodeSet);
        request.setAttribute("processTaskDTO", processTaskDTO);
        return "act/deal/taskInfo";
    }

    /**
     * 获取任务流程信息
     *
     * @param busId      业务id
     * @param instanceId 实例id
     * @return {@link String}
     */
    @GetMapping("act/deal/flowInfoHtml")
    public String flowInfoHtml(Integer busId, String instanceId) {
        List<ExtActTaskLog> taskLogs = taskLogService.selectTaskLogList(busId, instanceId);
        request.setAttribute("taskLogs", taskLogs);
        request.setAttribute("instanceId", instanceId);
        return "act/deal/flowInfoHtml";
    }

    /**
     * 获取实时流程图
     *
     * @param instanceId 流程实例
     * @return {@link ModelAndView}
     */
    @GetMapping(value = "act/deal/showFlowImg")
    public ModelAndView showFlowImg(String instanceId) throws IOException {
        InputStream in = actService.getFlowImgByInstantId(instanceId);
        //从response对象获取输出流
        OutputStream out = response.getOutputStream();
        //将输入流中的数据读取出来，写到输出流中
        for (int b; (b = in.read()) != -1; ) {
            out.write(b);
        }
        out.close();
        in.close();
        return null;
    }

    /**
     * 转到审批任务选择下一级审批者页面
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link String}
     */
    @RequestMapping(value = "act/deal/nextActTask")
    public String nextActTask(ProcessTaskDTO processTaskDTO) {
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        //查询可更改字段
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, task.getTaskDefinitionKey()));
        //查询流程基本信息
        ExtActFlowBusiness flowBusiness = flowBusinessService.selectOne(
                Wrappers.<ExtActFlowBusiness>lambdaQuery()
                        .eq(ExtActFlowBusiness::getInstanceId, processTaskDTO.getInstanceId())
                        .eq(ExtActFlowBusiness::getBusId, processTaskDTO.getBusId()));
        request.setAttribute("processTaskDTO", processTaskDTO);
        request.setAttribute("nodeSet", nodeSet);
        request.setAttribute("flowBusiness", flowBusiness);
        return "act/deal/nextActTask";
    }

    /**
     * 办理任务时，获取下一个节点的信息
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/getNextActNodes")
    @ResponseBody
    public ReturnBean getNextActNodes(ProcessTaskDTO processTaskDTO) {
        Assert.isBlank(processTaskDTO.getDefId(), "流程定义不能为空!");
        Assert.isBlank(processTaskDTO.getTaskId(), "流程任务id不能为空!");
        List<ProcessNodeDTO> nextActNodes = actService.getNextActNodes(processTaskDTO);
        return ReturnBean.ok(nextActNodes);
    }

    /**
     * 办理任务时查询业务可更改的字段和必要的流程相关信息
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @RequestMapping(value = "act/deal/changeFields", method = RequestMethod.POST)
    @ResponseBody
    public ReturnBean changeFields(ProcessTaskDTO processTaskDTO) {
        Assert.isBlank(processTaskDTO.getTaskId(), "任务id不能为空");
        Assert.isBlank(processTaskDTO.getInstanceId(), "流程实例id不能为空");
        Assert.isBlank(processTaskDTO.getDefId(), "流程定义id不能为空");
        Task task = taskService.createTaskQuery().taskId(processTaskDTO.getTaskId()).singleResult();
        //查询可更改字段
        ExtActNodeSet nodeSet = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, task.getTaskDefinitionKey()));
        //查询需要作为流程条件判断的字段
        Set<String> nextVarNames = actService.getNextVarNames(task.getTaskDefinitionKey(), processTaskDTO.getDefId());
        String[] changeFields = {};
        if (!Assert.isBlank(nodeSet.getChangeField())) {
            changeFields = nodeSet.getChangeField().split(GlobalConstant.COMMA);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("changeFields", changeFields);
        map.put("vars", nextVarNames);
        return ReturnBean.ok(map);
    }


    /**
     * 办理任务需要选择办理人
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/doActTask")
    @ResponseBody
    public ReturnBean doActTask(ProcessTaskDTO processTaskDTO) throws Exception {
        validate(processTaskDTO);
        Map<String, Object> params = getParams();
        actService.doActTask(processTaskDTO, params);
        return ReturnBean.ok();
    }


    /**
     * 办理任务（自动处理下一节点的预办理人）
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/autoDoActTask")
    @ResponseBody
    public ReturnBean autoDoActTask(ProcessTaskDTO processTaskDTO) throws Exception {
        validate(processTaskDTO);
        Map<String, Object> params = getParams();
        actService.autoDoActTask(processTaskDTO, params);
        return ReturnBean.ok();
    }

    /**
     * 驳回到任务发起人，重新编辑提交
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/backStartUser")
    @ResponseBody
    public ReturnBean backStartUser(ProcessTaskDTO processTaskDTO) throws Exception {
        Map<String, Object> params = getParams();
        actService.endFailFlow(processTaskDTO, params, false);
        return ReturnBean.ok();
    }

    /**
     * 终止流程
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/endProcess")
    @ResponseBody
    public ReturnBean endProcess(ProcessTaskDTO processTaskDTO) throws Exception {
        Map<String, Object> params = getParams();
        actService.endFailFlow(processTaskDTO, params, true);
        return ReturnBean.ok();
    }

    /**
     * 退回上一步
     *
     * @param processTaskDTO 流程任务DTO
     * @return {@link ReturnBean}
     */
    @PostMapping("act/deal/backPrevious")
    @ResponseBody
    public ReturnBean backPrevious(ProcessTaskDTO processTaskDTO) throws Exception {
        Map<String, Object> params = getParams();
        actService.backPrevious(processTaskDTO, params);
        return ReturnBean.ok();
    }

    /**
     * 挂起、激活流程实例
     *
     * @param processTaskDTO 流程任务实例
     * @return
     */
    @RequestMapping(value = "act/deal/updatePdStatus")
    @ResponseBody
    public ReturnBean updatePdStatus(ProcessTaskDTO processTaskDTO) throws Exception {
        boolean flag = ActConstant.PdStatus.ACTIVE.getValue().equals(processTaskDTO.getPdStatus())
                || ActConstant.PdStatus.SUSPEND.getValue().equals(processTaskDTO.getPdStatus());
        Assert.validate(!flag, "流程状态错误");
        actService.updatePdStatus(processTaskDTO);
        return ReturnBean.ok();
    }

    /**
     * 获取动态按钮
     *
     * @return String
     */
    @GetMapping("act/deal/getButtons")
    @ResponseBody
    public ReturnBean getButtons(String nodeId, String instanceId) {
        //获取审批按钮
        List buttons = nodeButtonService.selectList(Wrappers.<ExtActNodeButton>lambdaQuery().eq(ExtActNodeButton::getNodeId, nodeId));
        //获取流程实例状态
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        Map<String, Object> map = new HashMap<>(2);
        map.put("isSuspended", processInstance.isSuspended());
        map.put("buttons", buttons);
        return ReturnBean.ok(map);
    }

    /**
     * 参数验证
     *
     * @param processTaskDTO 流程任务实例
     */
    private void validate(ProcessTaskDTO processTaskDTO) {
        Assert.isBlank(processTaskDTO.getTaskId(), "任务id不能为空");
        Assert.isBlank(processTaskDTO.getInstanceId(), "流程实例id不能为空");
        Assert.isBlank(processTaskDTO.getDefId(), "流程定义id不能为空");
        Assert.isNull(processTaskDTO.getBusId(), "业务id不能为空");
    }

    /**
     * 获取业务参数
     *
     * @return {@link Map}
     */
    private Map<String, Object> getParams() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> params = new LinkedCaseInsensitiveMap<>(16);
        for (String key : parameterMap.keySet()) {
            params.put(key, parameterMap.get(key)[0]);
        }
        return params;
    }
}
