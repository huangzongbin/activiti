package com.krt.actdemo.service.impl;

import com.krt.common.constant.ActConstant;
import com.krt.actdemo.entity.TestLeave;
import com.krt.actdemo.mapper.TestLeaveMapper;
import com.krt.actdemo.service.ITestLeaveService;
import com.krt.common.base.BaseServiceImpl;
import com.krt.common.session.SessionUser;
import com.krt.common.util.ShiroUtils;
import com.krt.sys.service.INumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 请假流程测试服务接口实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Slf4j
@Service
public class TestLeaveServiceImpl extends BaseServiceImpl<TestLeaveMapper, TestLeave> implements ITestLeaveService {

    @Autowired
    private INumberService numberService;

    /**
     * 插入一条记录
     *
     * @param testLeave 实体
     * @return true:成功 false:失败
     */
    @Override
    public boolean insert(TestLeave testLeave) {
        log.debug("############  设置工作流公共部分  ################");
        String code = numberService.createNum("LEAVE");
        testLeave.setCode(code);
        testLeave.setStatus(ActConstant.ActStauts.DRAFT.getValue());

        log.debug("############  设置业务  ################");
        SessionUser user = ShiroUtils.getSessionUser();
        assert user != null;
        testLeave.setUserId(user.getId());
        testLeave.setUserName(user.getName());
        return super.insert(testLeave);
    }
}
