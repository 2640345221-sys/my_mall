# MyMall 电商平台

Spring Boot 单体电商系统，用户端 + 管理端，涵盖商品管理、购物车、订单流程、高并发秒杀。

## 技术栈

| 技术 | 用途 |
|------|------|
| Spring Boot 4.0 + Java 17 | 核心框架 |
| MyBatis + PageHelper | ORM + 分页 |
| MySQL 8.0 | 关系型数据库 |
| Redis + Caffeine | 两级缓存（L1 本地 / L2 远程） |
| Redis + Lua | 秒杀库存原子扣减 |
| RabbitMQ | 秒杀异步削峰 |
| Hutool IdUtil | 雪花算法订单号 |
| JWT (jjwt) | 双端身份认证 |
| Spring AOP | 操作日志、字段自动填充 |
| 阿里云 OSS | 文件上传 |
| SpringDoc OpenAPI 3.0 | API 文档 |

## 项目结构

```
my_mall/
├── controller/
│   ├── admin/          # 管理端
│   ├── common/         # 公共（文件上传）
│   └── user/           # 用户端
├── consumer/           # RabbitMQ 消费者
├── service/            # 业务逻辑
├── mapper/             # MyBatis 数据访问
├── entity/
│   ├── po/             # 持久化对象
│   ├── dto/            # 数据传输对象
│   └── vo/             # 视图对象
├── config/             # 配置
├── interceptor/        # JWT 拦截器
├── aspect/             # AOP 切面
├── exception/          # 自定义异常
├── handler/            # 全局异常处理
├── task/               # 定时任务
├── utils/              # 工具类
└── resources/
    ├── mapper/         # MyBatis XML
    ├── lua/            # Lua 脚本（秒杀）
    └── application*.yml
```

## 核心功能

### 用户端
- 注册 / 登录（JWT）
- 首页（新品 / 热销 / 推荐）
- 商品浏览 / 搜索
- 购物车
- 收货地址
- 下单 / 支付 / 取消 / 确认收货
- 限时秒杀

### 管理端
- 商品管理（OSS 图片上传）
- 分类管理（三级分类树）
- 首页配置（新品 / 热销 / 推荐，手动刷新缓存）
- 订单管理（配货 / 出库 / 关闭）
- 用户管理（禁用 / 启用）
- 秒杀商品管理

## 秒杀架构

```
用户请求
  ↓
Controller → RabbitMQ 异步入队（削峰）
  ↓
SeckillRequestConsumer: Lua 脚本原子扣减 Redis 库存
  → -1: 库存不足  → Redis 失败标记  → 前端轮询返回失败
  → -2: 重复秒杀  → 直接返回
  → ≥0: 成功      → 写 DB + RabbitMQ 转发
  ↓
SeckillOrderConsumer: 创建订单
  ↓
前端轮询 GET /result → 查 Redis 失败标记 + DB 订单
```

关键点：
- **Lua 脚本**一次网络往返完成"检查库存 + 去重 + 扣减"，原子执行
- **Lazy Queue**消息直接落盘，不会 OOM
- **失败标记**写入 Redis（带 TTL），前端 1.5s 轮询获取明确结果

## 缓存策略

| 数据 | Caffeine (L1) | Redis (L2) | 失效策略 |
|------|:---:|:---:|------|
| 新品商品 | 10min | 1天 | 清空 + 预热 |
| 热销商品 | 10min | 30min | 清空 + 预热 |
| 推荐商品 | 10min | 6h | 清空 + 预热 |
| 分类树 | 30min | 30min | 增删改全清 |
| 秒杀库存 | ❌ | 2h | 增删改同步 |

> 秒杀不走 Caffeine：多实例部署时本地缓存会导致超卖。

## 前端

独立 Vue3 项目 `my_mall_vue3`：
- Vue 3 + Pinia + Element Plus
- 用户端（移动端风格） + 管理端（后台风格）
- Token 自动携带、秒杀下单轮询、价格格式化

## 快速启动

1. 创建数据库，导入 `my_mall.sql`
2. 启动 MySQL / Redis / RabbitMQ
3. 配置 `application-dev.yml`（数据库密码等）
4. 启动后端
5. 启动前端：`cd my_mall_vue3 && npm i && npm run dev`

API 文档：`http://localhost:8080/swagger-ui/index.html`

## Docker 部署

```
docker compose up
```

- 密钥通过 `.env` 注入，不进镜像
- `application-docker.yml` 用 `${}` 占位符
- `.dockerignore` 排除敏感文件
