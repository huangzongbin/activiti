package com.krt.sx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.DataTable;
import com.krt.common.bean.ReturnBean;
import com.krt.sx.entity.ZCurrentItem;
import com.krt.sx.service.IZCurrentItemService;
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
 * 当前使用的事项信息控制层
 *
 * @author 李祥
 * @version 1.0
 * @date 2019年04月23日
 */
@Controller
public class ZCurrentItemController extends BaseController {

    @Autowired
    private IZCurrentItemService zCurrentItemService;

    /**
     * 当前使用的事项信息管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("z_current_item:zCurrentItem:list")
    @GetMapping("sx/zCurrentItem/list")
    public String list() {
        return "sx/zCurrentItem/list";
    }

    /**
     * 当前使用的事项信息管理
     *
     * @param para 搜索参数
     * @return {@link DataTable}
     */
    @RequiresPermissions("z_current_item:zCurrentItem:list")
    @PostMapping("sx/zCurrentItem/list")
    @ResponseBody
    public DataTable list(@RequestParam Map para) {
        IPage page = zCurrentItemService.selectPageList(para);
        return DataTable.ok(page);
    }

    /**
     * 新增当前使用的事项信息页
     *
     * @return {@link String}
     */
    @RequiresPermissions("z_current_item:zCurrentItem:insert")
    @GetMapping("sx/zCurrentItem/insert")
    public String insert() {
        return "sx/zCurrentItem/insert";
    }

    /**
     * 添加当前使用的事项信息
     *
     * @param zCurrentItem 当前使用的事项信息
     * @return {@link ReturnBean}
     */
    @KrtLog("添加当前使用的事项信息")
    @RequiresPermissions("z_current_item:zCurrentItem:insert")
    @PostMapping("sx/zCurrentItem/insert")
    @ResponseBody
    public ReturnBean insert(ZCurrentItem zCurrentItem) {
        zCurrentItemService.insert(zCurrentItem);
        return ReturnBean.ok();
    }

    /**
     * 修改当前使用的事项信息页
     *
     * @param id 当前使用的事项信息id
     * @return {@link String}
     */
    @RequiresPermissions("z_current_item:zCurrentItem:update")
    @GetMapping("sx/zCurrentItem/update")
    public String update(Integer id) {
        ZCurrentItem zCurrentItem = zCurrentItemService.selectById(id);
        request.setAttribute("zCurrentItem", zCurrentItem);
        return "sx/zCurrentItem/update";
    }

    /**
     * 修改当前使用的事项信息
     *
     * @param zCurrentItem 当前使用的事项信息
     * @return {@link ReturnBean}
     */
    @KrtLog("修改当前使用的事项信息")
    @RequiresPermissions("z_current_item:zCurrentItem:update")
    @PostMapping("sx/zCurrentItem/update")
    @ResponseBody
    public ReturnBean update(ZCurrentItem zCurrentItem) {
        zCurrentItemService.updateById(zCurrentItem);
        return ReturnBean.ok();
    }

    /**
     * 删除当前使用的事项信息
     *
     * @param id 当前使用的事项信息id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除当前使用的事项信息")
    @RequiresPermissions("z_current_item:zCurrentItem:delete")
    @PostMapping("sx/zCurrentItem/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        zCurrentItemService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 批量删除当前使用的事项信息
     *
     * @param ids 当前使用的事项信息ids
     * @return {@link ReturnBean}
     */
    @KrtLog("批量删除当前使用的事项信息")
    @RequiresPermissions("z_current_item:zCurrentItem:delete")
    @PostMapping("sx/zCurrentItem/deleteByIds")
    @ResponseBody
    public ReturnBean deleteByIds(Integer[] ids) {
        zCurrentItemService.deleteByIds(Arrays.asList(ids));
        return ReturnBean.ok();
    }


}
