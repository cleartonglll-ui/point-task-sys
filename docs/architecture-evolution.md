# 积分任务体系架构演进文档

> 本文档整合了多个互联网大厂（vivo、爱奇艺）的积分系统架构演进实践，详细记录了从贫血模型到充血模型的完整演化过程，以及存储架构、数据一致性保障等关键设计。

---

## 目录

1. [原系统的问题（架构痛点）](#一原系统的问题架构痛点)
2. [思考逻辑（思考过程）](#二思考逻辑思考过程)
3. [核心的演化逻辑](#三核心的演化逻辑)
4. [演化-代码对应关系](#四演化-代码对应关系)
5. [架构演进对比](#五架构演进对比)
6. [业务效果提升](#六业务效果提升)
7. [参考案例：爱奇艺积分系统](#七参考案例爱奇艺积分系统)

---

## 参考来源

| 案例 | 来源 | 核心亮点 |
|------|------|---------|
| **vivo积分任务体系** | https://mp.weixin.qq.com/s/kbAQTP1JeeDoTt-kId1Nog | 从贫血模型到充血模型的DDD实践 |
| **爱奇艺积分系统** | https://mp.weixin.qq.com/s/7U2Hhfe-6d1q3nEoDwHOWw | MySQL+MongoDB分离存储到MongoDB统一存储 |

---

---

## 一、原系统的问题（架构痛点）

### 1.1 阶段一的痛点

#### 跨项目协作周期长
- **跨项目管理难度大**：进度对齐、沟通协调困难
- **Case by case开发**：系统耦合严重，灵活性低
- **业务方逻辑重**：开发成本高，每个任务都需要业务方自行实现

#### 业务上运营效率低
- **上线周期长**：一个季度上线不了几个任务
- **效果不理想**：虽然任务基础信息可通过配置化完成，但远没有达到预期的"通过配置化就能够实现任务快速上线"

### 1.2 架构层面的根本问题

#### 贫血模型（Anemic Domain Model）
```java
// 阶段一：Task.java - 典型的贫血模型
public class Task {
    private Long id;
    private String taskName;
    // ... 只有字段和getter/setter
    
    // 没有任何业务行为！
}
```

**问题表现**：
1. **实体只是数据容器**：无法体现业务语义
2. **业务逻辑散落在Service层**：过程式代码，难以维护
3. **跨项目协作困难**：业务方需要重复实现判定逻辑

#### 任务完成逻辑需要业务方自行实现
```java
// 阶段一：TaskService.java
public void completeTask(Long userId, Long taskId) {
    // 系统只提供了基础的配置能力
    // 具体的"行为采集"和"达成判定"都由业务方完成
    // 导致：跨项目协作周期长、业务方逻辑重、开发成本高
}
```

### 1.3 阶段二的痛点

- **行为采集源单一**：非埋点行为类的任务不支持
- **触达形式单调**：只支持简单的Toast以及Snackbar

---

## 二、思考逻辑（思考过程）

### 2.1 问题分析

1. **初始系统仅引入了任务的定义与配置**，但任务的行为以及达成判定都由业务方实现
2. **发现大多数任务行为都是App端侧产生的行为**（如用户浏览新闻）
3. **结合互联网业务都有日常埋点的现状**，可以利用埋点上报捕获用户行为

### 2.2 业务本质分析

任务的本质是：
- **收集行为**
- **判定任务关联行为是否达成**
- **任务的奖励发放**
- **对用户的触达**

需要着重关注：
- 行为采集源的拓展
- 支持复杂行为计算
- 动态的规则配置

### 2.3 技术选型思考

#### 表达式引擎选择：AviatorScript

**选型理由**：
- **轻量级**：仅70K（不含依赖）
- **高性能**：直接编译为Java字节码
- **灵活性**：支持复杂业务逻辑动态配置

**使用场景**：
1. 数据预处理过滤
2. 规则计算
3. 动态奖励计算

---

## 三、核心的演化逻辑

### 3.1 阶段一 → 阶段二：从被动配置到主动采集

| 维度 | 阶段一（贫血模型） | 阶段二（充血模型初探） |
|------|-------------------|----------------------|
| **采集方式** | 业务方主动接入实现 | SDK主动采集埋点 |
| **判定逻辑** | 业务方实现 | 系统统一处理 |
| **集成成本** | 每次接入都需开发 | 一次接入，后续零成本 |
| **上线周期** | 1-3个月 | 1-3人天 |
| **支持能力** | 基础的配置 | 基础+实验+标签投放 |

#### 核心改动

**1. 引入行为模型（Behavior）**
```java
// 阶段二新增：Behavior.java
public class Behavior {
    private String eventName;  // 埋点事件名
    private Map<String, Object> filterConditions;  // 过滤条件
    
    // 充血模型：行为自我验证
    public boolean matchesEvent(TrackingEvent event) {
        // 验证埋点事件是否匹配
    }
}
```

**2. 引入行为采集SDK**
```java
// 阶段二新增：BehaviorSdk.java
public class BehaviorSdk {
    // 1. 拉取任务配置
    // 2. 监听埋点事件
    // 3. 行为过滤去重
    // 4. 上报行为数据
}
```

**3. 实体开始封装业务行为**
```java
// 阶段二：Task.java 开始充血
public boolean isValid() {
    // 将判定逻辑封装在实体中
}

public boolean matchesUserTags(List<String> userTags) {
    // 支持标签投放
}
```

### 3.2 阶段二 → 阶段三：从单一埋点到多源采集 + 灵活计算

| 维度 | 阶段二 | 阶段三（完整充血模型） |
|------|--------|----------------------|
| **数据源** | 仅支持埋点 | 埋点、数据库、消息队列、API/RPC |
| **规则配置** | 固定规则 | 动态表达式（Aviator） |
| **触达形式** | Toast/Snackbar | 自定义弹窗 + 消息透传 |
| **计算能力** | 简单判定 | 复杂行为计算（如根据金额分级奖励） |

#### 核心改动

**1. 多源数据采集**
```java
// 阶段三新增：DataSourceType.java
public enum DataSourceType {
    TRACKING,      // 埋点数据（阶段二已支持）
    DATABASE,      // 【新增】数据库
    MESSAGE_QUEUE, // 【新增】消息队列
    API            // 【新增】API/RPC接口
}
```

**2. 引入表达式引擎**
```java
// 阶段三新增：ExpressionEngine.java
public interface ExpressionEngine {
    // 数据预处理过滤
    boolean evaluateBoolean(String expression, Map<String, Object> context);
    
    // 动态奖励计算
    Number evaluateNumber(String expression, Map<String, Object> context);
}
```

**3. 丰富触达形式**
```java
// 阶段三新增：TouchType.java
public enum TouchType {
    TOAST,        // 阶段二支持
    SNACKBAR,     // 阶段二支持
    DIALOG,       // 【新增】自定义弹窗
    NOTIFICATION  // 【新增】消息推送
}
```

**4. 完整的充血模型**
```java
// 阶段三：Task.java - 完整充血模型
public class Task {
    // 数据字段...
    
    // 完整的业务行为封装
    public boolean isValid() { }
    public boolean matchesUserTags(List<String> userTags) { }
    public int calculateReward(RewardContext context) { }
    public boolean shouldNotifyUser() { }
}
```

---

## 四、演化-代码对应关系

### 4.1 代码改动位置总览

```
src/main/java/com/vivo/pointtask/
├── stage1/                    # 【阶段一】贫血模型
│   ├── entity/
│   │   ├── Task.java          # 只有getter/setter
│   │   └── UserTask.java      # 只有getter/setter
│   └── service/
│       └── TaskService.java   # 所有业务逻辑都在Service
├── stage2/                    # 【阶段二】充血模型初探
│   ├── entity/
│   │   ├── Task.java          # 【改动】新增isValid()等方法
│   │   ├── UserTask.java      # 【改动】新增isCompleted()等方法
│   │   ├── Behavior.java      # 【新增】行为模型
│   │   └── TrackingEvent.java # 【新增】埋点事件
│   ├── sdk/
│   │   └── BehaviorSdk.java   # 【新增】行为采集SDK
│   └── service/
│       └── BehaviorService.java # 【新增】行为处理服务
└── stage3/                    # 【阶段三】完整充血模型
    ├── entity/
    │   ├── Task.java          # 【改动】完整充血模型
    │   ├── UserTask.java      # 【改动】完整充血模型
    │   ├── TouchConfig.java   # 【新增】触达配置
    │   ├── TouchType.java     # 【新增】触达类型枚举
    │   └── RewardContext.java # 【新增】奖励计算上下文
    ├── collector/
    │   ├── DataSourceType.java   # 【新增】数据源类型
    │   └── DataSourceConfig.java # 【新增】数据源配置
    ├── engine/
    │   ├── ExpressionEngine.java       # 【新增】表达式引擎接口
    │   └── AviatorExpressionEngine.java # 【新增】Aviator实现
    ├── rule/
    │   ├── RuleEngine.java    # 【新增】规则引擎
    │   └── RuleResult.java    # 【新增】规则结果
    └── service/
        └── TaskService.java    # 【改动】支持多源数据和表达式
```

### 4.2 详细对应关系

#### 演化逻辑1：从贫血模型到充血模型

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 将任务有效性判定封装到实体 | `stage2/entity/Task.java` - `isValid()` | 新增方法 |
| 将用户标签匹配封装到实体 | `stage2/entity/Task.java` - `matchesUserTags()` | 新增方法 |
| 将实验组检查封装到实体 | `stage2/entity/Task.java` - `isInExperimentGroup()` | 新增方法 |
| 将行为匹配封装到实体 | `stage2/entity/Behavior.java` - `matchesEvent()` | 新增方法 |

**代码对比**：
```java
// 阶段一（贫血模型）：判定逻辑在Service中
public class TaskService {
    public boolean isTaskValid(Task task) {
        // 过程式判定
        return task.getStatus() == 1 &&
               (task.getStartTime() == null || !now.isBefore(task.getStartTime()));
    }
}

// 阶段二（充血模型）：判定逻辑封装在实体中
public class Task {
    public boolean isValid() {
        // 实体自己知道如何验证有效性
        return status != null && status == 1 &&
               (startTime == null || !LocalDateTime.now().isBefore(startTime));
    }
}
```

#### 演化逻辑2：引入行为模型和SDK

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 定义行为实体 | `stage2/entity/Behavior.java` | 新增文件 |
| 定义埋点事件 | `stage2/entity/TrackingEvent.java` | 新增文件 |
| 实现行为采集SDK | `stage2/sdk/BehaviorSdk.java` | 新增文件 |
| 实现行为处理服务 | `stage2/service/BehaviorService.java` | 新增文件 |

**核心代码**：
```java
// stage2/sdk/BehaviorSdk.java
public class BehaviorSdk {
    /**
     * 【核心方法】上报埋点事件
     * 演化说明：App端产生埋点后调用此方法，SDK自动匹配行为并上报
     */
    public void trackEvent(String eventName, Long userId, Map<String, Object> parameters) {
        // 1. 创建事件对象
        // 2. 去重检查
        // 3. 匹配行为定义
        // 4. 通知服务端处理
    }
}
```

#### 演化逻辑3：多源数据采集

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 定义数据源类型枚举 | `stage3/collector/DataSourceType.java` | 新增文件 |
| 定义数据源配置 | `stage3/collector/DataSourceConfig.java` | 新增文件 |
| 支持数据预处理表达式 | `DataSourceConfig.preprocessExpression` | 新增字段 |
| 支持数据归一化 | `DataSourceConfig.normalizeData()` | 新增方法 |

**核心代码**：
```java
// stage3/collector/DataSourceType.java
public enum DataSourceType {
    TRACKING,       // 埋点数据
    DATABASE,       // 【新增】数据库
    MESSAGE_QUEUE,  // 【新增】消息队列
    API             // 【新增】API接口
}

// stage3/collector/DataSourceConfig.java
public class DataSourceConfig {
    /**
     * 【演化新增】数据预处理表达式（Aviator表达式）
     * 演化说明：在数据进入规则计算层前进行过滤和转换
     * 示例："originEvent.pay_status == 1 && originEvent.amount > 0"
     */
    private String preprocessExpression;
}
```

#### 演化逻辑4：引入表达式引擎

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 定义表达式引擎接口 | `stage3/engine/ExpressionEngine.java` | 新增文件 |
| 实现Aviator引擎 | `stage3/engine/AviatorExpressionEngine.java` | 新增文件 |
| 构建规则引擎 | `stage3/rule/RuleEngine.java` | 新增文件 |
| 定义规则结果 | `stage3/rule/RuleResult.java` | 新增文件 |

**核心代码**：
```java
// stage3/engine/ExpressionEngine.java
public interface ExpressionEngine {
    /**
     * 【核心方法】执行布尔表达式
     * 示例："originEvent.pay_status == 1 && originEvent.amount > 100"
     */
    boolean evaluateBoolean(String expression, Map<String, Object> context);
    
    /**
     * 【核心方法】执行数值计算表达式
     * 示例："event.amount / 100"
     */
    Number evaluateNumber(String expression, Map<String, Object> context);
}

// stage3/rule/RuleEngine.java
public class RuleEngine {
    public RuleResult evaluate(String ruleExpression, Map<String, Object> eventData) {
        // 1. 验证表达式
        // 2. 构建上下文
        // 3. 执行表达式
        // 4. 返回结果
    }
}
```

#### 演化逻辑5：丰富触达形式

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 定义触达类型枚举 | `stage3/entity/TouchType.java` | 新增文件 |
| 定义触达配置 | `stage3/entity/TouchConfig.java` | 新增文件 |
| 支持模板变量 | `TouchConfig.renderContent()` | 新增方法 |
| Task支持触达配置 | `stage3/entity/Task.java` - `touchConfig` | 新增字段 |

**核心代码**：
```java
// stage3/entity/TouchType.java
public enum TouchType {
    TOAST,        // 阶段二支持
    SNACKBAR,     // 阶段二支持
    DIALOG,       // 【新增】自定义弹窗
    NOTIFICATION  // 【新增】消息推送
}

// stage3/entity/TouchConfig.java
public class TouchConfig {
    /**
     * 【演化改进】渲染触达内容
     * 演化说明：支持模板变量替换
     * 示例："恭喜您完成{taskName}，获得{points}积分！"
     */
    public String renderContent(Map<String, String> variables) {
        // 模板变量替换
    }
}
```

#### 演化逻辑6：动态奖励计算

| 演化逻辑 | 代码位置 | 改动类型 |
|---------|---------|---------|
| 定义奖励上下文 | `stage3/entity/RewardContext.java` | 新增文件 |
| Task支持奖励表达式 | `stage3/entity/Task.java` - `rewardExpression` | 新增字段 |
| Task支持动态计算 | `stage3/entity/Task.java` - `calculateReward()` | 新增方法 |

**核心代码**：
```java
// stage3/entity/RewardContext.java
public class RewardContext {
    private Long userId;
    private Double eventAmount;    // 事件金额
    private Integer eventCount;    // 事件数量
    private Long eventDuration;    // 事件时长
    private Map<String, Object> rawEventData;
}

// stage3/entity/Task.java
public class Task {
    /**
     * 【演化新增】动态奖励表达式
     * 演化说明：支持根据行为数据动态计算奖励
     * 示例："event.amount / 100"（消费100元返1积分）
     */
    private String rewardExpression;
    
    /**
     * 【演化新增】计算奖励积分
     */
    public int calculateReward(RewardContext context) {
        if (rewardExpression != null && !rewardExpression.isEmpty()) {
            return evaluateRewardExpression(rewardExpression, context);
        }
        return rewardPoints != null ? rewardPoints : 0;
    }
}
```

---

## 五、架构演进对比

### 5.1 系统架构演进图

```
【阶段一】贫血模型架构
┌─────────────────────────────────────┐
│           业务方                     │
│  （自行实现行为采集和判定）            │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│           端侧（App）                │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│        任务服务（TaskService）        │
│  （过程式业务逻辑，贫血实体）          │
└─────────────────────────────────────┘

【阶段二】充血模型初探
┌─────────────────────────────────────┐
│           端侧App                    │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      行为SDK（BehaviorSdk）          │
│  （拉取配置、上报行为、过滤、去重）     │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      行为服务（BehaviorService）      │
│  （用户标签、实验策略、行为计算）        │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│        任务服务（充血实体）            │
│  （Task.isValid(), Task.matchesUserTags()）│
└─────────────────────────────────────┘

【阶段三】完整充血模型
┌─────────────────────────────────────┐
│ 数据源                               │
├──────────┬──────────┬──────┬──────┤
│ 埋点数据 │ 数据库   │ 消息队列│ API │
│ (SDK)    │ (MySQL)  │ (MQ)  │(RPC)│
└──────────┴──────────┴──────┴──────┘
              │
┌─────────────▼───────────────────────┐
│        采集层（数据预处理）           │
│  ├─ 集群管理                         │
│  ├─ 数据源管理                       │
│  ├─ 元数据管理                       │
│  ├─ 数据预处理（Aviator过滤）        │
│  ├─ 数据归一化                       │
│  └─ 数据存储(MongoDB/TiDB)          │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│      规则计算层（表达式引擎）          │
│  ├─ ExpressionEngine                │
│  ├─ RuleEngine                      │
│  └─ 支持复杂行为计算                  │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│            任务层                    │
│  ├─ 任务与行为关联（充血实体）         │
│  ├─ 任务投放                         │
│  ├─ 奖励发放                         │
│  └─ 用户状态管理                      │
└─────────────┬───────────────────────┘
              │
┌─────────────▼───────────────────────┐
│            触达层                    │
│  ├─ Toast/Snackbar                  │
│  ├─ 自定义弹窗                       │
│  └─ 消息透传                         │
└─────────────────────────────────────┘
```

### 5.2 代码行数对比

| 模块 | 阶段一 | 阶段二 | 阶段三 | 增长率 |
|------|--------|--------|--------|--------|
| Entity | ~300行 | ~500行 | ~800行 | +167% |
| Service | ~200行 | ~300行 | ~400行 | +100% |
| SDK | 0 | ~250行 | ~250行 | +∞ |
| 表达式引擎 | 0 | 0 | ~200行 | +∞ |
| 规则引擎 | 0 | 0 | ~150行 | +∞ |
| **总计** | **~500行** | **~1050行** | **~1800行** | **+260%** |

---

## 六、业务效果提升

### 6.1 关键指标对比

| 指标 | 阶段一 | 阶段二 | 阶段三 | 提升幅度 |
|------|--------|--------|--------|----------|
| **上线周期** | 1-3个月 | 1-3人天 | 1-3人天 | **90%+** |
| **开发成本** | 每次需开发 | 零成本 | 零成本 | **100%** |
| **支持能力** | 基础配置 | +实验+标签 | +多源+表达式 | **持续扩展** |
| **数据源** | 1种 | 1种 | 4种 | **300%** |
| **触达方式** | 0种 | 2种 | 4种 | **100%** |

### 6.2 架构质量提升

| 维度 | 阶段一 | 阶段三 | 提升 |
|------|--------|--------|------|
| **内聚性** | 低（逻辑分散） | 高（充血模型） | 显著提升 |
| **耦合度** | 高（业务方依赖） | 低（配置化） | 显著降低 |
| **可扩展性** | 差（需改代码） | 好（配置即可） | 显著提升 |
| **可维护性** | 差（过程式代码） | 好（面向对象） | 显著提升 |
| **可测试性** | 差 | 好 | 显著提升 |

---

## 七、总结

### 7.1 演化核心思想

1. **从贫血到充血**：将业务逻辑从Service层下放到Entity，让实体拥有自我验证和行为能力
2. **从配置到采集**：引入SDK主动采集行为，替代业务方自行实现
3. **从单一到多元**：支持多种数据源，覆盖更多业务场景
4. **从静态到动态**：引入表达式引擎，支持动态规则配置
5. **从简单到丰富**：扩展触达形式，提升用户体验

### 7.2 关键设计决策

| 决策 | 选择 | 理由 |
|------|------|------|
| 表达式引擎 | AviatorScript | 轻量、高性能、易集成 |
| 数据存储 | MongoDB + TiDB | 满足聚合计算需求 |
| 架构模式 | 充血模型 | 高内聚、低耦合 |
| 扩展方式 | 配置化 | 无需改代码即可支持新场景 |

### 7.3 经验总结

1. **贫血模型是万恶之源**：导致业务逻辑分散，难以维护和复用
2. **充血模型是良药**：将行为封装在实体中，体现面向对象设计
3. **配置化优于编码**：通过表达式和配置支持业务变化，减少代码修改
4. **平台化思维**：从项目交付转向平台建设，提升整体效率

---

## 七、参考案例：爱奇艺积分系统

> 本章节整合爱奇艺积分系统的架构演进实践，重点介绍存储架构演进、数据一致性保障、灰度发布等关键设计。
> 
> 参考文章：https://mp.weixin.qq.com/s/vvSLW4EnsYEyHYIbGl8q2Q

---

### 7.1 存储架构演进：从分离到统一

#### 原有架构的问题

**分离式存储架构（MySQL + MongoDB）**：
- **MySQL**：存储积分总值（频繁读写）
- **MongoDB**：存储积分明细（海量数据）

**核心问题**：
1. **一致性问题**：总值与明细分离存储，需要分布式事务或双写补偿
2. **维护成本高**：需维护两个数据库体系，开发、运维、监控分离
3. **性能瓶颈**：MySQL单实例写入能力有限，横向扩展复杂

#### 演进后架构

**统一存储架构（MongoDB 7.0）**：
- **MongoDB 7.0**：统一存储总值 + 明细
- **优势**：简化架构、提升性能、保证一致性

#### 选型理由：为什么选择 MongoDB 7.0

| 能力要求 | MongoDB 7.0 优势 |
|---------|-----------------|
| **扩展能力** | 原生分片机制支持水平扩展，支持亿级用户并发访问 |
| **并发能力** | 支持高并发积分操作，整体写入吞吐能力提升明显 |
| **事务能力** | 支持多文档事务和可配置写入确认策略（majority） |
| **冗灾能力** | 高可用、自动故障转移、跨机房部署 |
| **建模能力** | 文档型存储天然适配多变的字段结构 |

#### 一致性策略

```java
// MongoDBConfig.java
public class MongoDBConfig {
    /**
     * 【核心】写入确认级别
     * 配置为majority，确保数据在主备节点间强一致传播
     * 用数据强一致性换取少量延迟波动（权衡合理）
     */
    private String writeConcern = "majority";
}
```

---

### 7.2 积分线模型设计

#### 核心概念

**业务层级结构**：
```
业务方（BusinessUnit）
  └── 业务线（BusinessLine）
       └── 积分线（PointLine）
            └── 用户积分账户（UserPointAccount）
```

**四大业务方**：
- 极速版
- 基线业务
- 国际业务
- 综合端业务

#### 积分线实体设计

```java
// PointLine.java
public class PointLine {
    private String lineCode;      // 积分线编码（唯一标识）
    private String businessUnit;  // 业务方编码
    private String businessLine;  // 业务线编码
    private String pointType;     // 积分类型
    private Integer expireType;   // 有效期类型
    private Integer expireDays;   // 有效期天数
    private Map<String, Object> config;  // 扩展配置
    
    // 充血模型方法
    public boolean isValid() { }
    public boolean isExpired(LocalDateTime pointTime) { }
    public boolean supportsBusiness(String businessUnit, String businessLine) { }
}
```

#### 用户积分账户实体设计

```java
// UserPointAccount.java
public class UserPointAccount {
    private Long userId;
    private Long pointLineId;
    private Long totalPoints;      // 积分总值
    private Long frozenPoints;     // 冻结积分
    private Long availablePoints;  // 可用积分
    private Long cumulativeEarned; // 累计获得
    private Long cumulativeConsumed; // 累计消耗
    private String extInfo;        // 【亮点】扩展信息，存储请求参数用于验证
    private Integer version;       // 【亮点】乐观锁版本号
    
    // 充血模型方法
    public boolean hasEnoughPoints(long points) { }
    public long addPoints(long points) { }
    public long deductPoints(long points) { }
    public long freezePoints(long points) { }
    public long unfreezePoints(long points) { }
}
```

---

### 7.3 数据一致性保障机制

#### 多重校验体系

**校验维度**：
1. **明细实验表 vs 明细对照表**（新旧明细对比）
2. **新总值表 vs 老总值表**（新旧总值对比）
3. **新总值表 vs 新明细表**（总值与明细汇总对比）

```java
// DataConsistencyChecker.java
public interface DataConsistencyChecker {
    /**
     * 执行全量一致性校验
     * 在正式切流前，对全量数据进行校验，确保100%一致
     */
    ConsistencyCheckResult checkAll();
    
    /**
     * 执行增量一致性校验
     * 在双写阶段，持续校验增量数据的一致性
     */
    ConsistencyCheckResult checkIncremental(String startTime, String endTime);
    
    /**
     * 修复不一致数据
     */
    RepairResult repairInconsistencies(List<InconsistencyRecord> inconsistencies);
}
```

#### 校验结果处理

- **任意不一致触发告警**
- **问题修复后循环验证**
- **全量校验达到100%一致后才允许切流**

---

### 7.4 灰度发布机制

#### 渐进式切流策略

**分批推进**：1% → 10% → 20% → 50% → 100%

```java
// GrayBatch.java
public class GrayBatch {
    // 预定义批次
    public static final GrayBatch BATCH_1 = new GrayBatch(1, "第一批", 1, "00", "小流量验证");
    public static final GrayBatch BATCH_2 = new GrayBatch(2, "第二批", 10, "00-09", "扩大验证");
    public static final GrayBatch BATCH_3 = new GrayBatch(3, "第三批", 20, "00-19", "进一步验证");
    public static final GrayBatch BATCH_4 = new GrayBatch(4, "第四批", 50, "00-49", "半量验证");
    public static final GrayBatch BATCH_5 = new GrayBatch(5, "第五批", 100, "00-99", "全量发布");
}
```

#### UID尾号切流算法

```java
// 根据用户ID的最后两位决定流量走向
public boolean isUserInGrayRange(Long userId) {
    String uidStr = userId.toString();
    String suffix = uidStr.substring(uidStr.length() - 2);
    int suffixNum = Integer.parseInt(suffix);
    return suffixNum < grayPercent;
}
```

#### 灰度管理器接口

```java
// GrayReleaseManager.java
public interface GrayReleaseManager {
    boolean isInGrayRelease(Long userId);     // 检查用户是否在新版本
    int getCurrentGrayPercent();               // 获取当前灰度比例
    boolean adjustGrayPercent(int percent);    // 调整灰度比例
    int advanceToNextBatch();                  // 推进到下一批次
    int rollbackToPreviousBatch();             // 回滚到上一批次
}
```

---

### 7.5 限流与熔断机制

#### 云配限流控制

**限流策略**：
1. **QPS限流**：限制每秒请求数
2. **并发限流**：限制同时处理的请求数
3. **热点限流**：针对热点用户或积分线进行限流
4. **熔断降级**：异常率达到阈值时自动熔断

```java
// RateLimiter.java
public interface RateLimiter {
    boolean tryAcquire(String key);           // 尝试获取许可
    boolean isLimited(String key);            // 检查是否被限流
    double getCurrentQps(String key);         // 获取当前QPS
    void updateConfig(RateLimitConfig config); // 动态更新配置
}
```

#### 限流配置

```java
// RateLimitConfig.java
public class RateLimitConfig {
    private LimitType limitType;      // QPS/CONCURRENCY/HOT_SPOT/CIRCUIT_BREAKER
    private int qpsThreshold;         // QPS阈值
    private int concurrencyThreshold; // 并发阈值
    private boolean enabled;          // 是否启用
    private String scope;             // global/user/pointLine
}
```

---

### 7.6 迁移收益总结

#### 服务稳定性增强 ⭐⭐⭐⭐⭐
- 接口超时499错误明显减少
- 接口成功率趋于稳定
- 毛刺波动现象基本消除
- 促销活动高峰期保持良好可用性

#### 系统并发能力增强 ⭐⭐⭐⭐⭐
- 支持水平扩展，应对高并发写入
- 支持多积分线并发访问
- 整体写入吞吐能力提升明显
- 原有MySQL锁竞争问题彻底解决

#### 数据一致性增强 ⭐⭐⭐⭐⭐
- 简化原本依赖分布式事务的复杂性
- 通过majority写入确认机制确保强一致性
- 极大降低因异步或双写不一致带来的数据风险

#### 开发维护效率增强 ⭐⭐⭐⭐⭐
- 从"双数据库、双接口逻辑"统一为"单一MongoDB模型"
- 开发侧仅需维护一套读写逻辑与数据结构
- 接口联调和问题排查成本大幅下降

---

## 八、总结

### 8.1 演化核心思想

1. **从贫血到充血**：将业务逻辑从Service层下放到Entity，让实体拥有自我验证和行为能力
2. **从配置到采集**：引入SDK主动采集行为，替代业务方自行实现
3. **从单一到多元**：支持多种数据源，覆盖更多业务场景
4. **从静态到动态**：引入表达式引擎，支持动态规则配置
5. **从简单到丰富**：扩展触达形式，提升用户体验
6. **从分离到统一**：统一存储架构，简化系统复杂度

### 8.2 关键设计决策

| 决策 | 选择 | 理由 |
|------|------|------|
| 表达式引擎 | AviatorScript | 轻量、高性能、易集成 |
| 数据存储 | MongoDB 7.0 | 统一存储、水平扩展、强一致性 |
| 架构模式 | 充血模型 | 高内聚、低耦合 |
| 扩展方式 | 配置化 | 无需改代码即可支持新场景 |
| 一致性策略 | majority写入确认 | 数据强一致性 |
| 发布策略 | 灰度切流 | 降低风险、快速回滚 |

### 8.3 经验总结

1. **贫血模型是万恶之源**：导致业务逻辑分散，难以维护和复用
2. **充血模型是良药**：将行为封装在实体中，体现面向对象设计
3. **配置化优于编码**：通过表达式和配置支持业务变化，减少代码修改
4. **平台化思维**：从项目交付转向平台建设，提升整体效率
5. **数据一致性优先**：用少量延迟换取数据强一致性，权衡合理
6. **渐进式发布**：灰度切流降低风险，确保系统稳定性

---

*文档完成时间：2026年2月*
