# MyMall 电商平台

Spring Boot 单体电商系统，用户端 + 管理端，涵盖商品管理、购物车、订单流程、高并发秒杀。

## 技术栈

| 技术 | 用途 |
|------|------|
| Spring Boot 4.0 + Java 17 | 核心框架 |
| MyBatis + PageHelper | ORM + 分页 |
| MySQL 8.0 | 关系型数据库 |
| Redis + Caffeine | 两级缓存（L1 本地 / L2 远程） |
| Redis + Redisson | 秒杀分布式锁 + 库存预扣 |
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
SeckillRequestConsumer（纯 Redis，毫秒级）
  Redisson 分布式锁串行化：
  清失败标记 → 查库存（key 缺失则 DB 兜底预热）
  → 库存不足 → Redis 失败标记
  → 已秒杀（userKey 存在）→ 跳过
  → 扣减 Redis 库存 + 标记用户 → 转发 SECKILL_QUEUE
  ↓
SeckillOrderConsumer（DB 落库，异步）
  校验地址/商品 → DB 唯一约束去重（重复则回补预扣）
  → 秒杀单占位 + 创建正式订单 → 条件扣减 DB 库存
  → 回写 orderId → 插入订单项 + 收货地址
  ↓ 异常 → 回补 Redis 预扣 + 失败标记 → 重抛进死信队列
SeckillDeadConsumer（死信兜底）→ 幂等回补
  ↓
前端轮询 GET /result → 失败标记 / DB 秒杀单
```

关键点：
- **防超卖**：Redisson 分布式锁 + DB 条件扣减（`stock_num >= count`）双重保证
- **幂等**：Redis `userKey`（带 TTL）+ DB 唯一约束 `(user_id, seckill_goods_id)`，消息重放不重复下单
- **一致性兜底**：死信队列回补预扣 + 定时对账修正（`Redis = DB - 未落库预扣`）
- **Lazy Queue** 消息直接落盘，不会 OOM
- **失败标记**写入 Redis（带 TTL），前端 1.5s 轮询获取明确结果

## 缓存策略

| 数据 | Caffeine (L1) | Redis (L2) | 失效策略 |
|------|:---:|:---:|------|
| 新品商品 | 10min | 1天 | 重置后写透两级缓存 + 广播清其它实例 L1 |
| 热销商品 | 10min | 30min | 重置后写透两级缓存 + 广播清其它实例 L1 |
| 推荐商品 | 10min | 6h | 重置后写透两级缓存 + 广播清其它实例 L1 |
| 分类树 | 30min | 30min | 增删改全清 |
| 秒杀库存 | ❌ | 2h | 增删改同步 |

> 配置变更采用**写透**（直接覆写逻辑过期包装），原子无空窗期；再通过 Redis Pub/Sub 广播清其它实例的本地缓存，各实例下次请求命中新的 L2。
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
