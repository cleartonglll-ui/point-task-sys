package com.vivo.pointtask.stage2.sdk;

import com.vivo.pointtask.stage2.entity.Behavior;
import com.vivo.pointtask.stage2.entity.TrackingEvent;
import com.vivo.pointtask.stage2.service.BehaviorService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * 【演化阶段二】行为采集SDK - 核心组件
 * 
 * 演化逻辑（对应文章阶段二核心改进）：
 * 引入端侧SDK，主动采集用户埋点行为，替代业务方自行实现行为采集。
 * 
 * 核心职责：
 * 1. 拉取任务配置：从服务端获取当前生效的任务和行为配置
 * 2. 监听埋点事件：注册监听器，捕获App端产生的埋点事件
 * 3. 行为过滤去重：根据行为定义过滤有效事件，进行去重处理
 * 4. 上报行为数据：将匹配的行为数据上报到服务端
 * 
 * 业务价值：
 * - 一次SDK接入，后续任务零成本开发
 * - 业务方无需关心行为采集细节
 * - 系统统一处理行为判定和奖励发放
 * 
 * @author vivo积分任务系统
 * @version 2.0.0
 */
public class BehaviorSdk {
    
    /**
     * 行为服务，用于获取配置和上报事件
     */
    private BehaviorService behaviorService;
    
    /**
     * 当前生效的行为配置列表
     * 演化说明：SDK会定期从服务端拉取最新的行为配置
     */
    private List<Behavior> activeBehaviors = new CopyOnWriteArrayList<>();
    
    /**
     * 事件监听器注册表
     * 演化说明：支持多个监听器，用于内部处理和上报
     */
    private Map<String, List<Consumer<TrackingEvent>>> eventListeners = new ConcurrentHashMap<>();
    
    /**
     * 去重缓存：记录已处理的事件（用户ID+事件ID）
     * 演化说明：防止重复计算，确保任务进度准确
     */
    private Map<String, Boolean> processedEvents = new ConcurrentHashMap<>();
    
    /**
     * SDK初始化标志
     */
    private volatile boolean initialized = false;
    
    public BehaviorSdk(BehaviorService behaviorService) {
        this.behaviorService = behaviorService;
    }
    
    /**
     * 【核心方法】初始化SDK
     * 
     * 演化说明：
     * SDK初始化时会拉取服务端配置，并启动定时刷新机制
     * 这是"一次接入，后续零成本"的关键
     */
    public void initialize() {
        if (initialized) {
            return;
        }
        
        // 拉取行为配置
        refreshBehaviorConfigs();
        
        // 启动定时刷新（简化实现，实际可能使用定时任务框架）
        startConfigRefreshTimer();
        
        initialized = true;
        System.out.println("[BehaviorSdk] SDK初始化完成，已加载 " + activeBehaviors.size() + " 个行为配置");
    }
    
    /**
     * 【核心方法】上报埋点事件
     * 
     * 演化说明：
     * App端产生埋点后调用此方法上报，SDK会自动：
     * 1. 匹配行为定义
     * 2. 过滤无效事件
     * 3. 去重处理
     * 4. 通知服务端进行任务判定
     * 
     * @param eventName 事件名称
     * @param userId 用户ID
     * @param parameters 事件参数
     */
    public void trackEvent(String eventName, Long userId, Map<String, Object> parameters) {
        if (!initialized) {
            throw new RuntimeException("SDK未初始化，请先调用initialize()");
        }
        
        // 创建事件对象
        TrackingEvent event = new TrackingEvent(eventName, userId, parameters);
        
        // 去重检查
        String dedupKey = generateDedupKey(userId, eventName, parameters);
        if (processedEvents.containsKey(dedupKey)) {
            System.out.println("[BehaviorSdk] 事件已处理，跳过: " + eventName);
            return;
        }
        
        // 标记为已处理
        processedEvents.put(dedupKey, true);
        
        // 匹配行为定义
        List<Behavior> matchedBehaviors = matchBehaviors(event);
        
        if (matchedBehaviors.isEmpty()) {
            System.out.println("[BehaviorSdk] 事件未匹配到行为定义: " + eventName);
            return;
        }
        
        // 通知服务端处理匹配的行为
        for (Behavior behavior : matchedBehaviors) {
            System.out.println("[BehaviorSdk] 事件匹配行为: " + behavior.getBehaviorName());
            behaviorService.processBehaviorEvent(userId, behavior.getId(), event);
        }
        
        // 触发注册的监听器
        notifyListeners(eventName, event);
    }
    
    /**
     * 【核心方法】注册事件监听器
     * 
     * @param eventName 事件名称
     * @param listener 监听器回调
     */
    public void addEventListener(String eventName, Consumer<TrackingEvent> listener) {
        eventListeners.computeIfAbsent(eventName, k -> new CopyOnWriteArrayList<>()).add(listener);
    }
    
    /**
     * 【内部方法】匹配行为定义
     * 
     * 演化说明：
     * SDK本地缓存了行为配置，可以在端侧快速匹配，减少服务端压力
     * 
     * @param event 埋点事件
     * @return 匹配的行为列表
     */
    private List<Behavior> matchBehaviors(TrackingEvent event) {
        List<Behavior> matched = new java.util.ArrayList<>();
        
        for (Behavior behavior : activeBehaviors) {
            // 使用行为实体的自我验证能力
            if (behavior.isValid() && behavior.matchesEvent(event)) {
                matched.add(behavior);
            }
        }
        
        return matched;
    }
    
    /**
     * 【内部方法】刷新行为配置
     * 
     * 演化说明：
     * 定期从服务端拉取最新的行为配置，确保SDK使用的是最新配置
     */
    private void refreshBehaviorConfigs() {
        List<Behavior> behaviors = behaviorService.getActiveBehaviors();
        activeBehaviors.clear();
        activeBehaviors.addAll(behaviors);
        System.out.println("[BehaviorSdk] 行为配置已刷新，当前 " + behaviors.size() + " 个有效行为");
    }
    
    /**
     * 【内部方法】启动配置刷新定时器
     */
    private void startConfigRefreshTimer() {
        // 简化实现：实际应使用ScheduledExecutorService等定时任务框架
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000); // 每分钟刷新一次
                    refreshBehaviorConfigs();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    
    /**
     * 【内部方法】生成去重Key
     * 
     * 演化说明：
     * 去重策略可以根据业务需求定制，如：
     * - 同一用户同一事件一天只算一次
     * - 同一用户同一事件参数组合只算一次
     * 
     * @param userId 用户ID
     * @param eventName 事件名称
     * @param parameters 事件参数
     * @return 去重Key
     */
    private String generateDedupKey(Long userId, String eventName, Map<String, Object> parameters) {
        // 简化实现：使用用户ID+事件名+当前日期作为去重Key
        String date = java.time.LocalDate.now().toString();
        return userId + ":" + eventName + ":" + date;
    }
    
    /**
     * 【内部方法】通知监听器
     * 
     * @param eventName 事件名称
     * @param event 事件对象
     */
    private void notifyListeners(String eventName, TrackingEvent event) {
        List<Consumer<TrackingEvent>> listeners = eventListeners.get(eventName);
        if (listeners != null) {
            for (Consumer<TrackingEvent> listener : listeners) {
                try {
                    listener.accept(event);
                } catch (Exception e) {
                    System.err.println("[BehaviorSdk] 监听器执行异常: " + e.getMessage());
                }
            }
        }
    }
}
