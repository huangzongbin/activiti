package com.krt.common.util;

import com.krt.common.annotation.ActField;
import com.krt.common.bean.ReturnBean;
import com.krt.common.exception.KrtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 注解工具类
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月22日
 */
public class AnnotationUtils {

    /**
     * 通过类路径,通过反射获取类属性上的ActField注解信息,在获取属性时，
     * 最多支持向上获取两层父类属性,如需要可自行添加
     *
     * @param classUrl 实体类地址
     * @return List key:keyName 属性名 key:actField 属性注解
     */
    public static List<Map<String, Object>> getActFieldByClazz(String classUrl) {
        if (StringUtils.isEmpty(classUrl)) {
            throw new KrtException(ReturnBean.error("实体类不能为空"));
        }
        List<Map<String, Object>> list = new ArrayList<>();
        //获取当前类以及父类中属性
        ActField actField;
        try {
            Class clazz = Class.forName(classUrl);
            PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(clazz);
            for (PropertyDescriptor sourcePd1 : sourcePds) {
                Map<String, Object> reMap = new HashMap<>(16);
                if (sourcePd1 != null && sourcePd1.getWriteMethod() != null) {
                    try {
                        String keyName = sourcePd1.getName();
                        try {
                            actField = clazz.getDeclaredField(keyName).getAnnotation(ActField.class);
                        } catch (NoSuchFieldException e) {
                            try {
                                actField = clazz.getSuperclass().getDeclaredField(keyName).getAnnotation(ActField.class);
                            } catch (NoSuchFieldException el) {
                                actField = clazz.getSuperclass().getSuperclass().getDeclaredField(keyName).getAnnotation(ActField.class);
                            }
                        }
                        if (actField != null) {
                            reMap.put("actField", actField);
                            reMap.put("keyName", keyName);
                            list.add(reMap);
                        }
                    } catch (Throwable ex) {
                        throw new FatalBeanException("对象获取属性失败!", ex);
                    }
                }
            }
        } catch (Exception e1) {
            throw new KrtException(ReturnBean.error("获取业务实体数据失败"));
        }
        return list;
    }


}
