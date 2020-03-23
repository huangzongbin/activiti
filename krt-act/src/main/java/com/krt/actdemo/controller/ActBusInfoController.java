package com.krt.actdemo.controller;

import com.krt.act.dto.ProcessTaskDTO;
import com.krt.actdemo.entity.TestLeave;
import com.krt.actdemo.service.ITestLeaveService;
import com.krt.common.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 流程相关的业务根据业务id查询公共类，路径为actKey，也就是业务key
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
@RequestMapping("act/busInfo")
@Controller
public class ActBusInfoController extends BaseController {

    @Autowired
    private ITestLeaveService testLeaveService;

    @GetMapping(value = "leave")
    public String leave(ProcessTaskDTO processTaskDTO) {
        TestLeave testLeave = testLeaveService.selectById(processTaskDTO.getBusId());
        request.setAttribute("testLeave", testLeave);
        request.setAttribute("processTaskDTO", processTaskDTO);
        return "actdemo/testLeave/testLeaveInfo";
    }
}
