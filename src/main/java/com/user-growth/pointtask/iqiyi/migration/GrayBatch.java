package com.vivo.pointtask.iqiyi.migration;

/**
 * 【爱奇艺亮点】灰度批次配置
 * 
 * 参考来源：爱奇艺积分系统架构演进
 * 文章链接：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q
 * 
 * 默认批次配置：
 * - BATCH_1: 1%  （小流量验证）
 * - BATCH_2: 10% （扩大验证范围）
 * - BATCH_3: 20% （进一步验证）
 * - BATCH_4: 50% （半量验证）
 * - BATCH_5: 100%（全量发布）
 * 
 * @author 参考：爱奇艺技术产品团队
 * @version 4.0.0
 */
public class GrayBatch {
    
    /**
     * 批次编号
     */
    private int batchNumber;
    
    /**
     * 批次名称
     */
    private String batchName;
    
    /**
     * 灰度比例（0-100）
     */
    private int grayPercent;
    
    /**
     * UID尾号范围（用于判断用户是否在灰度范围内）
     * 例如：grayPercent=10时，uidSuffixRange="00-09"
     */
    private String uidSuffixRange;
    
    /**
     * 批次描述
     */
    private String description;
    
    /**
     * 是否可回滚到此批次
     */
    private boolean rollbackable;
    
    // ==================== 预定义批次 ====================
    
    /**
     * 第一批：1% 小流量验证
     */
    public static final GrayBatch BATCH_1 = new GrayBatch(1, "第一批", 1, "00", "小流量验证", true);
    
    /**
     * 第二批：10% 扩大验证
     */
    public static final GrayBatch BATCH_2 = new GrayBatch(2, "第二批", 10, "00-09", "扩大验证范围", true);
    
    /**
     * 第三批：20% 进一步验证
     */
    public static final GrayBatch BATCH_3 = new GrayBatch(3, "第三批", 20, "00-19", "进一步验证", true);
    
    /**
     * 第四批：50% 半量验证
     */
    public static final GrayBatch BATCH_4 = new GrayBatch(4, "第四批", 50, "00-49", "半量验证", true);
    
    /**
     * 第五批：100% 全量发布
     */
    public static final GrayBatch BATCH_5 = new GrayBatch(5, "第五批", 100, "00-99", "全量发布", false);
    
    // ==================== 构造函数 ====================
    
    public GrayBatch() {
    }
    
    public GrayBatch(int batchNumber, String batchName, int grayPercent, 
                     String uidSuffixRange, String description, boolean rollbackable) {
        this.batchNumber = batchNumber;
        this.batchName = batchName;
        this.grayPercent = grayPercent;
        this.uidSuffixRange = uidSuffixRange;
        this.description = description;
        this.rollbackable = rollbackable;
    }
    
    // ==================== 业务方法 ====================
    
    /**
     * 检查用户ID是否在当前批次的灰度范围内
     * 
     * @param userId 用户ID
     * @return 是否在灰度范围内
     */
    public boolean isUserInGrayRange(Long userId) {
        if (userId == null) {
            return false;
        }
        
        // 获取UID最后两位
        String uidStr = userId.toString();
        String suffix = uidStr.length() >= 2 ? 
                       uidStr.substring(uidStr.length() - 2) : 
                       String.format("%02d", userId % 100);
        
        int suffixNum = Integer.parseInt(suffix);
        
        // 根据灰度比例判断
        return suffixNum < grayPercent;
    }
    
    /**
     * 获取所有预定义批次
     * 
     * @return 批次数组
     */
    public static GrayBatch[] getAllBatches() {
        return new GrayBatch[]{BATCH_1, BATCH_2, BATCH_3, BATCH_4, BATCH_5};
    }
    
    /**
     * 根据批次号获取批次
     * 
     * @param batchNumber 批次号
     * @return 批次配置
     */
    public static GrayBatch getBatchByNumber(int batchNumber) {
        for (GrayBatch batch : getAllBatches()) {
            if (batch.getBatchNumber() == batchNumber) {
                return batch;
            }
        }
        return null;
    }
    
    // ==================== Getter/Setter ====================
    
    public int getBatchNumber() {
        return batchNumber;
    }
    
    public void setBatchNumber(int batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    public String getBatchName() {
        return batchName;
    }
    
    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }
    
    public int getGrayPercent() {
        return grayPercent;
    }
    
    public void setGrayPercent(int grayPercent) {
        this.grayPercent = grayPercent;
    }
    
    public String getUidSuffixRange() {
        return uidSuffixRange;
    }
    
    public void setUidSuffixRange(String uidSuffixRange) {
        this.uidSuffixRange = uidSuffixRange;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isRollbackable() {
        return rollbackable;
    }
    
    public void setRollbackable(boolean rollbackable) {
        this.rollbackable = rollbackable;
    }
}
