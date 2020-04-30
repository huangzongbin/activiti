package com.krt.act.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.krt.act.dto.ProcessTaskDTO;
import com.krt.act.entity.ExtActBusiness;
import com.krt.act.service.IExtActBusinessService;
import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.ReturnBean;
import com.krt.common.constant.ActConstant;
import com.krt.common.constant.GlobalConstant;
import com.krt.common.exception.KrtException;
import com.krt.common.validator.Assert;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 业务流程  对应的 业务表控制层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
@Controller
public class ExtActBusinessController extends BaseController {

    @Autowired
    private IExtActBusinessService businessService;

    /**
     * 业务流程  对应的 业务表管理页
     *
     * @return {@link String}
     */
    @RequiresPermissions("act:business:list")
    @GetMapping("act/business/list")
    public String list() {
        return "act/business/list";
    }

    /**
     * 业务流程  对应的 业务表管理
     *
     * @param pid 父id
     * @return {@link ReturnBean}
     */
    @RequiresPermissions("act:business:list")
    @PostMapping("act/business/list")
    @ResponseBody
    public ReturnBean list(Integer pid) {
        List list = businessService.selectByPid(pid);
        return ReturnBean.ok(list);
    }

    /**
     * 新增业务流程  对应的 业务表页
     *
     * @return {@link String}
     */
    @RequiresPermissions("act:business:insert")
    @GetMapping("act/business/insert")
    public String insert(Integer pid) {
        if (pid != null) {
            ExtActBusiness business = businessService.selectById(pid);
            request.setAttribute("pBusiness", business);
        }
        return "act/business/insert";
    }

    /**
     * 添加业务流程  对应的 业务表
     *
     * @param business 业务流程  对应的 业务表
     * @return {@link ReturnBean}
     */
    @KrtLog("添加业务流程  对应的 业务表")
    @RequiresPermissions("act:business:insert")
    @PostMapping("act/business/insert")
    @ResponseBody
    public ReturnBean insert(ExtActBusiness business) {
        validate(business);
        if (business.getPid() == null) {
            business.setPid(GlobalConstant.DEFAULT_PID);
        }
        businessService.insert(business);
        return ReturnBean.ok();
    }

    /**
     * 修改业务流程  对应的 业务表页
     *
     * @param id 业务流程  对应的 业务表 id
     * @return {@link String}
     */
    @RequiresPermissions("act:business:update")
    @GetMapping("act/business/update")
    public String update(Integer id) {
        ExtActBusiness business = businessService.selectById(id);
        Integer pid = business.getPid();
        if (!GlobalConstant.DEFAULT_PID.equals(pid)) {
            ExtActBusiness pBusiness = businessService.selectById(pid);
            request.setAttribute("pBusiness", pBusiness);
        }
        request.setAttribute("business", business);
        return "act/business/update";
    }

    /**
     * 修改业务流程  对应的 业务表
     *
     * @param business 业务流程  对应的 业务表
     * @return {@link ReturnBean}
     */
    @KrtLog("修改业务流程  对应的 业务表")
    @RequiresPermissions("act:business:update")
    @PostMapping("act/business/update")
    @ResponseBody
    public ReturnBean update(ExtActBusiness business) {
        validate(business);
        businessService.updateById(business);
        return ReturnBean.ok();
    }

    /**
     * 删除业务流程  对应的 业务表
     *
     * @param id 业务流程  对应的 业务表 id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除业务流程  对应的 业务表")
    @RequiresPermissions("act:business:delete")
    @PostMapping("act/business/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        businessService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 获取业务流程  对应的 业务表树数据
     *
     * @return {@link ReturnBean}
     */
    @PostMapping("act/business/businessTree")
    @ResponseBody
    public ReturnBean businessTree() {
        List list = businessService.selectList();
        return ReturnBean.ok(list);
    }


    /**
     * 获取业务流程  对应的 业务表树数据
     *
     * @return {@link ReturnBean}
     */
    @PostMapping("act/business/businessTreeData")
    @ResponseBody
    public ReturnBean businessTreeData() {
        List list = businessService.selectList(Wrappers.<ExtActBusiness>lambdaQuery()
                .ne(ExtActBusiness::getType, ActConstant.ActBusType.BACK.getValue()));
        return ReturnBean.ok(list);
    }

    /**
     * 参数验证
     *
     * @param business 流程业务
     */
    private void validate(ExtActBusiness business) {
        Assert.isBlank(business.getType(), "业务类型不可为空");
        if (ActConstant.ActBusType.BUS.getValue().equals(business.getType())) {
            Assert.isBlank(business.getClassUrl(), "业务类不可为空");
            try {
                Class.forName(business.getClassUrl());
            } catch (ClassNotFoundException e) {
                throw new KrtException(ReturnBean.error("业务类不存在"));
            }
            Assert.isBlank(business.getActKey(), "流程key不可为空");
            int count = businessService.selectCount(Wrappers.<ExtActBusiness>lambdaQuery()
                    .eq(ExtActBusiness::getActKey, business.getActKey())
                    .ne(!Assert.isNull(business.getId()), ExtActBusiness::getId, business.getId())
            );
            Assert.validate(count > 0, "流程key已存在");
        }
        if (ActConstant.ActBusType.BACK.getValue().equals(business.getType())) {
            Assert.isBlank(business.getClassUrl(), "回调方法路径不可为空");
            String callBack = business.getClassUrl();
            int lastIndex = callBack.lastIndexOf(GlobalConstant.POINT);
            //方法名
            String methodStr = callBack.substring(lastIndex + 1);
            //类路径
            String classUrl = callBack.substring(0, lastIndex);
            try {
                //反射验证
                Class<?> clazz = Class.forName(classUrl);
                clazz.getMethod(methodStr, ProcessTaskDTO.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                throw new KrtException(ReturnBean.error("回调方法不存在"));
            }
        }
    }
}
