package com.krt.common.editor;

import com.alibaba.fastjson.JSONObject;
import org.activiti.engine.ActivitiException;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.InputStream;

/**
 * @author 殷帅
 * @version 1.0
 * @Description: StencilsetRestResource控制层
 * @date 2017年09月02日
 */
@Controller
public class StencilsetRestResource {

    @GetMapping(value = "/service/editor/stencilset")
    @ResponseBody
    public JSONObject getStencilset() {
        try {
            InputStream stencilsetStream = new ClassPathResource("stencilset_zh-cn.json").getInputStream();
            String str = IOUtils.toString(stencilsetStream, "utf-8");
            return JSONObject.parseObject(str);
        } catch (Exception e) {
            throw new ActivitiException("Error while loading stencil set", e);
        }
    }
}