package com.user.growth.reward.point.context.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 积分明细流水数据对象
 *
 * 对应数据库表 point_detail
 *
 * @author system
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("point_detail")
public class PointDetailDO extends com.user.growth.reward.common.BaseEntity {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 积分类型 1:获得 2:消费 3:冻结 4:解冻
     */
    private Integer pointType;

    /**
     * 业务类型 签到/作业/视频/互动/答题/邀请/兑换
     */
    private String bizType;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 积分变动数量 正数为增加 负数为减少
     */
    private Integer pointAmount;

    /**
     * 变动前积分
     */
    private Integer beforePoints;

    /**
     * 变动后积分
     */
    private Integer afterPoints;

    /**
     * 备注
     */
    private String remark;
}
