package com.krt.act.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.krt.common.annotation.ActField;
import com.krt.act.entity.ExtActBusiness;
import com.krt.act.mapper.ExtActBusinessMapper;
import com.krt.act.service.IExtActBusinessService;
import com.krt.common.util.AnnotationUtils;
import com.krt.common.base.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务流程  对应的 业务表实现层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月25日
 */
@Service
public class ExtActBusinessServiceImpl extends BaseServiceImpl<ExtActBusinessMapper, ExtActBusiness> implements IExtActBusinessService {

    /**
     * 根据pid查询业务流程  对应的 业务表
     *
     * @param pid 业务流程  对应的 业务表pid
     * @return {@link List<ExtActBusiness>}
     */
    @Override
    public List<ExtActBusiness> selectByPid(Integer pid) {
        return baseMapper.selectByPid(pid);
    }

    /**
     * 根据extend_act_model中的modelid查询对应的业务
     *
     * @param modelId 模型id
     * @return ExtActBusiness
     */
    @Override
    public ExtActBusiness selectActBusByModelId(String modelId) {
        ExtActBusiness extActBusiness = baseMapper.selectActBusByModelId(modelId);
        //业务实体类
        String classUrl = extActBusiness.getClassUrl();
        //可写
        List<Map<String, Object>> writes = new ArrayList<>();
        //可设置为条件
        List<Map<String, Object>> judges = new ArrayList<>();
        Map<String, Object> temMap;
        List<Map<String, Object>> mapList = AnnotationUtils.getActFieldByClazz(classUrl);
        for (Map remap : mapList) {
            temMap = new HashMap<>(2);
            ActField actField = (ActField) remap.get("actField");
            String keyName = (String) remap.get("keyName");
            if (actField != null) {
                temMap.put("value", keyName);
                temMap.put("name", actField.name());
                writes.add(temMap);
                if (actField.isJudg()) {
                    temMap.put("allow", actField.isJudg());
                    judges.add(temMap);
                }
            }
        }
        extActBusiness.setJudges(judges);
        extActBusiness.setWrites(writes);
        return extActBusiness;
    }

    /**
     * 递归删除业务流程  对应的 业务表
     *
     * @param id 业务流程  对应的 业务表id
     * @return {@link boolean}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Serializable id) {
        boolean result = super.deleteById(id);
        List<ExtActBusiness> list = selectList(new LambdaQueryWrapper<ExtActBusiness>().eq(ExtActBusiness::getPid, id));
        for (ExtActBusiness entity : list) {
            deleteById(entity.getId());
        }
        return result;
    }
}
