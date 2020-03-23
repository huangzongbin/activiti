package com.krt.biz.controller;

import com.krt.common.annotation.KrtLog;
import com.krt.common.base.BaseController;
import com.krt.common.bean.ReturnBean;
import com.krt.common.constant.GlobalConstant;
import com.krt.biz.entity.QltOrganDepartment;
import com.krt.biz.service.IQltOrganDepartmentService;
import com.krt.common.session.SessionUser;
import com.krt.common.util.ShiroUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内设机构设置控制层
 *
 * @author FuCongYuan
 * @version 1.0
 * @date 2019年03月14日
 */
@Controller
public class QltOrganDepartmentController extends BaseController {
    @Autowired
    private IQltOrganDepartmentService qltOrganDepartmentService;

    /**
     * 内设机构设置管理页
     *
     *  @return {@link String}
     */
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:list")
    @GetMapping("biz/qltOrganDepartment/list")
    public String list() {
        System.out.println("内设机构设置管理页");
        return "biz/qltOrganDepartment/list";
    }

    /**
     * 内设机构设置管理
     *
     * @param pid 父id
     * @return {@link ReturnBean}
     */
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:list")
    @PostMapping("biz/qltOrganDepartment/list")
    @ResponseBody
    public ReturnBean list(Integer pid) {
        //得到当前用户
        SessionUser sessionUser = ShiroUtils.getSessionUser();
        String organCode =sessionUser.getOrganCode();
        System.out.printf("得到当前用户organCode="+organCode);
        Map para=new HashMap();
        para.put("pid",pid);
        para.put("organId",organCode);
        //根据pid查询内设机构设置
        List list = qltOrganDepartmentService.selectByPid(para);
        return ReturnBean.ok(list);
    }

    /**
     * 新增内设机构设置页
     *
     * @return {@link String}
     */
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:insert")
    @GetMapping("biz/qltOrganDepartment/insert")
    public String insert(Integer pid) {
        if (pid != null) {
            QltOrganDepartment pQltOrganDepartment = qltOrganDepartmentService.selectById(pid);
            request.setAttribute("pQltOrganDepartment", pQltOrganDepartment);
            //生成机构编码
            Map par=new HashMap();
            par.put("code",pQltOrganDepartment.getCode());
            String  lastCode=  qltOrganDepartmentService.selectLastCode(par);
            ////取出字符后面的数字串
            if("0".equals(lastCode)){
                //如果取出结果是0
                lastCode=pQltOrganDepartment.getCode()+"01";
            }else{
                String begCode = lastCode.substring(0, lastCode.length() - 2);   //截取最前面的字符串
                String endCode = lastCode.substring(lastCode.length() - 2, lastCode.length());   //截取最后2位
                Long code = Long.valueOf(endCode) + 1;
                if(code<10){
                    lastCode = begCode + "0"+code;
                }else{
                    lastCode = begCode + code;
                }

                System.out.printf("||lastCode="+lastCode+"||begCode="+begCode+"||endCode="+endCode+"||code="+code+"||lastCode="+lastCode);
            }

            request.setAttribute("lastCode", lastCode);
        }

        return "biz/qltOrganDepartment/insert";
    }

    /**
     * 添加内设机构设置
     *
     * @param qltOrganDepartment 内设机构设置
     * @return {@link ReturnBean}
     */
    @KrtLog("添加内设机构设置")
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:insert")
    @PostMapping("biz/qltOrganDepartment/insert")
    @ResponseBody
    public ReturnBean insert(QltOrganDepartment qltOrganDepartment) {
        if (qltOrganDepartment.getPid() == null) {
            qltOrganDepartment.setPid(GlobalConstant.DEFAULT_PID);
        }
        qltOrganDepartmentService.insert(qltOrganDepartment);
        return ReturnBean.ok();
    }

    /**
     * 修改内设机构设置页
     *
     * @param id 内设机构设置 id
     * @return {@link String}
     */
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:update")
    @GetMapping("biz/qltOrganDepartment/update")
    public String update(Integer id) {
        QltOrganDepartment qltOrganDepartment = qltOrganDepartmentService.selectById(id);
        Integer pid = qltOrganDepartment.getPid();
        if (!GlobalConstant.DEFAULT_PID.equals(pid)) {
            QltOrganDepartment pQltOrganDepartment = qltOrganDepartmentService.selectById(pid);
            request.setAttribute("pName", pQltOrganDepartment.getName());
        }else{
            request.setAttribute("pName", "公司");
        }
        request.setAttribute("qltOrganDepartment", qltOrganDepartment);
        return "biz/qltOrganDepartment/update";
    }

    /**
     * 修改内设机构设置
     *
     * @param qltOrganDepartment 内设机构设置
     * @return {@link ReturnBean}
     */
    @KrtLog("修改内设机构设置")
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:update")
    @PostMapping("biz/qltOrganDepartment/update")
    @ResponseBody
    public ReturnBean update(QltOrganDepartment qltOrganDepartment) {
        qltOrganDepartmentService.updateById(qltOrganDepartment);
        return ReturnBean.ok();
    }

    /**
     * 删除内设机构设置
     *
     * @param id 内设机构设置 id
     * @return {@link ReturnBean}
     */
    @KrtLog("删除内设机构设置")
    @RequiresPermissions("qlt_organ_department:qltOrganDepartment:delete")
    @PostMapping("biz/qltOrganDepartment/delete")
    @ResponseBody
    public ReturnBean delete(Integer id) {
        qltOrganDepartmentService.deleteById(id);
        return ReturnBean.ok();
    }

    /**
     * 获取内设机构设置树数据
     *
     * @return {@link ReturnBean}
     */
    @PostMapping("biz/qltOrganDepartment/qltOrganDepartmentTree")
    @ResponseBody
    public ReturnBean qltOrganDepartmentTree() {
        //得到当前用户
        SessionUser sessionUser = ShiroUtils.getSessionUser();
        String organCode =sessionUser.getOrganCode();
        System.out.printf("=======得到当前用户organCode"+organCode+"sessionUser="+sessionUser);
        Map para=new HashMap();
        para.put("organId",organCode);
        List list = qltOrganDepartmentService.selectList(para);
        return ReturnBean.ok(list);
    }


}
