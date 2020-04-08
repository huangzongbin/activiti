package com.krt.hactdemo.service.impl;

import org.springframework.stereotype.Service;
import com.krt.hactdemo.entity.HLeave;
import com.krt.hactdemo.mapper.HLeaveMapper;
import com.krt.hactdemo.service.IHLeaveService;
import com.krt.common.base.BaseServiceImpl;


/**
 * 请假流程测试服务接口实现层
 *
 * @author hzb
 * @version 1.0
 * @date 2020年04月06日
 */
@Service
public class HLeaveServiceImpl extends BaseServiceImpl<HLeaveMapper, HLeave> implements IHLeaveService {

}
