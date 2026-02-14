# 行为奖励系统 - 四微服务架构 (DDD规范版)

## 🏗️ DDD目录结构说明

本项目严格遵循DDD四层架构设计，每个微服务包含以下标准分层：

```
service-xxx/
└── src/main/java/com/user/growth/xxx/
    ├── api/                    # 接口层
    │   ├── controller/         # REST控制器
    │   └── dto/                # 数据传输对象
    ├── application/            # 应用层
    │   └── service/            # 应用服务(编排领域服务)
    ├── domain/                 # 领域层
    │   ├── aggregate/          # 聚合根(核心业务逻辑)
    │   ├── entity/             # 实体
    │   ├── valueobject/        # 值对象
    │   ├── service/            # 领域服务
    │   └── repository/         # 仓储接口
    ├── infrastructure/         # 基础设施层
    │   └── repository/         # 仓储实现
    └── XxxApplication.java     # 启动类
```

## 📁 四个微服务详情

### 1. event-collection-service (端口8081)
**限界上下文：行为事件上下文**

**核心聚合根：**
- `BehaviorEvent` - 行为事件聚合根

**主要职责：**
- 接收客户端SDK上报的用户行为
- 事件清洗和标准化
- 存储到MongoDB
- 发送事件到Kafka

### 2. task-config-service (端口8082)
**限界上下文：任务规则上下文**

**核心聚合根：**
- `TaskRule` - 任务规则聚合根

**主要职责：**
- 任务规则配置管理
- AviatorScript表达式引擎集成
- 任务状态管理
- 精细化运营配置

### 3. event-calculation-service (端口8083)
**限界上下文：奖励计算上下文**

**核心聚合根：**
- `RewardCalculation` - 奖励计算聚合根

**主要职责：**
- 监听Kafka事件
- 匹配任务规则
- 执行奖励计算(集成Aviator)
- Redis+Lua防超发
- 发送奖励发放消息

### 4. reward-delivery-service (端口8084)
**限界上下文：积分账户上下文**

**核心聚合根：**
- `PointAccount` - 积分账户聚合根

**主要职责：**
- 积分账户管理
- 积分发放/扣减
- 多样化触达(Push/短信/MQ)
- 幂等性保障

## 工作流程（方法调用链）

### 1. 用户行为触发奖励全流程

```
客户端SDK采集行为事件
    ↓
BehaviorEventController.collectEvent()  [接口层]
    ↓
EventCollectionService.collectEvent()   [应用层]
    ↓
BehaviorEvent.isValid()                 [领域层-聚合根方法]
    ↓
IBehaviorEventRepository.save()         [领域层-仓储接口]
    ↓
BehaviorEventRepositoryImpl.save()      [基础设施层-仓储实现]
    ↓
MongoDB存储
    ↓
------------------------------
Kafka消息消费
    ↓
RewardCalculationService.processEvent() [应用层]
    ↓
TaskRuleRepository.findByEventType()    [领域层]
    ↓
AviatorExpressionEngine.execute()       [领域服务]
    ↓
Redis+Lua原子操作(防超发)
    ↓
------------------------------
RewardDeliveryService.deliverReward()   [应用层]
    ↓
PointAccount.addPoints()                [领域层-聚合根方法]
    ↓
TouchNotificationService.notifyUser()   [领域服务]
```

### 2. 任务配置流程

```
TaskConfigController.createTask()       [接口层]
    ↓
TaskConfigService.createTask()          [应用层]
    ↓
TaskRule.isValid()                      [领域层-聚合根方法]
    ↓
ITaskRuleRepository.save()              [领域层]
    ↓
TaskRuleRepositoryImpl.save()           [基础设施层]
    ↓
MySQL存储
```

### 3. 防超发机制(DDD视角)

```
RewardCalculation.aggregate.calculateReward()  [聚合根方法]
    ↓
构建Redis Key: point:account:{userId}
    ↓
构建幂等Key: reward:idempotent:{eventId}
    ↓
执行Lua脚本(原子操作):
    1. 检查幂等键
    2. 获取当前积分
    3. 检查上限
    4. 增加积分
    5. 设置幂等键
    ↓
PointAccount聚合根保证一致性
```

## DDD设计亮点

