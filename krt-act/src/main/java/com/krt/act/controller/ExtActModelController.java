package com.krt.act.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.krt.act.entity.*;
import com.krt.act.service.*;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.common.constant.ActConstant;
import com.krt.common.validator.Assert;
import com.krt.sys.entity.Dic;
import com.krt.sys.service.IDicService;
import com.krt.sys.service.IOrganService;
import com.krt.sys.service.IRoleService;
import com.krt.sys.service.IUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程模板扩展控制层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Controller
public class ExtActModelController extends BaseController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IOrganService organService;

    @Autowired
    private IDicService dicService;

    @Autowired
    private IActService actService;

    @Autowired
    private IExtActModelService modelService;

    @Autowired
    private IExtActBusinessService businessService;

    @Autowired
    private IExtActNodeSetService nodeSetService;

    @Autowired
    private IExtActNodeUserService nodeUserService;

    @Autowired
    private IExtActNodeFieldService nodeFieldService;

    @Autowired
    private IExtActNodeButtonService nodeButtonService;


    /**
     * 流程模板扩展管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("act:model:list")
    @GetMapping("act/model/list")
    public String list() {
        return "act/model/list";
    }

    /**
     * 流程模板扩展管理
     *
     * @param para 搜索参数
     * @return {@link DataTable}
     */
    @RequiresPermissions("act:model:list")
    @PostMapping("act/model/list")
    @ResponseBody
    public DataTable list(@RequestParam Map para) {
        IPage page = modelService.selectPageList(para);
        return DataTable.ok(page);
    }

    /**
     * 新增流程模板扩展页
     *
     * @return {@link String}
     */
    @RequiresPermissions("act:model:insert")
    @GetMapping("act/model/insert")
    public String insert() {
        return "act/model/insert";
    }

    /**
     * 添加流程模板扩展
     *
     * @param model 流程模板扩展
     * @return {@link ReturnBean}
     */
    @KrtLog("添加流程模板扩展")
    @RequiresPermissions("act:model:insert")
    @PostMapping("act/model/insert")
    @ResponseBody
    public ReturnBean insert(ExtActModel model) {
        validate(model);
        modelService.createModeler(model);
        return ReturnBean.ok();
    }

    /**
     * 修改流程模板扩展页
     *
     * @param id 流程模板扩展id
     * @return {@link String}
     */
    @RequiresPermissions("act:model:update")
    @GetMapping("act/model/update")
    public String update(Integer id) {
        ExtActModel model = modelService.selectById(id);
        ExtActBusiness business = businessService.selectById(model.getExtBusinessId());
        request.setAttribute("model", model);
        request.setAttribute("business", business);
        return "act/model/update";
    }

    /**
     * 修改流程模板扩展
     *
     * @param model 流程模板扩展
     * @return {@link ReturnBean}
     */
    @KrtLog("修改流程模板扩展")
    @RequiresPermissions("act:model:update")
    @PostMapping("act/model/update")
    @ResponseBody
    public ReturnBean update(ExtActModel model) {
        validate(model);
        modelService.updateById(model);
        return ReturnBean.ok();
    }

    /**
     * 删除流程模板扩展
     *
     * @param id 流程模板扩展id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除流程模板扩展")
    @RequiresPermissions("act:model:delete")
    @PostMapping("act/model/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        modelService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 节点设置
     *
     * @param modelId 模型id
     */
    @RequiresPermissions("act:model:flowNodeSet")
    @GetMapping("act/model/flowNodeSet")
    public String flowNodeSet(String modelId) throws Exception {
        //所有节点
        List<Map<String, String>> flows = actService.getFlows(modelId);
        request.setAttribute("flows", JSON.toJSONString(flows));
        //所有回调和业务相关设置
        ExtActBusiness bus = new ExtActBusiness();
        bus.setType(ActConstant.ActBusType.BACK.getValue());
        ExtActBusiness businessEntity = businessService.selectActBusByModelId(modelId);
        List<ExtActBusiness> callbacks = businessService.selectByPid(businessEntity.getId());
        request.setAttribute("callbacks", callbacks);
        request.setAttribute("writes", businessEntity.getWrites());
        request.setAttribute("judges", businessEntity.getJudges());
        //状态回调
        Dic dic = dicService.selectByTypeAndCode(ActConstant.BIZ_STATUS, businessEntity.getActKey());
        if (!Assert.isNull(dic)) {
            List<Dic> statusCallback = dicService.selectByTypeAndPid(ActConstant.BIZ_STATUS, dic.getId());
            request.setAttribute("statusCallback", statusCallback);
        }
        return "act/model/flowNodeSet";
    }

    /**
     * 获取节点的扩展设置信息
     *
     * @return {@link ReturnBean}
     */
    @ResponseBody
    @GetMapping("act/model/flowSetInfo")
    public ReturnBean flowSetInfo(String nodeId, String type) {
        Assert.isEmpty(nodeId, "未获取节点id");
        Map<String, Object> map = new HashMap<>(3);
        ExtActNodeSet nodesetEntity = nodeSetService.selectOne(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getNodeId, nodeId));
        map.put("nodeSet", nodesetEntity);
        //如果节点类型为审批节点
        if (ActConstant.NodeType.EXAMINE.getValue().equals(type)) {
            //审批用户
            List<ExtActNodeUser> userLists = nodeUserService.selectNodeUserByNodeId(nodeId);
            map.put("userList", userLists);
            //审批按钮
            List<ExtActNodeButton> buttonList = nodeButtonService.selectList(Wrappers.<ExtActNodeButton>lambdaQuery().eq(ExtActNodeButton::getNodeId, nodeId));
            map.put("buttonList", buttonList);
        }
        //节点类型为连线
        if (ActConstant.NodeType.LINE.getValue().equals(type)) {
            List<ExtActNodeField> fields = nodeFieldService.selectList(Wrappers.<ExtActNodeField>lambdaQuery().eq(ExtActNodeField::getNodeId, nodeId));
            map.put("fields", fields);
        }
        return ReturnBean.ok(map);
    }

    /**
     * 选择审批范围弹框
     *
     * @return {@link String}
     */
    @GetMapping("act/model/userList")
    public String userList() {
        return "act/model/userList";
    }

    /**
     * 选择审批范围弹框
     *
     * @return {@link DataTable}
     */
    @PostMapping("act/model/userList")
    @ResponseBody
    public DataTable userList(@RequestParam Map para) {
        String type = para.get("type").toString();
        IPage page;
        //类型为用户
        if (ActConstant.ExamineType.USER.getValue().equals(type)) {
            page = userService.selectPageList(para);
        //角色
        } else if (ActConstant.ExamineType.ROLE.getValue().equals(type)) {
            page = roleService.selectPageList(para);
        //组织
        } else if (ActConstant.ExamineType.ORGAN.getValue().equals(type)) {
            page = organService.selectPageList(para);
        } else {
            return null;
        }
        return DataTable.ok(page);
    }

    /**
     * 保存节点设置
     *
     * @param nodeSet 流程节点配置
     * @return {@link ReturnBean}
     */
    @KrtLog("保存节点设置")
    @PostMapping("act/model/saveNode")
    @ResponseBody
    public ReturnBean saveNode(ExtActNodeSet nodeSet) throws Exception {
        Assert.isEmpty(nodeSet.getModelId(), "模型id不能为空");
        Assert.isEmpty(nodeSet.getNodeId(), "节点id不能为空");
        Assert.isEmpty(nodeSet.getNodeType(), "节点类型不能为空");
        ExtActNodeSet node = nodeSetService.saveNode(nodeSet);
        return ReturnBean.ok(node.getId());
    }

    /**
     * 部署模型
     *
     * @return {@link ReturnBean}
     */
    @KrtLog("部署模型")
    @RequiresPermissions("act:model:deploy")
    @PostMapping("act/model/deploy")
    @ResponseBody
    public ReturnBean deploy(String modelId) {
        actService.deploy(modelId);
        return ReturnBean.ok();
    }

    /**
     * 验证模型参数
     *
     * @param model 模型
     */
    private void validate(ExtActModel model) {
        Assert.isNull(model.getExtBusinessId(), "关联业务不可为空");
        ExtActBusiness business = businessService.selectById(model.getExtBusinessId());
        boolean flag = ActConstant.ActBusType.BUS.getValue().equals(business.getType());
        Assert.validate(!flag, "关联业务类型错误");
        int count = modelService.selectCount(Wrappers.<ExtActModel>lambdaQuery()
                .eq(ExtActModel::getModelKey, business.getActKey())
                .ne(!Assert.isNull(model.getId()), ExtActModel::getId, model.getId())
        );
        Assert.validate(count > 0, "业务重复关联");
    }
}
