package com.krt.actdemo.callback;

import com.krt.act.dto.ProcessTaskDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * 请假回调
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Slf4j
public class TestLeaveCallBack {

    public void leaveBack(ProcessTaskDTO processTaskDTO) {
        log.debug("processTaskDTO:{}",processTaskDTO);
        log.debug("请假回调成功啦！！！！！！！");
    }
}
