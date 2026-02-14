package com.vivo.pointtask.stage3.engine;

import java.util.Map;

/**
 * 【演化阶段三】表达式引擎接口
 * 
 * 演化逻辑（对应文章阶段三核心技术选型）：
 * 引入表达式引擎（如AviatorScript）支持动态规则配置。
 * 
 * 技术选型：AviatorScript
 * - 轻量级：仅70K（不含依赖）
 * - 高性能：直接编译为Java字节码
 * - 灵活性：支持复杂业务逻辑动态配置
 * 
 * 使用场景：
 * 1. 数据预处理过滤
 * 2. 规则计算
 * 3. 动态奖励计算
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public interface ExpressionEngine {
    
    /**
     * 【核心方法】执行布尔表达式
     * 
     * 演化说明：
     * 用于数据过滤和规则判定
     * 
     * 示例表达式：
     * - "originEvent.pay_status == 1 && originEvent.amount > 100"
     * - "string.contains('11,12,13', originEvent.product_type)"
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 执行结果
     */
    boolean evaluateBoolean(String expression, Map<String, Object> context);
    
    /**
     * 【核心方法】执行计算表达式
     * 
     * 演化说明：
     * 用于动态计算奖励值
     * 
     * 示例表达式：
     * - "event.amount / 100"
     * - "min(event.count * 10, 100)"
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 计算结果
     */
    Object evaluate(String expression, Map<String, Object> context);
    
    /**
     * 【核心方法】执行数值计算表达式
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 数值结果
     */
    Number evaluateNumber(String expression, Map<String, Object> context);
    
    /**
     * 【核心方法】验证表达式语法
     * 
     * @param expression 表达式
     * @return 是否有效
     */
    boolean validate(String expression);
}
