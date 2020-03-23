package com.krt.act.service.impl;

import com.krt.act.entity.ExtActTaskLog;
import com.krt.act.mapper.ExtActTaskLogMapper;
import com.krt.act.service.IExtActTaskLogService;
import com.krt.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 任务日志服务接口实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
@Service
public class ExtActTaskLogServiceImpl extends BaseServiceImpl<ExtActTaskLogMapper, ExtActTaskLog> implements IExtActTaskLogService {

    /**
     * 查询办理记录
     *
     * @param busId      业务id
     * @param instanceId 流程实例id
     * @return {@link List <ExtActTaskLog>}
     */
    @Override
    public List<ExtActTaskLog> selectTaskLogList(Integer busId, String instanceId) {
        return baseMapper.selectTaskLogList(busId, instanceId);
    }
}
