package com.krt.act.service;

import com.krt.act.entity.ExtActModel;
import com.krt.common.base.IBaseService;


/**
 * 流程模板扩展服务接口层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
public interface IExtActModelService extends IBaseService<ExtActModel> {

    /**
     * 创建流程模板
     *
     * @param model 流程模板
     */
    void createModeler(ExtActModel model);

}
