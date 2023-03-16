package com.linkwechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.annotation.SqlParser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.linkwechat.domain.WeLxQrCodeLog;

/**
 * 拉新活码领取记录表(WeLxQrCodeLog)
 *
 * @author danmo
 * @since 2023-03-16 16:19:02
 */
@Repository()
@Mapper
public interface WeLxQrCodeLogMapper extends BaseMapper<WeLxQrCodeLog> {


}

