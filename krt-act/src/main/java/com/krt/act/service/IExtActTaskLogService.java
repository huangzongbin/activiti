package com.krt.act.service;

import com.krt.act.entity.ExtActTaskLog;
import com.krt.common.base.IBaseService;

import java.util.List;


/**
 * 任务日志服务接口层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
public interface IExtActTaskLogService extends IBaseService<ExtActTaskLog> {

    /**
     * 查询办理记录
     *
     * @param busId      业务id
     * @param instanceId 流程实例id
     * @return {@link List<ExtActTaskLog>}
     */
    List<ExtActTaskLog> selectTaskLogList(Integer busId, String instanceId);
}
