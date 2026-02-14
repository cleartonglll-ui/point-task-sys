package com.user.growth.reward.point.context.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.user.growth.reward.point.context.entity.PointDetailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分明细流水 Mapper
 *
 * @author system
 * @since 1.0.0
 */
@Mapper
public interface PointDetailMapper extends BaseMapper<PointDetailDO> {
}
