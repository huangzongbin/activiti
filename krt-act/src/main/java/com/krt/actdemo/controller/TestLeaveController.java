package com.krt.actdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.krt.actdemo.entity.TestLeave;
import com.krt.actdemo.service.ITestLeaveService;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.common.constant.GlobalConstant;
import com.krt.common.session.SessionUser;
import com.krt.common.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

/**
 * 请假流程测试控制层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月26日
 */
@Controller
public class TestLeaveController extends BaseController {

    @Autowired
    private ITestLeaveService testLeaveService;

    /**
     * 请假流程测试管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("actdemo:testLeave:list")
    @GetMapping("actdemo/testLeave/list")
    public String list() {
        return "actdemo/testLeave/list";
    }

    /**
     * 请假流程测试管理
     *
     * @param para 搜索参数
     * @return {@link DataTable}
     */
    @RequiresPermissions("actdemo:testLeave:list")
    @PostMapping("actdemo/testLeave/list")
    @ResponseBody
    public DataTable list(@RequestParam Map para) {
        SessionUser user = ShiroUtils.getSessionUser();
        if (!user.getUsername().equals(GlobalConstant.ADMIN)) {
            para.put("inserter", user.getId());
        }
        IPage page = testLeaveService.selectPageList(para);
        return DataTable.ok(page);
    }

    /**
     * 新增请假流程测试页
     *
     * @return {@link String}
     */
    @RequiresPermissions("actdemo:testLeave:insert")
    @GetMapping("actdemo/testLeave/insert")
    public String insert() {
        return "actdemo/testLeave/insert";
    }

    /**
     * 添加请假流程测试
     *
     * @param testLeave 请假流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("添加请假流程测试")
    @RequiresPermissions("actdemo:testLeave:insert")
    @PostMapping("actdemo/testLeave/insert")
    @ResponseBody
    public ReturnBean insert(TestLeave testLeave) {
        testLeaveService.insert(testLeave);
        return ReturnBean.ok();
    }

    /**
     * 修改请假流程测试页
     *
     * @param id 请假流程测试id
     * @return {@link String}
     */
    @RequiresPermissions("actdemo:testLeave:update")
    @GetMapping("actdemo/testLeave/update")
    public String update(Integer id) {
        TestLeave testLeave = testLeaveService.selectById(id);
        request.setAttribute("testLeave", testLeave);
        return "actdemo/testLeave/update";
    }

    /**
     * 修改请假流程测试
     *
     * @param testLeave 请假流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("修改请假流程测试")
    @RequiresPermissions("actdemo:testLeave:update")
    @PostMapping("actdemo/testLeave/update")
    @ResponseBody
    public ReturnBean update(TestLeave testLeave) {
        testLeaveService.updateById(testLeave);
        return ReturnBean.ok();
    }

    /**
     * 删除请假流程测试
     *
     * @param id 请假流程测试id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除请假流程测试")
    @RequiresPermissions("actdemo:testLeave:delete")
    @PostMapping("actdemo/testLeave/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        testLeaveService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 批量删除请假流程测试
     *
     * @param ids 请假流程测试ids
     * @return {@link ReturnBean}
     */
    @KrtLog("批量删除请假流程测试")
    @RequiresPermissions("actdemo:testLeave:delete")
    @PostMapping("actdemo/testLeave/deleteByIds")
    @ResponseBody
    public ReturnBean deleteByIds(Integer[] ids) {
        testLeaveService.deleteByIds(Arrays.asList(ids));
        return ReturnBean.ok();
    }


}