### 1. 聚合根设计
- **BehaviorEvent**: 保证事件数据完整性
- **TaskRule**: 封装任务规则业务逻辑
- **PointAccount**: 保证积分账户一致性
- **RewardCalculation**: 协调奖励计算流程

### 2. 分层职责清晰
- **接口层**: 处理HTTP请求、参数校验、DTO转换
- **应用层**: 编排领域服务、处理事务边界
- **领域层**: 纯粹业务逻辑、不依赖技术框架
- **基础设施层**: 技术实现、数据库操作

### 3. 限界上下文明确
- 行为事件上下文
- 任务规则上下文
- 奖励计算上下文
- 积分账户上下文

### 4. 领域服务使用场景
- Aviator表达式引擎服务
- 触达通知服务
- 规则匹配服务

## 技术实现

### 核心组件
- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3
- MongoDB (行为事件存储)
- MySQL (业务数据)
- Redis + Lua (防超发)
- Kafka (服务间通信)
- AviatorScript (动态规则引擎)

### 依赖注入
```
接口层 → 应用层 → 领域层(接口) → 基础设施层(实现)
Controller → ApplicationService → RepositoryInterface → RepositoryImpl
```

## 启动说明

每个服务都是独立的SpringBoot应用，可单独启动：

1. `event-collection-service` (端口8081)
2. `task-config-service` (端口8082)
3. `event-calculation-service` (端口8083)
4. `reward-delivery-service` (端口8084)

## 优势对比

| 方面 | 原始结构 | DDD结构 | 说明 |
|------|----------|---------|------|
| 代码组织 | 扁平化 | 分层清晰 | 符合DDD规范 |
| 业务逻辑 | 散落各处 | 聚焦领域层 | 高内聚 |
| 技术依赖 | 混合业务 | 基础设施层隔离 | 低耦合 |
| 扩展性 | 困难 | 容易 | 可插拔设计 |
| 可维护性 | 一般 | 优秀 | 职责分明 |
| 团队协作 | 冲突多 | 冲突少 | 分层开发 |

## 技术栈

- Spring Boot 2.7.18
- MyBatis-Plus 3.5.3
- MySQL 8.0
- Redis
- MongoDB
- Kafka
- AviatorScript (任务配置服务)
- Sentinel (限流熔断)
- Prometheus + Grafana (监控)

## 环境准备

### 1. 安装依赖服务

```bash
# 启动MySQL (端口3306)
# 创建数据库: behavioral_reward

# 启动Redis (端口6379)

# 启动MongoDB (端口27017)

# 启动Kafka (端口9092)
```

### 2. 初始化数据库

执行SQL脚本：
```bash
mysql -u root -p < sql/init.sql
```

## 启动服务

### 方式一：IDE启动 (推荐开发)

分别启动4个SpringBoot应用：

1. `event-collection-service/src/main/java/com/user/growth/collection/EventCollectionApplication.java`
2. `task-config-service/src/main/java/com/user/growth/task/TaskConfigApplication.java`
3. `event-calculation-service/src/main/java/com/user/growth/calculation/EventCalculationApplication.java`
4. `reward-delivery-service/src/main/java/com/user/growth/delivery/RewardDeliveryApplication.java`

### 方式二：命令行启动

```bash
# 编译打包
mvn clean package

# 分别启动各服务
cd event-collection-service
java -jar target/event-collection-service-1.0.0.jar

cd ../task-config-service
java -jar target/task-config-service-1.0.0.jar

cd ../event-calculation-service
java -jar target/event-calculation-service-1.0.0.jar

cd ../reward-delivery-service
java -jar target/reward-delivery-service-1.0.0.jar
```

## 服务端口

| 服务名称 | 端口 | 功能 |
|---------|------|------|
| event-collection-service | 8081 | 采集用户行为事件 |
| task-config-service | 8082 | 任务规则配置 |
| event-calculation-service | 8083 | 积分计算、防超发 |
| reward-delivery-service | 8084 | 奖励发放、触达 |

## 核心特性

### 1. DDD领域驱动设计
- 四个限界上下文：行为事件、任务规则、积分账户、奖励发放
- 充血模型设计
- 分层架构：接口层、应用层、领域层、仓储层

### 2. AviatorScript表达式引擎
- 支持动态任务规则配置
- 复杂条件判断
- 示例：`user.level >= 5 and event.watchDuration >= 300`

