# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在本仓库中工作时提供指导。

## 项目概述

**easegen-admin** 是一个基于 ruoyi-vue-pro 框架构建的数字人课程创作平台。它是一个 Spring Boot 3 + JDK 17 的单体应用，采用模块化 Maven 结构，使用 MyBatis-Plus 作为 ORM 框架，Redis 用于缓存，MySQL 用于数据持久化。

**前端仓库**: https://github.com/taoofagi/easegen-front
**部署文档**: https://ozij45g3ts.feishu.cn/docx/EgS3dm1HtoKOPkxReEQcn3MCncg

## 常用命令

### 构建和运行
```bash
# 清理构建（跳过测试）
mvn clean install -DskipTests

# 运行应用（默认端口: 48080）
cd yudao-server
mvn spring-boot:run

# 或在 IDE 中直接运行主类
cn.iocoder.yudao.server.YudaoServerApplication
```

### 测试
```bash
# 运行所有测试
mvn test

# 运行特定模块的测试
cd yudao-module-digitalcourse/yudao-module-digitalcourse-biz
mvn test

# 运行单个测试类
mvn test -Dtest=YourTestClass

# 使用特定的 Spring profile 运行
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Docker 构建
```bash
# 使用 Docker 中的 Maven 构建 JAR
docker volume create --name yudao-maven-repo
docker run -it --rm --name yudao-maven \
    -v yudao-maven-repo:/root/.m2 \
    -v $PWD:/usr/src/mymaven \
    -w /usr/src/mymaven \
    maven mvn clean install package '-Dmaven.test.skip=true'

# 构建并启动所有服务
docker compose --env-file docker.env up -d

# 构建特定的后端镜像
docker build -t registry.cn-hangzhou.aliyuncs.com/easegen/easegen-admin:v0.2.6 .
```

### 开发
```bash
# 检查代码风格并编译
mvn compile

# 生成 Mapper XML 文件（如果需要）
mvn mybatis-generator:generate

# 访问 Swagger API 文档
http://localhost:48080/swagger-ui

# 访问 Druid SQL 监控
http://localhost:48080/druid/
```

## 架构概览

### 模块结构

这是一个**模块化单体架构**，具有以下关键概念：

1. **yudao-server**: 空壳容器，通过 Maven 依赖聚合所有业务模块。这是可运行的 Spring Boot 应用程序。

2. **yudao-framework**: 自定义 Spring Boot starters（共 14 个），提供横切关注点：
   - `yudao-spring-boot-starter-mybatis`: 数据库访问（含 BaseMapperX）
   - `yudao-spring-boot-starter-security`: 认证/授权
   - `yudao-spring-boot-starter-redis`: 缓存和会话
   - `yudao-spring-boot-starter-web`: Web 层增强
   - `yudao-spring-boot-starter-biz-*`: 业务工具（数据权限、租户等）

3. **业务模块**遵循一致的 2-3 子模块模式：
   - `yudao-module-xxx-api`: 模块间通信的接口（Feign 客户端、DTOs）
   - `yudao-module-xxx-biz`: 实现（controllers、services、DAL）
   - `yudao-module-xxx-starter`（可选）: 自动配置

### 活跃的业务模块

- **yudao-module-system**: 核心功能（用户、角色、权限、菜单、部门）
- **yudao-module-infra**: 基础设施（文件存储、配置、日志、任务、API 文档）
- **yudao-module-digitalcourse**: ⭐ 数字课程创建的主要业务逻辑
- **yudao-module-ai**: AI 模型集成（OpenAI、千帆、智谱、DeepSeek、Midjourney、Suno）
- **yudao-module-member**: 会员/用户管理
- **yudao-module-pay**: 支付集成

**已注释的模块**（为了加快编译速度）: bpm、report、mp、mall、crm、erp、iot

### 分层架构模式

每个业务模块都遵循以下结构：
```
controller (admin/app 包)
    ↓ @RestController, 验证, VO 转换
