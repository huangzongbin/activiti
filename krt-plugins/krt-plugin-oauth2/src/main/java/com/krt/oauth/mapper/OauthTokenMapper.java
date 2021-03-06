package com.krt.oauth.mapper;

import com.krt.common.base.BaseMapper;
import com.krt.oauth.entity.OauthToken;
import org.apache.ibatis.annotations.Mapper;


/**
 * 认证token映射层
 *
 * @author 殷帅
 * @version 1.0
 * @date 2017年12月12日
 */
@Mapper
public interface OauthTokenMapper extends BaseMapper<OauthToken> {


}
