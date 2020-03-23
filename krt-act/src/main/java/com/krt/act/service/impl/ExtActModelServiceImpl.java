package com.krt.act.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.krt.act.entity.*;
import com.krt.act.mapper.ExtActModelMapper;
import com.krt.act.service.*;
import com.krt.common.base.BaseServiceImpl;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;


/**
 * 流程模板扩展服务接口实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
@Service
public class ExtActModelServiceImpl extends BaseServiceImpl<ExtActModelMapper, ExtActModel> implements IExtActModelService {

    @Autowired
    private IActService actModelerService;

    @Autowired
    private IExtActBusinessService extActBusinessService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IExtActNodeSetService nodeSetService;

    @Autowired
    private IExtActNodeFieldService nodeFieldService;

    @Autowired
    private IExtActNodeUserService nodeUserService;

    @Autowired
    private IExtActFlowBusinessService flowBusinessService;


    /**
     * 创建流程模板
     *
     * @param extActModel 流程模板
     */
    @Override
    public void createModeler(ExtActModel extActModel) {
        ExtActBusiness extActBusiness = extActBusinessService.selectById(extActModel.getExtBusinessId());
        extActModel.setModelKey(extActBusiness.getActKey());
        actModelerService.createModeler(extActModel);
    }

    /**
     * 根据 ID 删除
     *
     * @param id 数据库id
     * @return true:成功 false:失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Serializable id) {
        ExtActModel actModel = baseMapper.selectById(id);
        Model model = repositoryService.getModel(actModel.getModelId());
        if (!StringUtils.isEmpty(model.getDeploymentId())) {
            //删除部署表
            repositoryService.deleteDeployment(model.getDeploymentId(),true);
        }
        //删除模型表
        repositoryService.deleteModel(model.getId());
        //节点设置
        List<ExtActNodeSet> nodeList = nodeSetService.selectList(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getModelId,model.getId()));
        if(nodeList!=null && nodeList.size()>0){
            //删除节点
            nodeSetService.delete(Wrappers.<ExtActNodeSet>lambdaQuery().eq(ExtActNodeSet::getModelId,model.getId()));
            for(ExtActNodeSet nodeSet:nodeList){
                //节点字段
                nodeFieldService.delete(Wrappers.<ExtActNodeField>lambdaQuery().eq(ExtActNodeField::getNodeId,nodeSet.getNodeId()));
                //节点审批人员
                nodeUserService.delete(Wrappers.<ExtActNodeUser>lambdaQuery().eq(ExtActNodeUser::getNodeId,nodeSet.getNodeId()));
            }
        }
        flowBusinessService.delete(Wrappers.<ExtActFlowBusiness>lambdaQuery().eq(ExtActFlowBusiness::getActKey,actModel.getModelKey()));
        return super.deleteById(id);
    }

}
