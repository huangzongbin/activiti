package com.krt.act.mapper;

import com.krt.act.entity.ExtActTaskLog;
import com.krt.common.base.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务日志映射层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2019年03月27日
 */
@Mapper
public interface ExtActTaskLogMapper extends BaseMapper<ExtActTaskLog> {

    /**
     * 查询办理记录
     *
     * @param busId      业务id
     * @param instanceId 流程实例id
     * @return {@link List<ExtActTaskLog>}
     */
    List<ExtActTaskLog> selectTaskLogList(@Param("busId") Integer busId, @Param("instanceId") String instanceId);
}
