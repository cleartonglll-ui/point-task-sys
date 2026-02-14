一、 领域驱动设计（DDD）分析
在设计目录结构前，必须先明确业务边界和领域模型。

1. 领域划分
核心域：行为激励（核心业务逻辑，决定用户是否能获得奖励）。
支撑域：
积分账户（负责积分的存储、冻结、流水）。
勋章/成就（负责荣誉体系的定义与颁发）。
规则引擎（负责判断行为是否满足奖励条件）。
通用域：用户信息（依赖用户中心）、通知（依赖消息中心）。
2. 限界上下文
根据领域划分，拆分为以下微服务：

Reward-Engine-Service (奖励引擎服务)：核心协调者。负责接收用户行为事件，匹配规则，触发奖励发放。
Points-Wallet-Service (积分钱包服务)：负责积分的增删改查、余额管理、过期处理。
Achievement-Service (成就勋章服务)：负责勋章定义、用户勋章点亮与展示。
Rule-Engine-Service (规则配置服务)：负责运营配置奖励规则（如：“连续签到7天送100积分”）。
二、 微服务总体目录结构
采用 Maven 多模块结构，严格遵守 DDD 四层架构（接口层、应用层、领域层、基础设施层）。

text
复制
下载
online-education-reward-platform
│
├── pom.xml  -- 父级依赖管理
│
├── docs                     -- 项目文档
│   ├── architecture         -- 架构图
│   ├── sql                  -- 数据库脚本
│   └── api-spec             -- Swagger/OpenAPI 定义
│
├── common                   -- 公共基础模块
│   ├── common-core          -- 通用工具类, 枚举, 异常定义
│   ├── common-redis         -- Redis 配置与工具
│   ├── common-mq            -- RocketMQ/Kafka 封装
│   └── common-web           -- Web 拦截器, 统一返回体
│
├── service-reward-engine    -- 【核心服务】奖励引擎服务
│
├── service-points-wallet    -- 【核心服务】积分钱包服务
│
├── service-achievement      -- 【核心服务】成就勋章服务
│
└── service-rule-config      -- 【支撑服务】规则配置服务
三、 核心服务详细目录结构
以下展示最核心的三个服务的内部 DDD 目录结构。

1. 奖励引擎服务(行为计算)
这是系统的“大脑”，负责接收行为并协调奖励发放。

text
复制
下载
service-reward-engine
├── pom.xml
└── src/main/java/com/edu/reward
    │
    ├── api                     -- 【接口层 Interfaces】
    │   ├── controller
    │   │   ├── BehaviorEventController.java  -- 接收行为上报
    │   │   └── RewardTaskController.java     -- 奖励任务查询
    │   ├── dto                 -- 数据传输对象
    │   │   ├── BehaviorEventDTO.java
    │   │   └── RewardResultDTO.java
    │   └── assembler           -- DTO与领域对象转换器
    │
    ├── application             -- 【应用层 Application】
    │   ├── service
    │   │   ├── RewardProcessService.java     -- 奖励处理编排服务
    │   │   └── RuleMatchService.java         -- 规则匹配应用服务
    │   ├── event               -- 应用层事件处理
    │   │   └── RewardEventListener.java      -- 监听行为MQ消息
    │   └── assembler           -- 组装器
    │
    ├── domain                  -- 【领域层 Domain - 核心】
    │   ├── model               -- 领域模型/聚合根
    │   │   ├── aggregate
    │   │   │   └── RewardTask.java           -- 奖励任务聚合根
    │   │   ├── entity
    │   │   │   └── UserBehavior.java         -- 用户行为实体
    │   │   └── valueobject
    │   │       ├── RewardType.java           -- 奖励类型(积分/勋章)
    │   │       └── BehaviorType.java         -- 行为类型(签到/听课)
    │   ├── service             -- 领域服务
    │   │   ├── RewardCalculator.java         -- 奖励计算逻辑
    │   │   └── BehaviorValidator.java        -- 行为有效性校验
    │   ├── repository          -- 仓储接口
    │   │   └── IRewardTaskRepository.java
    │   └── event               -- 领域事件
    │       └── RewardGrantedEvent.java       -- 奖励发放成功事件
    │
    ├── infrastructure          -- 【基础设施层 Infrastructure】
    │   ├── repository          -- 仓储实现
    │   │   ├── RewardTaskRepositoryImpl.java
    │   │   └── mapper          -- MyBatis Mapper
    │   │       └── RewardTaskMapper.java
    │   ├── gateway             -- 外部服务调用
    │   │   ├── PointsGateway.java            -- 调用积分服务RPC
    │   │   └── RuleGateway.java              -- 调用规则服务RPC
    │   ├── config              -- 配置类
    │   └── util                -- 工具类
    │
    └── RewardApplication.java  -- 启动类
