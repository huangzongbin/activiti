package com.krt.hactdemo.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.actdemo.entity.TestLeave;
import com.krt.actdemo.service.ITestLeaveService;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.hactdemo.entity.HLeave;
import com.krt.hactdemo.service.IHLeaveService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.Map;

/**
 * 请假流程测试控制层
 *
 * @author hzb
 * @version 1.0
 * @date 2020年04月06日
 */
@Controller
public class HLeaveController extends BaseController {

    @Autowired
    private IHLeaveService hLeaveService;

    /**
     * 请假流程测试管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("hactDemon:hLeave:list")
    @GetMapping("hactdemo/hLeave/list")
    public String list() {
        return "hactdemo/hLeave/list";
    }

    /**
     * 请假流程测试管理
     *
     * @param para 搜索参数
     * @return {@link DataTable}
     */
    @RequiresPermissions("hactDemon:hLeave:list")
    @PostMapping("hactdemo/hLeave/list")
    @ResponseBody
    public DataTable list(@RequestParam Map para) {
        IPage page = hLeaveService.selectPageList(para);
        return DataTable.ok(page);
    }

    /**
     * 新增请假流程测试页
     *
     * @return {@link String}
     */
    @RequiresPermissions("hactDemon:hLeave:insert")
    @GetMapping("hactdemo/hLeave/insert")
    public String insert() {
        return "hactdemo/hLeave/insert";
    }

    /**
     * 添加请假流程测试
     *
     * @param hLeave 请假流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("添加请假流程测试")
    @RequiresPermissions("hactDemon:hLeave:insert")
    @PostMapping("hactdemo/hLeave/insert")
    @ResponseBody
    public ReturnBean insert(HLeave hLeave) {
        hLeaveService.insert(hLeave);
        return ReturnBean.ok();
    }

    /**
     * 修改请假流程测试页
     *
     * @param id 请假流程测试id
     * @return {@link String}
     */
    @RequiresPermissions("hactDemon:hLeave:update")
    @GetMapping("hactdemo/hLeave/update")
    public String update(Integer id) {
        HLeave hLeave = hLeaveService.selectById(id);
        request.setAttribute("hLeave", hLeave);
        return "hactdemo/hLeave/update";
    }

    /**
     * 修改请假流程测试
     *
     * @param hLeave 请假流程测试
     * @return {@link ReturnBean}
     */
    @KrtLog("修改请假流程测试")
    @RequiresPermissions("hactDemon:hLeave:update")
    @PostMapping("hactdemo/hLeave/update")
    @ResponseBody
    public ReturnBean update(HLeave hLeave) {
        hLeaveService.updateById(hLeave);
        return ReturnBean.ok();
    }

    /**
     * 删除请假流程测试
     *
     * @param id 请假流程测试id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除请假流程测试")
    @RequiresPermissions("hactDemon:hLeave:delete")
    @PostMapping("hactdemo/hLeave/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        hLeaveService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 批量删除请假流程测试
     *
     * @param ids 请假流程测试ids
     * @return {@link ReturnBean}
     */
    @KrtLog("批量删除请假流程测试")
    @RequiresPermissions("hactDemon:hLeave:delete")
    @PostMapping("hactdemo/hLeave/deleteByIds")
    @ResponseBody
    public ReturnBean deleteByIds(Integer[] ids) {
        hLeaveService.deleteByIds(Arrays.asList(ids));
        return ReturnBean.ok();
    }



    @GetMapping("hactdemo/hLeave/leave")
    public String leave(ProcessTaskDTO processTaskDTO) {

        HLeave hleave=hLeaveService.selectById(processTaskDTO.getBusId());
        request.setAttribute("hleave",hleave);
        request.setAttribute("processTaskDTO",processTaskDTO);
        return   "hactdemo/hLeave/hleave";
    }

}
