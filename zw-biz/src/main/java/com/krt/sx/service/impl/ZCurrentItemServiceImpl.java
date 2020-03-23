package com.krt.sx.service.impl;

import org.springframework.stereotype.Service;
import com.krt.sx.entity.ZCurrentItem;
import com.krt.sx.mapper.ZCurrentItemMapper;
import com.krt.sx.service.IZCurrentItemService;
import com.krt.common.base.BaseServiceImpl;


/**
 * 当前使用的事项信息服务接口实现层
 *
 * @author 李祥
 * @version 1.0
 * @date 2019年04月23日
 */
@Service
public class ZCurrentItemServiceImpl extends BaseServiceImpl<ZCurrentItemMapper, ZCurrentItem> implements IZCurrentItemService {

}
