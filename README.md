# Dyna-Guard - 动态校验框架

Dyna-Guard 是一个基于 Java 的动态校验框架，支持多种脚本语言和规则引擎。它提供了一套灵活的校验链机制，允许开发者通过配置化的方式定义和执行复杂的业务校验逻辑。

## 技术文档

https://www.yuque.com/yuqueyonghuqdqyqs/kswtr7/nqb761u5ni0dgqkg

## 项目结构

```
dyna-guard/
├── dyna-guard-core/           # 核心模块，包含校验引擎、规则解析等核心功能
└── dyna-guard-spring-boot-starter/  # Spring Boot Starter，提供自动配置和集成支持
```

## 功能特性

- **多规则引擎支持**：支持 Groovy、JavaScript、Python、QLExpress 和 Spring Expression Language (SpEL)
- **灵活的校验链机制**：通过配置定义校验节点，支持多种数据源（JSON、XML、SQL）
- **Spring Boot 集成**：提供 Starter 模块，开箱即用
- **动态规则加载**：支持从本地文件、数据库等多种来源加载校验规则
- **熔断机制**：内置计数器熔断器，防止系统过载
- **AOP 拦截**：基于注解的方法级校验拦截

## 安装使用

### Maven 依赖

```xml

<dependency>
    <groupId>com.easytu</groupId>
    <artifactId>dyna-guard-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 基本配置

在 `application.yml` 中添加配置：

```yaml
validation:
  # 解析文件解析器
  parser: sql,xml,json
  # 需要拦截的类，如果想使用方法级别校验，请将此参数设置为需要拦截的类名称
  validation-method: **Service
  # 存放校验链文件
  chain-file-path: classpath:chain/*Chain.xml,classpath:chain/*Chain.json
```

### 使用示例

1.使用json中的校验本流程进行验证

在需要校验的方法上添加 [@DynamicGuard]
注解：

```java

@Service("userService")
public class UserService {

    @DynamicGuard(group = "json", chainId = "user.create")
    public User createUser(CreateUserRequest request) {
        // 方法实现
        return user;
    }
}
```

2. 定义校验链配置（JSON格式）：

```json
[
  {
    "chainId": "user.create",
    "node": [
      {
        "order": 1,
        "script": "request != null && request.name != null",
        "language": "spel",
        "message": "参数不能为空",
        "fastFail": true
      },
      {
        "order": 2,
        "script": "request.phone matches '^1[3-9]\\d{9}$'",
        "language": "spel",
        "message": "手机号格式不正确",
        "fastFail": true
      }
    ]
  }
]
```

## 扩展开发

### 自定义校验器

实现 [Validator]
接口并注册到 SPI：

```java
public class CustomValidator extends BaseValidator {
    @Override
    public Boolean validate(String script, ValidationContext context) {
        // 实现校验逻辑
        return true;
    }

    @Override
    public String getLanguage() {
        return "custom";
    }
}
```

在 `META-INF/services/com.easytu.dynaguard.core.engine.Validator` 文件中添加实现类：

```
com.example.CustomValidator
```

## 技术架构

Dyna-Guard 采用模块化设计，核心模块与 Spring Boot Starter 分离，便于在不同环境中使用：

1. **核心模块** (`dyna-guard-core`) 提供基础的校验能力
2. **Spring Boot 集成模块** (`dyna-guard-spring-boot-starter`) 提供自动配置和 Spring 集成

框架通过 SPI (Service Provider Interface) 机制实现插件化扩展，支持动态加载不同的校验器、解析器等组件。

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进 Dyna-Guard。

## 许可证

[待补充具体许可证信息]

## 联系方式

[wd826209152]