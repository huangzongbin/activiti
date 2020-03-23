package com.krt.act.dto;

import lombok.Data;

/**
 * 流程节点dto
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月28日
 */
@Data
public class ProcessNodeDTO {

    /**
     * 结点id
     */
    private String nodeId;

    /**
     * 节点名字
     */
    private String nodeName;

    /**
     * 节点类型 =1开始节点 2=审批节点 3=分支 4=连线 5=结束节点 6=并发节点
     */
    private String nodeType;

    /**
     * 节点行为 1=审批 2=会签
     */
    private String nodeAction;

    /**
     * 节点行为名字
     */
    private String nodeActionName;

    /**
     * 节点类型名字
     */
    private String nodeTypeName;
}
