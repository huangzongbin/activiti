package com.krt.act.service;

import com.krt.act.entity.ExtActNodeSet;
import com.krt.common.base.IBaseService;


/**
 * 流程节点配置服务接口层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
public interface IExtActNodeSetService extends IBaseService<ExtActNodeSet> {

    /**
     * 保存节点设置
     *
     * @param nodeSet 节点设置
     * @return ExtActNodeSet 节点设置
     * @throws Exception 异常
     */
    ExtActNodeSet saveNode(ExtActNodeSet nodeSet) throws Exception;
}
