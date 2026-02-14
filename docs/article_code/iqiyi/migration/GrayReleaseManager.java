package com.vivo.pointtask.iqiyi.migration;

import java.util.List;

/**
 * 【爱奇艺亮点】灰度发布管理器 - 渐进式切流机制
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心设计：
 * 基于配置中心和UID尾号的渐进式灰度切流策略。
 * 分批推进：1% → 10% → 20% → 50% → 100%
 * 
 * 切流策略：
 * - 按UID尾号切流：根据用户ID的最后几位决定流量走向
 * - 配置中心控制：通过配置中心动态调整灰度比例
 * - 快速回滚：发现问题可立即切换回老版本
 * 
 * 业务价值：
 * - 降低风险：小流量验证，问题影响范围可控
 * - 快速回滚：发现问题可立即回滚
 * - 渐进式推进：逐步增加流量，确保稳定性
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public interface GrayReleaseManager {
    
    /**
     * 【核心方法】检查用户是否在新版本灰度范围内
     * 
     * 演化说明：
     * 根据用户ID和当前灰度配置，判断该用户是否使用新版本
     * 
     * @param userId 用户ID
     * @return 是否使用新版本
     */
    boolean isInGrayRelease(Long userId);
    
    /**
     * 【核心方法】获取当前灰度比例
     * 
     * @return 灰度比例（0-100）
     */
    int getCurrentGrayPercent();
    
    /**
     * 【核心方法】调整灰度比例
     * 
     * 演化说明：
     * 通过配置中心动态调整灰度比例，无需重启服务
     * 
     * @param percent 灰度比例（0-100）
     * @return 是否调整成功
     */
    boolean adjustGrayPercent(int percent);
    
    /**
     * 【核心方法】获取灰度批次配置
     * 
     * @return 灰度批次列表
     */
    List<GrayBatch> getGrayBatches();
    
    /**
     * 【核心方法】推进到下一批次
     * 
     * 演化说明：
     * 按预设批次逐步推进：1% → 10% → 20% → 50% → 100%
     * 
     * @return 新的灰度比例
     */
    int advanceToNextBatch();
    
    /**
     * 【核心方法】回滚到上一批次
     * 
     * 演化说明：
     * 发现问题时快速回滚到上一稳定批次
     * 
     * @return 回滚后的灰度比例
     */
    int rollbackToPreviousBatch();
    
    /**
     * 【核心方法】全量切换（100%）
     * 
     * @return 是否切换成功
     */
    boolean fullRelease();
    
    /**
     * 【核心方法】全量回滚（0%）
     * 
     * @return 是否回滚成功
     */
    boolean fullRollback();
}
