package com.vivo.pointtask.stage3.engine;

import java.util.Map;

/**
 * 【演化阶段三】Aviator表达式引擎实现
 * 
 * 演化逻辑（对应文章阶段三核心技术选型）：
 * 使用AviatorScript作为表达式引擎的实现。
 * 
 * 注意：这是一个简化实现，实际项目中应引入Aviator依赖：
 * <dependency>
 *     <groupId>com.googlecode.aviator</groupId>
 *     <artifactId>aviator</artifactId>
 *     <version>5.4.3</version>
 * </dependency>
 * 
 * Aviator优势：
 * 1. 高性能：编译为JVM字节码执行
 * 2. 轻量级：核心仅70K
 * 3. 功能丰富：支持函数、变量、运算符等
 * 4. 安全：可限制执行时间和资源
 * 
 * @author vivo积分任务系统
 * @version 3.0.0
 */
public class AviatorExpressionEngine implements ExpressionEngine {
    
    // 实际项目中使用：
    // private static final AviatorEvaluator evaluator = AviatorEvaluator.getInstance();
    
    public AviatorExpressionEngine() {
        // 初始化Aviator配置
        // evaluator.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        // evaluator.setOption(Options.MAX_LOOP_COUNT, 1000);
    }
    
    /**
     * 【核心方法】执行布尔表达式
     * 
     * 演化示例（对应文章中的表达式）：
     * 数据清洗过滤：
     * originEvent.pay_status == 1 && string.contains("11,12,13,14,15,16,92,93,95", originEvent.product_type + "")
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 执行结果
     */
    @Override
    public boolean evaluateBoolean(String expression, Map<String, Object> context) {
        // 实际实现：
        // Expression compiledExpr = evaluator.compile(expression);
        // Object result = compiledExpr.execute(context);
        // return Boolean.TRUE.equals(result);
        
        // 简化实现：模拟表达式执行
        return simulateBooleanEvaluation(expression, context);
    }
    
    /**
     * 【核心方法】执行计算表达式
     * 
     * 演化示例（对应文章中的表达式）：
     * 规则计算：
     * let value = eventObject.value / 100;
     * let success = value >= 1;
     * return seq.map('success',success,'data',value);
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 计算结果
     */
    @Override
    public Object evaluate(String expression, Map<String, Object> context) {
        // 实际实现：
        // Expression compiledExpr = evaluator.compile(expression);
        // return compiledExpr.execute(context);
        
        // 简化实现
        return simulateEvaluation(expression, context);
    }
    
    /**
     * 【核心方法】执行数值计算表达式
     * 
     * @param expression 表达式
     * @param context 上下文变量
     * @return 数值结果
     */
    @Override
    public Number evaluateNumber(String expression, Map<String, Object> context) {
        Object result = evaluate(expression, context);
        if (result instanceof Number) {
            return (Number) result;
        }
        return 0;
    }
    
    /**
     * 【核心方法】验证表达式语法
     * 
     * @param expression 表达式
     * @return 是否有效
     */
    @Override
    public boolean validate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return false;
        }
        
        try {
            // 实际实现：
            // evaluator.validate(expression);
            // return true;
            
            // 简化实现：基本语法检查
            return expression.contains("event") || expression.contains("originEvent");
        } catch (Exception e) {
            return false;
        }
    }
    
    // ==================== 模拟实现（实际应使用Aviator） ====================
    
    /**
     * 模拟布尔表达式执行
     * 
     * 演化说明：
     * 这里模拟了文章中的表达式执行逻辑
     */
    private boolean simulateBooleanEvaluation(String expression, Map<String, Object> context) {
        // 模拟：originEvent.pay_status == 1
        if (expression.contains("pay_status == 1")) {
            Map<String, Object> originEvent = (Map<String, Object>) context.get("originEvent");
            if (originEvent != null) {
                Object payStatus = originEvent.get("pay_status");
                return Integer.valueOf(1).equals(payStatus);
            }
        }
        
        // 模拟：originEvent.amount > 100
        if (expression.contains("amount >")) {
            Map<String, Object> originEvent = (Map<String, Object>) context.get("originEvent");
            if (originEvent != null) {
                Object amount = originEvent.get("amount");
                if (amount instanceof Number) {
                    return ((Number) amount).doubleValue() > 100;
                }
            }
        }
        
        // 默认返回true
        return true;
    }
    
    /**
     * 模拟表达式执行
     */
    private Object simulateEvaluation(String expression, Map<String, Object> context) {
        // 模拟：event.amount / 100
        if (expression.contains("/ 100") || expression.contains("/100")) {
            Map<String, Object> event = (Map<String, Object>) context.get("event");
            if (event != null) {
                Object amount = event.get("amount");
                if (amount instanceof Number) {
                    return ((Number) amount).doubleValue() / 100;
                }
            }
        }
        
        // 模拟：event.count * 10
        if (expression.contains("* 10")) {
            Map<String, Object> event = (Map<String, Object>) context.get("event");
            if (event != null) {
                Object count = event.get("count");
                if (count instanceof Number) {
                    return ((Number) count).intValue() * 10;
                }
            }
        }
        
        return 0;
    }
}
