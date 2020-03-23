package com.krt.act.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.krt.common.constant.ActConstant;
import com.krt.act.entity.ExtActNodeButton;
import com.krt.act.entity.ExtActNodeField;
import com.krt.act.entity.ExtActNodeSet;
import com.krt.act.entity.ExtActNodeUser;
import com.krt.act.mapper.ExtActNodeSetMapper;
import com.krt.act.service.IExtActNodeButtonService;
import com.krt.act.service.IExtActNodeFieldService;
import com.krt.act.service.IExtActNodeSetService;
import com.krt.act.service.IExtActNodeUserService;
import com.krt.common.util.ActUtils;
import com.krt.common.base.BaseServiceImpl;
import com.krt.common.validator.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * 流程节点配置服务接口实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Slf4j
@Service
public class ExtActNodeSetServiceImpl extends BaseServiceImpl<ExtActNodeSetMapper, ExtActNodeSet> implements IExtActNodeSetService {

    @Autowired
    private IExtActNodeUserService nodeUserService;

    @Autowired
    private IExtActNodeFieldService nodeFieldService;

    @Autowired
    private IExtActNodeButtonService nodeButtonService;

    /**
     * 保存节点设置
     *
     * @param nodeSet 节点设置
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExtActNodeSet saveNode(ExtActNodeSet nodeSet) throws Exception {
        //节点类型为审批节点
        if (nodeSet.getNodeType().equals(ActConstant.NodeType.EXAMINE.getValue())) {
            nodeSet = saveExamineNode(nodeSet);
        }
        //分支条件连线
        if (nodeSet.getNodeType().equals(ActConstant.NodeType.LINE.getValue())) {
            nodeSet = saveLineNode(nodeSet);
        }
        //节点类型为结束
        if (nodeSet.getNodeType().equals(ActConstant.NodeType.END.getValue())) {
            insertOrUpdate(nodeSet);
        }
        return nodeSet;
    }

    /**
     * 保存审批节点
     *
     * @param nodeSet 节点信息
     */
    private ExtActNodeSet saveExamineNode(ExtActNodeSet nodeSet) {
        if (Assert.isNull(nodeSet.getId())) {
            //保存节点信息
            insert(nodeSet);
        } else {
            //更新
            update(nodeSet, Wrappers.<ExtActNodeSet>lambdaUpdate()
                    .set(Assert.isBlank(nodeSet.getChangeField()), ExtActNodeSet::getChangeField, null)
                    .eq(ExtActNodeSet::getId, nodeSet.getId()));
            //保存审批用户 先根据nodeId删除节点相关的审批用户
            nodeUserService.delete(Wrappers.<ExtActNodeUser>lambdaQuery().eq(ExtActNodeUser::getNodeId, nodeSet.getNodeId()));
            //删除审批按钮
            nodeButtonService.delete(Wrappers.<ExtActNodeButton>lambdaQuery().eq(ExtActNodeButton::getNodeId, nodeSet.getNodeId()));
        }
        //保存审批用户
        String[] userTypes = nodeSet.getUserTypes();
        Integer[] userIds = nodeSet.getUserIds();
        List<ExtActNodeUser> list = new ArrayList<>();
        ExtActNodeUser nodeUser;
        if (userIds != null && userIds.length > 0) {
            for (int i = 0; i < userIds.length; i++) {
                nodeUser = new ExtActNodeUser();
                nodeUser.setUserId(userIds[i]);
                nodeUser.setUserType(userTypes[i]);
                nodeUser.setNodeId(nodeSet.getNodeId());
                list.add(nodeUser);
            }
            nodeUserService.insertBatch(list);
        }
        //保存审批按钮
        String[] buttonCodes = nodeSet.getButtonCodes();
        String[] buttonNames = nodeSet.getButtonNames();
        String[] callBacks = nodeSet.getCallBacks();
        List<ExtActNodeButton> btnList = new ArrayList<>();
        ExtActNodeButton nodeButton;
        if (buttonCodes != null && buttonCodes.length > 0) {
            for (int i = 0; i < buttonCodes.length; i++) {
                nodeButton = new ExtActNodeButton();
                nodeButton.setButtonCode(buttonCodes[i]);
                nodeButton.setButtonName(buttonNames[i]);
                nodeButton.setCallback(callBacks[i]);
                nodeButton.setNodeId(nodeSet.getNodeId());
                btnList.add(nodeButton);
            }
            nodeButtonService.insertBatch(btnList);
        }
        return nodeSet;
    }

    /**
     * 保存审批节点
     *
     * @param nodeSet 节点信息
     */
    private ExtActNodeSet saveLineNode(ExtActNodeSet nodeSet) throws Exception {
        //保存
        if (Assert.isNull(nodeSet.getId())) {
            //保存节点信息
            insert(nodeSet);
        } else {
            //更新
            updateById(nodeSet);
            //根据nodeId删除所有节点对应的连线条件
            nodeFieldService.delete(Wrappers.<ExtActNodeField>lambdaQuery().eq(ExtActNodeField::getNodeId, nodeSet.getNodeId()));
        }
        //el条件 例如${day>3 && isagree==1}
        StringBuilder condition = new StringBuilder("${");
        if (nodeSet.getJudgeList() != null && nodeSet.getJudgeList().size() > 0) {
            List<ExtActNodeField> judgeList = new ArrayList<>();
            int sort = 0;
            for (ExtActNodeField nodeField : nodeSet.getJudgeList()) {
                if (StringUtils.isEmpty(nodeField.getFieldName()) || StringUtils.isEmpty(nodeField.getRule())) {
                    continue;
                }
                if (!StringUtils.isEmpty(nodeField.getElOperator())) {
                    condition.append(" ").append(nodeField.getElOperator()).append(" ");
                }
                if ("==".equals(nodeField.getRule())) {
                    //中文等于 要 + ''
                    condition.append(nodeField.getFieldName()).append(nodeField.getRule()).append("'").append(nodeField.getFieldVal()).append("'");
                } else {
                    condition.append(nodeField.getFieldName()).append(nodeField.getRule()).append(nodeField.getFieldVal());
                }
                nodeField.setNodeId(nodeSet.getNodeId());
                nodeField.setFieldType(ActConstant.FieldType.JUDG.getValue());
                nodeField.setSort(sort);
                sort++;
                judgeList.add(nodeField);
            }
            nodeFieldService.insertBatch(judgeList);
        }
        String judge = condition.append("}").toString();
        log.debug("节点条件 judge:{}", judge);
        //添加条件
        ActUtils.setSequenceFlowCondition(nodeSet.getModelId(), nodeSet.getNodeId(), judge);
        return nodeSet;
    }

}
