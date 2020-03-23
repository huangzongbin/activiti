package com.krt.sx.callback;

import com.krt.act.dto.ProcessTaskDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 报销回调
 *
 * @author fcy
 * @version 1.0
 * @date 2019年03月26日
 */
@Slf4j
public class TestPxCallBack {

    public void pxBack(ProcessTaskDTO processTaskDTO) {
        log.debug("processTaskDTO:{}",processTaskDTO);
        log.debug("报销回调成功啦！！！！！！！");
    }
}