### 3. Redis+Lua原子操作
- 防止积分超发
- 幂等性保障
- 高并发支持

### 4. 系统防护
- Sentinel限流熔断
- Prometheus监控埋点
- 全链路日志

### 5. 多样化触达方式
- Push推送
- 短信通知
- MQ透传
- 自定义弹窗

## 业务流程

1. 客户端SDK采集用户行为事件 → 发送到事件采集服务
2. 事件采集服务清洗标准化 → 存储到MongoDB → 发送Kafka
3. 事件计算服务监听Kafka → 匹配任务规则 → 计算奖励 → 防超发校验
4. 奖励触达服务接收计算结果 → 发放奖励 → 多渠道触达用户

## API接口

### 事件采集服务
```
POST /api/v1/events/collect        # 采集事件
POST /api/v1/events/collect/batch  # 批量采集
GET  /api/v1/events/health         # 健康检查
```

### 任务配置服务
```
POST   /api/v1/tasks               # 创建任务
PUT    /api/v1/tasks/{id}          # 更新任务
GET    /api/v1/tasks/{id}          # 获取任务详情
GET    /api/v1/tasks/event-type/{eventType}  # 根据事件类型查询任务
GET    /api/v1/tasks/visible       # 获取用户可见任务
PATCH  /api/v1/tasks/{id}/status   # 启用/禁用任务
DELETE /api/v1/tasks/{id}          # 删除任务
GET    /api/v1/tasks/health        # 健康检查
```

## 注意事项

1. 各服务需要独立的数据库连接池配置
2. Kafka Topic需要提前创建
3. Redis需要开启持久化
4. 建议部署时使用Docker容器化
5. 生产环境需配置Sentinel Dashboard

## 监控

访问各服务健康检查：
- http://localhost:8081/actuator/health
- http://localhost:8082/actuator/health
- http://localhost:8083/actuator/health
- http://localhost:8084/actuator/health

Prometheus指标：
- http://localhost:8081/actuator/prometheus
- http://localhost:8082/actuator/prometheus
- http://localhost:8083/actuator/prometheus
- http://localhost:8084/actuator/prometheus


请完成一个线上教育平台的行为奖励系统，按照DDD 领域驱动设计与微服务架构，在四个文件夹下完成可直接运行的 Java 后端项目代码（可修改文件结构和文件内容）。

业务场景：海外线上教育平台，用户行为包含上课签到、完成作业、观看视频、课堂互动、课后答题、邀请好友等，完成行为自动发放积分、金币、或成就等。

# 架构要求
## 逻辑四层：任务配置层、精细化运营层、采集计算层、奖励触达层
## 物理服务拆分：事件采集服务、事件计算服务、任务配置服务、奖励触达服务(四个服务按DDD完成设计)
## DDD 设计：划分限界上下文（行为事件上下文、积分账户上下文、任务规则上下文、奖励发放上下文），包含领域模型、领域服务、仓储层、应用层、接口层
## 技术栈：SpringBoot、MyBatis-Plus、MySQL、Redis+Lua 原子操作、Kafka (MQ)、Sentinel (限流熔断降级)、Prometheus+Grafana 监控、全链路日志

## 四个服务的内容：
    1. 前端（客户端）SDK采集用户行为，采集服务清洗标准化后存入 MongoDB 并发送 MQ
    2. 计算服务：监听MQ，根据任务配置进行积分计算，异步执行，支持高并发，防止超发 / 重复发放
    2. 任务配置服务，引入AviatorScript 表达式引擎实现复杂规则动态计算；丰富触达形式（自定义弹窗、Push、短信、MQ 透传），任务层支持事件与任务 N:M 关联、多类型奖励（积分 / 优惠券 / 实物等）和精细化运营（基于用户标签做精细化运营、差异化任务推荐）。
    4. 奖励触达服务，集中实现幂等（看看是在计算服务还是触达服务实现好）
    5. 系统防护：限流、熔断、降级、集群隔离
    6. 双重监控：系统指标（CPU / 内存 / 队列积压）+ 行为监控 + 全链路日志
代码要求：结构清晰、注释详尽，DDD架构

不确定的内容请参考 https://juejin.cn/post/7229131003040989243?share_token=fb12f450-b496-486c-b143-541f154cdb48 或看看其他互联网公司的解决方案。