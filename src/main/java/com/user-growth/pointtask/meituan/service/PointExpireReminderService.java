package com.vivo.pointtask.meituan.service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 【美团亮点】积分过期提醒服务 - 离线计算+MQ削峰方案
 * 
 * 参考来源：美团面试文章《用户积分系统怎么设计》
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 核心问题：
 * 5亿用户无法实时扫描，定时任务扫全表发短信不可行
 * 
 * 解决方案：
 * 1. T+1离线计算：用Hive/Spark每天凌晨计算"未来7天过期的用户"清单
 * 2. MQ削峰：拿到清单后用MQ削峰，控制短信发送速率（如5000 QPS）
 * 3. 惰性清理：
 *    - 读时触发：前端根据expire_time过滤
 *    - 写时触发：消费时过滤已过期Bucket
 *    - 物理归档：低频Job搬运垃圾数据到冷库
 * 
 * 与实时方案对比：
 * - 实时方案：扫全表，性能差，无法支撑大规模用户
 * - 离线方案：T+1计算，性能好，可支撑亿级用户
 * 
 * 降级方案（中小规模）：
 * - 日历表方案：预生成未来30天的过期任务
 * 
 * @author 参考：Fox爱分享
 * @version 5.0.0
 */
public interface PointExpireReminderService {
    
    /**
     * 【核心方法】T+1离线计算过期用户清单
     * 
     * 演化说明：
     * 每天凌晨用Hive/Spark计算未来N天即将过期的用户清单。
     * 
     * @param days 提前天数（如7天）
     * @return 计算结果
     */
    ExpireUserList calculateExpireUsers(int days);
    
    /**
     * 【核心方法】发送过期提醒（MQ削峰）
     * 
     * 演化说明：
     * 通过MQ控制发送速率，防止短信服务被压垮。
     * 控制速率：如5000 QPS
     * 
     * @param userId 用户ID
     * @param expirePoints 即将过期的积分
     * @param expireDate 过期日期
     * @return 发送结果
     */
    ReminderSendResult sendReminder(Long userId, int expirePoints, LocalDateTime expireDate);
    
    /**
     * 【核心方法】批量发送过期提醒
     * 
     * @param users 用户列表
     * @return 批量发送结果
     */
    BatchReminderResult batchSendReminders(List<ExpireUserInfo> users);
    
    /**
     * 【核心方法】惰性清理过期积分
     * 
     * 演化说明：
     * 不主动删除，而是在读写时触发清理。
     * 
     * @param userId 用户ID
     * @return 清理结果
     */
    CleanupResult lazyCleanup(Long userId);
    
    /**
     * 【核心方法】物理归档过期数据
     * 
     * 演化说明：
     * 低频Job将已过期且已用完的Bucket搬运到冷库。
     * 
     * @param beforeDate 归档此日期之前的数据
     * @return 归档结果
     */
    ArchiveResult archiveExpiredData(LocalDateTime beforeDate);
}

/**
 * 即将过期用户信息
 */
class ExpireUserInfo {
    private Long userId;
    private int expirePoints;
    private LocalDateTime expireDate;
    
    // Constructor and getters...
    public ExpireUserInfo(Long userId, int expirePoints, LocalDateTime expireDate) {
        this.userId = userId;
        this.expirePoints = expirePoints;
        this.expireDate = expireDate;
    }
    
    public Long getUserId() { return userId; }
    public int getExpirePoints() { return expirePoints; }
    public LocalDateTime getExpireDate() { return expireDate; }
}
