package com.krt.common.callback;

import com.krt.act.dto.ProcessTaskDTO;
import com.krt.act.service.IActService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流公共回调
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年04月04日
 */
@Slf4j
@Component
public class ActCallback {

    @Autowired
    private IActService actService;

    /**
     * 执行公共回调
     *
     * @param processTaskDTO 流程任务DTO
     */
    public void callback(ProcessTaskDTO processTaskDTO) {
        log.debug("执行公共回调改变业务{}状态:{}", processTaskDTO.getTableName(), processTaskDTO.getCallback());
        actService.updateBizStatus(processTaskDTO.getTableName(), processTaskDTO.getBusId(), processTaskDTO.getCallback());
    }
}
