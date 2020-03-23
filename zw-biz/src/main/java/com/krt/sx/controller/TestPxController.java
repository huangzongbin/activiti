package com.krt.sx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.sx.entity.TestPx;
import com.krt.sx.service.ITestPxService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Map;

/**
 * 报销流程测试控制层
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年04月18日
 */
@Controller
public class TestPxController extends BaseController {

    @Autowired
    private ITestPxService testPxService;

    /**
     * 报销流程测试管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("test_px:testPx:list")
    @GetMapping("sx/testPx/list")
    public String list() {
        return "sx/testPx/list";
    }

    /**
     * 报销流程测试管理
     *
     * @param para 搜索参数
     * @return {@link DataTable}
     */
    @RequiresPermissions("test_px:testPx:list")
    @PostMapping("sx/testPx/list")
    @ResponseBody
    public DataTable list(@RequestParam Map para) {
        IPage page = testPxService.selectPageList(para);
        return DataTable.ok(page);
    }

    /**
     * 新增报销流程测试页
     *
     * @return {@link String}
     */
    @RequiresPermissions("test_px:testPx:insert")
    @GetMapping("sx/testPx/insert")
    public String insert() {
        return "sx/testPx/insert";
    }

    /**
     * 添加报销流程测试
     *
     * @param testPx 报销流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("添加报销流程测试")
    @RequiresPermissions("test_px:testPx:insert")
    @PostMapping("sx/testPx/insert")
    @ResponseBody
    public ReturnBean insert(TestPx testPx) {
        testPxService.insert(testPx);
        return ReturnBean.ok();
    }

    /**
     * 修改报销流程测试页
     *
     * @param id 报销流程测试id
     * @return {@link String}
     */
    @RequiresPermissions("test_px:testPx:update")
    @GetMapping("sx/testPx/update")
    public String update(Integer id) {
        TestPx testPx = testPxService.selectById(id);
        request.setAttribute("testPx", testPx);
        return "sx/testPx/update";
    }

    /**
     * 修改报销流程测试
     *
     * @param testPx 报销流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("修改报销流程测试")
    @RequiresPermissions("test_px:testPx:update")
    @PostMapping("sx/testPx/update")
    @ResponseBody
    public ReturnBean update(TestPx testPx) {
        testPxService.updateById(testPx);
        return ReturnBean.ok();
    }

    /**
     * 删除报销流程测试
     *
     * @param id 报销流程测试id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除报销流程测试")
    @RequiresPermissions("test_px:testPx:delete")
    @PostMapping("sx/testPx/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        testPxService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 批量删除报销流程测试
     *
     * @param ids 报销流程测试ids
     * @return {@link ReturnBean}
     */
    @KrtLog("批量删除报销流程测试")
    @RequiresPermissions("test_px:testPx:delete")
    @PostMapping("sx/testPx/deleteByIds")
    @ResponseBody
    public ReturnBean deleteByIds(Integer[] ids) {
        testPxService.deleteByIds(Arrays.asList(ids));
        return ReturnBean.ok();
    }

    /**
     * 普通任务表单
     * @param processTaskDTO
     * @return
     */
    //@RequiresPermissions("test_px:update")
    @GetMapping("sx/testPx/doTestPx")
    public String doTestPx(ProcessTaskDTO processTaskDTO) {

        System.out.println("=================processTaskDTO.getBusId()="+processTaskDTO.getBusId());

        TestPx testPx = testPxService.selectById(processTaskDTO.getBusId());
        request.setAttribute("testPx", testPx);
        request.setAttribute("processTaskDTO", processTaskDTO);
        return "sx/testPx/doTestPx";
    }

}
