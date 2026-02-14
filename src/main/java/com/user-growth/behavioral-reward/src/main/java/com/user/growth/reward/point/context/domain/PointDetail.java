package com.user.growth.reward.point.context.domain;

import com.user.growth.reward.point.context.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积分明细流水领域模型
 *
 * 记录积分变动的详细信息
 *
 * @author system
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID（对应数据库主键）
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 积分类型 1:获得 2:消费 3:冻结 4:解冻
     */
    private PointType pointType;

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

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