service (接口 + 实现)
    ↓ 业务逻辑, 事务管理
dal
    ├── dataobject (实体类，表映射)
    ├── mysql (Mapper 接口，继承 BaseMapperX)
    └── redis (可选，Redis DAO)
```

**关键约定：**
- Controller 使用 `CommonResult<T>` 作为 API 响应
- 使用 `@Tag` 和 `@Operation` 注解生成 Swagger 文档
- 通过 `BeanUtils` 或 MapStruct 进行 VO/DO 转换
- 所有 Mapper 继承 `BaseMapperX<T>`，提供增强的查询方法和分页功能

### 数据访问层

**BaseMapperX** 扩展了 MyBatis-Plus，提供：
- `selectPage(PageParam, Wrapper)`: 自动分页，返回 `PageResult<T>`
- `selectOne(Wrapper)`: 单条记录查询
- `selectList(Wrapper)`: 列表查询
- 通过 MPJLambdaWrapper 支持 JOIN 查询
- 自动处理逻辑删除、多租户、数据权限

使用示例：
```java
public interface CoursesMapper extends BaseMapperX<CoursesDO> {
    default PageResult<CoursesDO> selectPage(CoursesPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CoursesDO>()
            .likeIfPresent(CoursesDO::getName, reqVO.getName())
            .eqIfPresent(CoursesDO::getStatus, reqVO.getStatus())
            .orderByDesc(CoursesDO::getId));
    }
}
```

### 配置管理

- **application.yaml**: 基础配置（激活的 profile、通用设置）
- **application-dev.yaml**: 开发环境设置
- **application-local.yaml**: 本地开发（数据库、Redis、MQ 连接）
- **application-prod.yaml**: 生产环境（不在仓库中）

**Profile 选择**: 在 application.yaml 中设置 `spring.profiles.active`（默认: dev）

### API 设计模式

Controller 按访问类型分为：
- **admin**: 后台管理 API（`@RequestMapping("/digitalcourse/xxx")`）
- **app**: 前端用户 API

标准的 Controller 方法：
- `create*`: POST，返回 ID
- `update*`: PUT，返回成功布尔值
- `delete*`: DELETE，返回成功布尔值
- `get*`: GET，通过 ID 获取单条记录
- `get*Page`: GET，带分页
- `list*`: GET，不分页的列表
- `export*`: GET，返回 Excel 文件

## 关键技术

- **Java 17** with Spring Boot 3.3.4
- **MyBatis-Plus 3.5.8** with MyBatis-Plus-Join 用于复杂查询
- **Redis** via Spring Data Redis with Redisson
- **MySQL 8.0+**（支持 PostgreSQL、Oracle、DM、人大金仓、OpenGauss）
- **Swagger 3** (Knife4j) 用于 API 文档
- **Druid** 用于连接池和 SQL 监控
- **Quartz** 用于定时任务
- **Hutool** 用于工具函数
- **MapStruct** 用于 DTO/VO/DO 转换
- **Spring AI** 用于 AI 模型集成
- **Lombok** 用于减少样板代码

## 重要模式和约定

### 模块通信
- 永远不要直接引用其他模块的 Service 实现
- 使用 `-api` 模块的接口/DTO 进行模块间调用
- 示例：`yudao-module-infra-api` 提供 `ConfigApi` 供 digitalcourse 模块使用

### 实体命名
- **DO** (Data Object): 数据库实体，带 MyBatis-Plus 注解
- **VO** (View Object): API 响应对象
- **ReqVO**: API 请求对象（通常带验证注解）
- **RespVO**: API 响应对象
- **PageReqVO**: 继承 `PageParam` 用于分页查询

### 数据库约定
- 表名使用 snake_case（如：`digital_course_courses`）
- ID 字段使用 `@TableId(type = IdType.NONE)`（根据数据库类型自动配置）
- 通过 `deleted` 字段实现逻辑删除（1=已删除，0=活跃）
- 审计字段：`create_time`、`update_time`、`creator`、`updater`
- 通过 `tenant_id` 字段支持多租户（当前已禁用：`yudao.tenant.enable=false`）

### 安全和权限
- 基于 JWT 的认证，使用 Spring Security
- 权限注解：`@PreAuthorize("@ss.hasPermission('module:entity:operation')")`
- Admin API 默认需要认证
- 某些 API 通过 `yudao.security.permit-all_urls` 配置豁免

### 错误处理
- 所有 API 响应使用 `CommonResult<T>` 包装
- 标准方法：`success(data)`、`error(code, message)`
- 在 framework 层进行全局异常处理
- 每个模块在 `ErrorCodeConstants` 中定义自定义错误码

## 数字课程模块详情

**yudao-module-digitalcourse** 是核心业务模块，包含以下主要实体：
- **Courses**: 课程元数据（名称、时长、状态）
- **CoursePpts**: 课程的 PPT/课件
- **CourseScenes**: 课程中的视频场景
- **CourseSceneAudios**: 场景的音频轨道
- **CourseMedia**: 媒体资源（视频、图片）
- **Backgrounds**: 背景图片/视频
- **Templates**: 课程模板
- **Voices**: 数字人的语音配置

该模块集成了：
- AI 服务用于脚本生成
- 文件存储（infra 模块）用于媒体
- 外部 API 用于数字人视频生成

## 开发工作流

1. **添加新实体**：
   - 在 `dal/dataobject` 中创建 DO 类
   - 在 `dal/mysql` 中创建 Mapper 接口，继承 `BaseMapperX`
   - 在 `service` 中创建 Service 接口和实现
   - 在 `controller/*/vo` 中创建 ReqVO/RespVO
   - 创建包含 CRUD 操作的 Controller
   - 在 `ErrorCodeConstants` 中添加错误码
   - 在 system 模块中添加菜单和权限

2. **数据库变更**：
   - 手动执行 SQL 脚本（不使用 Flyway/Liquibase）
   - 更新相应的 DO 类
   - 如需要，增加版本号

3. **添加 API 端点**：
   - 在 Controller 类上使用 `@Tag` 进行 Swagger 分组
   - 在方法上使用 `@Operation` 生成 API 文档
   - 使用 `@Valid` 和 Jakarta 验证来验证请求参数
   - 返回 `CommonResult<T>` 包装

4. **模块依赖**：
   - 编辑根目录的 `pom.xml` 添加/移除模块
   - 编辑 `yudao-server/pom.xml` 激活模块
   - 注释掉不需要的模块以加快编译速度

## 环境配置

**必需的服务：**
- MySQL 8.0+ 在 `127.0.0.1:3306`（数据库: `easegen`，用户: `easegen`，密码: `123456`）
- Redis 在 `127.0.0.1:6379`（密码: `123456`）

**可选的服务：**
- RocketMQ 在 `127.0.0.1:9876`
- RabbitMQ 在 `127.0.0.1:5672`
- Kafka 在 `127.0.0.1:9092`

**配置文件位置：**
- 本地开发：`yudao-server/src/main/resources/application-local.yaml`
- 在 `application.yaml` 第 6 行修改 profile：`spring.profiles.active`

## API 访问

- **Admin API**: `http://localhost:48080/admin-api/**`
- **App API**: `http://localhost:48080/app-api/**`
- **Swagger UI**: `http://localhost:48080/swagger-ui`
- **API Docs (JSON)**: `http://localhost:48080/v3/api-docs`

## 故障排查

- 如果启动失败，请阅读：https://doc.iocoder.cn/quick-start/（在主类中提到了 6 次）
- 检查 MySQL 和 Redis 是否正在运行并且可访问
- 验证 `application-local.yaml` 中的数据库/Redis 凭据是否正确
- 对于编译问题，确保使用 JDK 17（检查 `JAVA_HOME`）
- Maven 构建错误：尝试 `mvn clean` 然后重新构建
- 对于"表未找到"错误：从部署文档导入 SQL
