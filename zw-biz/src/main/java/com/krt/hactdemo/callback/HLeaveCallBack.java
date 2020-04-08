package com.krt.hactdemo.callback;

import com.krt.act.dto.ProcessTaskDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author :huangZB
 * @date 2020/4/6
 * @Description
 */
@Slf4j
public class HLeaveCallBack {

	public void hlBack(ProcessTaskDTO processTaskDTO){

		log.debug("processTaskDTO{}",processTaskDTO);
		log.debug("请假回调成功!");

	}
}