2. 积分钱包服务（触达）
负责积分这一“价值资产”的管理，要求高一致性。

text
复制
下载
service-points-wallet
└── src/main/java/com/edu/points
    ├── api
    │   ├── controller
    │   │   ├── PointsWalletController.java   -- 余额查询
    │   │   └── PointsTransactionController.java -- 流水查询
    │   └── dto
    ├── application
    │   ├── service
    │   │   ├── PointsAccountService.java     -- 账户操作服务
    │   │   └── PointsTransferService.java    -- 转账/支付服务
    │   └── event
    │       └── PointsEventListener.java      -- 监听奖励发放事件
    ├── domain
    │   ├── model
    │   │   ├── aggregate
    │   │   │   └── PointsAccount.java        -- 积分账户聚合根(含余额逻辑)
    │   │   ├── entity
    │   │   │   └── PointsTransaction.java    -- 积分流水实体
    │   │   └── valueobject
    │   │       ├── AccountStatus.java
    │   │       └── TransactionType.java      -- (收入/支出/冻结)
    │   ├── service
    │   │   └── PointsCalculator.java         -- 积分计算逻辑
    │   └── repository
    │       ├── IPointsAccountRepository.java
    │       └── IPointsTransactionRepository.java
    ├── infrastructure
    │   ├── repository
    │   │   └── PointsAccountRepositoryImpl.java
    │   └── mq
    │       └── PointsProducer.java           -- 发送积分变动消息
    └── PointsApplication.java
3. 规则配置服务
运营后台使用，用于配置复杂的奖励规则。

text
复制
下载
service-rule-config
└── src/main/java/com/edu/rule
    ├── api
    │   ├── controller
    │   │   └── RuleConfigController.java     -- 运营配置CRUD
    │   └── facade
    │       └── RuleQueryFacade.java          -- 供其他服务查询规则
    ├── application
    │   └── service
    │       └── RuleManagementService.java
    ├── domain
    │   ├── model
    │   │   ├── aggregate
    │   │   │   └── RewardRule.java           -- 规则聚合根
    │   │   ├── entity
    │   │   │   └── RuleCondition.java        -- 规则条件实体
    │   │   └── valueobject
    │   │       └── RuleStatus.java
    │   └── service
    │       └── RuleMatcher.java              -- 规则匹配领域逻辑
    └── infrastructure
        └── repository
四、 关键设计说明
1. 依赖关系与调用链路
调用链路： 用户行为 -> Reward Engine (接收行为) -> Rule Config (查询规则) -> Reward Engine (计算奖励) -> Points Wallet (RPC调用增加积分)。
解耦： Reward Engine 与 Points Wallet/Achievement 之间建议使用 MQ（消息队列） 进行解耦。
Reward Engine 发送 RewardGrantedEvent。
Points Wallet 监听该事件，执行积分入账。
2. 领域对象职责
聚合根：如 PointsAccount，必须保证积分变动的原子性。账户余额的修改必须通过聚合根的方法（如 increase(amount)），而不是直接 set 属性，以此封装业务规则（如余额不能为负）。
领域服务：如 RewardCalculator，当一个逻辑涉及多个实体或聚合，或者不属于单一实体职责时，放入领域服务。
3. 目录分层规范
Interfaces (api)：对外暴露端点，处理 HTTP 请求参数校验，DTO 转换。
Application (application)：事务控制，编排领域服务，不包含核心业务逻辑。
Domain (domain)：纯粹的业务逻辑，不依赖任何框架实现（如 Spring 注解尽量少用，保持 POJO 纯净），是系统的核心。
Infrastructure (infrastructure)：技术实现，数据库操作、RPC 调用、Redis 实现等。
4. 公共模块设计
common-core 中应定义通用的枚举，如 BehaviorType (SIGN_IN, WATCH_COURSE, SUBMIT_HOMEWORK)，供所有服务引用，确保业务语言的一致性。
五、 总结
该目录结构设计体现了 DDD 的精髓：

高内聚：业务逻辑集中在 Domain 层，不散落在 Controller 或 Service 中。
低耦合：微服务之间通过 RPC 或 MQ 通信，内部各层通过接口解耦。
可扩展：未来增加新的奖励方式（如“优惠券”），只需增加新的微服务或新的领域对象，不影响现有积分和勋章的核心逻辑。