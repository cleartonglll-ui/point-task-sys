package com.vivo.pointtask.stage3.rule;

/**
 * 【演化阶段三】规则执行结果
 * 
 * 演化说明：
 * 封装规则计算的结果，支持多种返回类型。
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class RuleResult {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 结果数据
     */
    private Object data;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    private RuleResult() {
    }
    
    /**
     * 创建成功结果
     * 
     * @param data 数据
     * @return 结果对象
     */
    public static RuleResult success(Object data) {
        RuleResult result = new RuleResult();
        result.success = true;
        result.data = data;
        return result;
    }
    
    /**
     * 创建失败结果
     * 
     * @param errorMessage 错误信息
     * @return 结果对象
     */
    public static RuleResult fail(String errorMessage) {
        RuleResult result = new RuleResult();
        result.success = false;
        result.errorMessage = errorMessage;
        return result;
    }
    
    /**
     * 创建布尔结果
     * 
     * @param success 是否成功
     * @return 结果对象
     */
    public static RuleResult of(boolean success) {
        RuleResult result = new RuleResult();
        result.success = success;
        return result;
    }
    
    /**
     * 创建带数据的布尔结果
     * 
     * @param success 是否成功
     * @param data 数据
     * @return 结果对象
     */
    public static RuleResult of(boolean success, Object data) {
        RuleResult result = new RuleResult();
        result.success = success;
        result.data = data;
        return result;
    }
    
    // ==================== Getter ====================
    
    public boolean isSuccess() {
        return success;
    }
    
    public Object getData() {
        return data;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * 获取数值类型的数据
     * 
     * @return 数值
     */
    public Number getNumberData() {
        if (data instanceof Number) {
            return (Number) data;
        }
        return 0;
    }
    
    /**
     * 获取整数类型的数据
     * 
     * @return 整数值
     */
    public int getIntData() {
        return getNumberData().intValue();
    }
    
    @Override
    public String toString() {
        return "RuleResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
