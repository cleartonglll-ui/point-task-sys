package com.vivo.pointtask.stage3.rule;

import com.vivo.pointtask.stage3.engine.ExpressionEngine;

import java.util.Map;

/**
 * 【演化阶段三】规则引擎 - 核心组件
 * 
 * 演化逻辑（对应文章阶段三核心改进）：
 * 基于表达式引擎构建的规则计算层，支持复杂行为计算。
 * 
 * 核心职责：
 * 1. 接收标准化的事件数据
 * 2. 根据规则配置进行计算
 * 3. 输出计算结果（是否达成、奖励值等）
 * 
 * 规则示例：
 * - 消费满100元返积分："originEvent.amount >= 100"
 * - 消费金额的1%返积分："event.amount * 0.01"
 * - 游戏充值额外奖励："originEvent.product_type in ('11','12') ? event.amount * 0.02 : event.amount * 0.01"
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class RuleEngine {
    
    /**
     * 表达式引擎
     */
    private ExpressionEngine expressionEngine;
    
    public RuleEngine(ExpressionEngine expressionEngine) {
        this.expressionEngine = expressionEngine;
    }
    
    /**
     * 【核心方法】执行规则计算
     * 
     * 演化说明：
     * 这是规则计算层的入口方法，负责：
     * 1. 验证规则表达式
     * 2. 执行规则计算
     * 3. 返回计算结果
     * 
     * @param ruleExpression 规则表达式
     * @param eventData 事件数据
     * @return 规则执行结果
     */
    public RuleResult evaluate(String ruleExpression, Map<String, Object> eventData) {
        // 验证表达式
        if (!expressionEngine.validate(ruleExpression)) {
            return RuleResult.fail("无效的表达式: " + ruleExpression);
        }
        
        try {
            // 构建上下文
            Map<String, Object> context = buildContext(eventData);
            
            // 执行表达式
            Object result = expressionEngine.evaluate(ruleExpression, context);
            
            // 解析结果
            return parseResult(result);
            
        } catch (Exception e) {
            return RuleResult.fail("规则执行异常: " + e.getMessage());
        }
    }
    
    /**
     * 【核心方法】执行布尔规则判定
     * 
     * 演化说明：
     * 用于判断行为是否达成，如"消费满100元"
     * 
     * @param conditionExpression 条件表达式
     * @param eventData 事件数据
     * @return 是否达成
     */
    public boolean evaluateCondition(String conditionExpression, Map<String, Object> eventData) {
        if (conditionExpression == null || conditionExpression.trim().isEmpty()) {
            return true; // 无条件视为达成
        }
        
        Map<String, Object> context = buildContext(eventData);
        return expressionEngine.evaluateBoolean(conditionExpression, context);
    }
    
    /**
     * 【核心方法】计算奖励值
     * 
     * 演化说明：
     * 支持动态奖励计算，如"消费金额的1%"
     * 
     * @param rewardExpression 奖励表达式
     * @param eventData 事件数据
     * @return 奖励值
     */
    public Number calculateReward(String rewardExpression, Map<String, Object> eventData) {
        if (rewardExpression == null || rewardExpression.trim().isEmpty()) {
            return 0;
        }
        
        Map<String, Object> context = buildContext(eventData);
        return expressionEngine.evaluateNumber(rewardExpression, context);
    }
    
    /**
     * 【内部方法】构建表达式执行上下文
     * 
     * 演化说明：
     * 将事件数据封装为表达式可用的上下文变量
     * 
     * @param eventData 事件数据
     * @return 上下文
     */
    private Map<String, Object> buildContext(Map<String, Object> eventData) {
        Map<String, Object> context = new java.util.HashMap<>();
        
        // 原始事件数据
        context.put("originEvent", eventData);
        
        // 标准化后的事件数据（如果有）
        @SuppressWarnings("unchecked")
        Map<String, Object> normalizedData = (Map<String, Object>) eventData.get("_normalized");
        if (normalizedData != null) {
            context.put("event", normalizedData);
        } else {
            context.put("event", eventData);
        }
        
        // 数据源类型
        Object sourceType = eventData.get("_sourceType");
        if (sourceType != null) {
            context.put("sourceType", sourceType);
        }
        
        return context;
    }
    
    /**
     * 【内部方法】解析表达式执行结果
     * 
     * @param result 原始结果
     * @return 规则结果
     */
    private RuleResult parseResult(Object result) {
        if (result instanceof Boolean) {
            return RuleResult.of((Boolean) result);
        }
        
        if (result instanceof Number) {
            return RuleResult.success(((Number) result).intValue());
        }
        
        if (result instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> resultMap = (Map<String, Object>) result;
            Boolean success = (Boolean) resultMap.get("success");
            Object data = resultMap.get("data");
            return RuleResult.of(success != null && success, data);
        }
        
        return RuleResult.success(result);
    }
}
