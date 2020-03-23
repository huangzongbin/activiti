package com.krt.act.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.krt.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.repository.Model;

/**
 * 流程模板扩展实体类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("ext_act_model")
public class ExtActModel extends BaseEntity {

    /**
     * 流程key
     */
    private String modelKey;

    /**
     * 名称
     */
    private String modelName;

    /**
     * 描述
     */
    private String description;

    /**
     * 发布状态 0:已 1：未发布
     */
    private String status;

    /**
     * 版本号
     */
    private Integer modelVersion;

    /**
     * 业务id
     */
    private Integer extBusinessId;

    /**
     * activiti中的模型表id
     */
    private String modelId;

    /**
     * activiti中的部署表id
     */
    private String deploymentId;

    /**
     * 获取 ObjectNode
     *
     * @param objectMapper 转换
     * @param model        模型
     * @return ObjectNode 模型元信息
     */
    public ObjectNode getMetaNode(ObjectMapper objectMapper, Model model) {
        ObjectNode metaNode = objectMapper.createObjectNode();
        metaNode.put(ModelDataJsonConstants.MODEL_NAME, getModelName());
        metaNode.put(ModelDataJsonConstants.MODEL_REVISION, model.getVersion());
        metaNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, getDescription());
        return metaNode;
    }

}