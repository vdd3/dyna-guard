# dyna-guard - 动态校验框架

dyna-guard 是一个基于 Java 的动态校验框架，支持多种脚本语言和规则引擎。提供了一套灵活的校验链机制，允许开发者通过配置化的方式定义和执行复杂的业务校验逻辑。

## 技术文档 [点击进入](https://www.yuque.com/yuqueyonghuqdqyqs/kswtr7)

## 示例项目 [点击进入](https://github.com/vdd3/dyna-guard-example)

## 项目结构

```
dyna-guard/
├── dyna-guard-core/                 # 核心模块，包含校验引擎、规则解析等核心功能
├── dyna-guard-engine/               # 引擎模块，包含不同的规则引擎
└── dyna-guard-spring-boot-starter/  # Spring Boot Starter，提供自动配置和集成支持
```

## 技术架构

dyna-guard 采用模块化设计，核心模块与 Spring Boot Starter 分离，便于在不同环境中使用：

1. **核心模块** (`dyna-guard-core`) 提供基础的校验能力
2. **Spring Boot 集成模块** (`dyna-guard-spring-boot-starter`) 提供自动配置和 Spring 集成
3. **引擎模块** (`dyna-guard-engine`) 提供不同的规则引擎，如 Groovy、JavaScript和Aviator

框架通过 SPI (Service Provider Interface) 机制实现插件化扩展，支持动态加载不同的校验器、解析器等组件。

## 功能特性

- **多规则引擎支持**：支持 Groovy、JavaScript、QLExpress4 和 Aviator
- **灵活的校验链机制**：通过配置定义校验节点，支持多种数据源（JSON、XML、SQL）
- **Spring Boot 集成**：提供 Starter 模块，开箱即用
- **动态规则加载**：支持从本地文件、数据库等多种来源加载校验规则
- **熔断机制**：内置计数器熔断器，防止系统过载
- **AOP 拦截**：方法级校验拦截，实现自动执行校验过程
- **插件机制**：支持动态加载不同的校验器、解析器等组件
- **校验追踪**：支持校验追踪，记录拦截的触发条件，可以让用户对拦截的业务进行感知并且进行分析

## 安全性

[![Security Status](https://www.murphysec.com/platform3/v31/badge/1957809854577401856.svg)](https://www.murphysec.com/console/report/1957067370900410368/1957809854577401856)

## 安装使用

### Maven 依赖

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-spring-boot-starter</artifactId>
    <version>0.0.5-beta</version>
</dependency>
```

#### 额外的语言选择

从0.0.5版本开始框架本身不额外引入其他语言的情况下只支持QLExpress4，如果需要其他的语言，请自行引入对应的依赖

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-groovy</artifactId>
    <version>0.0.5-beta</version>
</dependency>
```

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-aviator</artifactId>
    <version>0.0.5-beta</version>
</dependency>
```

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-javascript</artifactId>
    <version>0.0.5-beta</version>
</dependency>
```

### 基本配置

在 `application.yml` 中添加配置：

```yaml
validation:
  # 解析器的配置，获取校验链时若未指定group，将根据解析器优先级获取对应group下的流程
  parser: sql,xml,json

  # 解析文件路径的解析器名称，一般无需设置，使用框架默认解析器
  pathParserName: spring

  # 需要拦截的类，若使用方法级别校验，设置为需要拦截的类名称
  validationMethod: **Service

  # 安全策略
  enableSecurityStrategy: false

  # 链路追踪
  enableBizTrace: false

  # 熔断配置
  enableGuard: false
  # 熔断模式
  guardMode: COUNTER

  # 流程文件的路径，支持多个路径，用英文逗号分隔
  chainFilePath: classpath:chain/*Chain.xml,classpath:chain/*Chain.json

  # SQL数据存放chain的配置（用于连接数据库及字段映射，默认开启监听）
  sqlChainDataMap:
    enableListener: true
    url: jdbc:mysql://localhost:3306/db
    driverClassName: com.mysql.cj.jdbc.Driver
    username: root
    password: root
    # 监听器线程池核心线程数（定时任务实现）
    corePoolSize: 1
    # 第一次任务的间隔时间（秒）
    firstListenerInterval: 120
    # 后续任务的间隔时间（秒）
    listenerInterval: 300
    tableName: validation_chain
    createTimeField: create_time
    updateTimeField: update_time
    chainIdField: chain_id
    deletedField: deleted
    orderField: order
    messageField: message
    fastFailField: fast_fail
    languageField: language
    scriptField: script

  # XML存放chain的配置（用于xml标签映射及监听器）
  xmlChainDataMap:
    enableListener: true
    # 监听文件的路径
    listenerFileList: classpath:chain/*Chain.xml
    # 监听文件的间隔
    listenerInterval: 5
    guardExpireField: guardExpire
    guardThresholdField: guardThreshold
    chainField: chain
    chainIdField: id
    nodeField: node
    languageField: language
    orderField: order
    messageField: message
    fastFailField: fastFail

  # JSON存放chain的配置（用于json字段映射及监听器）
  jsonChainDataMap:
    enableListener: true
    # 监听文件的路径
    listenerFileList: classpath:chain/*Chain.json
    # 监听文件的间隔
    listenerInterval: 5
    guardExpireField: guardExpire
    guardThresholdField: guardThreshold
    chainIdField: chainId
    nodeField: node
    languageField: language
    orderField: order
    messageField: message
    fastFailField: fastFail
    scriptField: script
```

在 `application.properties` 中添加配置：

```properties
# 解析器的配置，获取校验链的时候如果没有指定group会根据解析器的优先级获取对应group下的流程
validation.parser=sql,xml,json
# 解析文件路径的解析器名称，一般不用设置，直接根据框架默认的解析器即可
validation.pathParserName=spring
# 需要拦截的类，如果想使用方法级别校验，请将此参数设置为需要拦截的类名称
validation.validationMethod=**Service
# 流程文件的路径，支持多个路径，多个路径用英文逗号隔开
validation.chainFilePath=classpath:chain/*Chain.xml,classpath:chain/*Chain.json
# 安全策略，如果开启，脚本中无法对参数内容进行修改
validation.enableSecurityStrategy=false
# 链路追踪
validation.enableBizTrace=false
# 熔断配置
validation.enableGuard=false
# 熔断模式
validation.guardMode=COUNTER
# sql数据存放chain的配置，主要用于连接数据以及对字段的映射，默认开启监听
validation.sqlChain-data-map[enableListener]=true
validation.sqlChainDataMap[url]=jdbc:mysql://localhost:3306/db
validation.sqlChainDataMap[driverClassName]=com.mysql.cj.jdbc.Driver
validation.sqlChainDataMap[username]=root
validation.sqlChainDataMap[password]=root
# 监听器是使用定时任务线程池实现的，这个是线程池的核心线程数
validation.sqlChainDataMap[corePoolSize]=1
# 第一次任务的间隔时间（秒）
validation.sqlChainDataMap[firstListenerInterval]=120
# 后续任务的间隔时间（秒）
validation.sqlChainDataMap[listenerInterval]=300
validation.sqlChainDataMap[tableName]=validation_chain
validation.sqlChainDataMap[createTimeField]=create_time
validation.sqlChainDataMap[updateTimeField]=update_time
validation.sqlChainDataMap[chainIdField]=chain_id
validation.sqlChainDataMap[deletedField]=deleted
validation.sqlChainDataMap[orderField]=order
validation.sqlChainDataMap[messageField]=message
validation.sqlChainDataMap[fastFailField]=fast_fail
validation.sqlChainDataMap[languageField]=language
validation.sqlChainDataMap[scriptField]=script
# xml存放chain的配置，主要用于xml标签的映射，以及监听器
validation.xmlChainDataMap[enableListener]=true
# 监听文件的路径
validation.xmlChainDataMap[listenerFileList]=classpath:chain/*Chain.xml
# 监听文件的间隔
validation.xmlChainDataMap[listenerInterval]=5
validation.xmlChainDataMap[guardExpireField]=guardExpire
validation.xmlChainDataMap[guardThresholdField]=guardThreshold
validation.xmlChainDataMap[chainField]=chain
validation.xmlChainDataMap[chainIdField]=id
validation.xmlChainDataMap[nodeField]=node
validation.xmlChainDataMap[languageField]=language
validation.xmlChainDataMap[orderField]=order
validation.xmlChainDataMap[messageField]=message
validation.xmlChainDataMap[fastFailField]=fastFail
# json存放chain的配置，主要用于json字段的映射，以及监听器
validation.jsonChainDataMap[enableListener]=true
# 监听文件的路径
validation.jsonChainDataMap[listenerFileList]=classpath:chain/*Chain.json
# 监听文件的间隔
validation.jsonChainDataMap[listenerInterval]=5
validation.jsonChainDataMap[guardExpireField]=guardExpire
validation.jsonChainDataMap[guardThresholdField]=guardThreshold
validation.jsonChainDataMap[chainIdField]=chainId
validation.jsonChainDataMap[nodeField]=node
validation.jsonChainDataMap[languageField]=language
validation.jsonChainDataMap[orderField]=order
validation.jsonChainDataMap[messageField]=message
validation.jsonChainDataMap[fastFailField]=fastFail
validation.jsonChainDataMap[scriptField]=script
```

### 使用示例

在springBoot项目启动类上添加 [@EnableValidation] `加上这个注解springboot启动时会自动加载一整套流程`

#### 自动执行校验流程

在配置文件中配置 `validation.validationMethod` 定义需要拦截的类，可以使用通配符

在需要校验的方法实现类上添加 [@DynamicGuard]注解

```java

@Service("simpleService")
public class SimpleServiceImpl implements SimpleService {

    /**
     * 单个脚本为校验链
     *
     * @param param 请求参数
     */
    @DynamicGuard(group = "json", chainId = "user.create")
    @Override
    public void oneNode(Param param) {
        System.out.println("校验通过");
    }

    /**
     * 多脚本组合为校验链
     *
     * @param param 请求参数
     */
    @DynamicGuard(group = "xml", chainId = "user.update")
    @Override
    public void moreNode(Param param) {
        System.out.println("校验通过");
    }

    /**
     * 按照优先级匹配校验链分组
     *
     * @param param 请求参数
     */
    @DynamicGuard(chainId = "user.create")
    @Override
    public void sqlNode(Param param) {
        System.out.println("校验通过");
    }

    /**
     * 熔断
     *
     * @param param 请求参数
     */
    @Override
    @DynamicGuard(chainId = "user.create",
            // 启用熔断
            enableGuard = true,
            // 熔断模式，可选：COUNTER,RATE
            guardMode = GuardMode.COUNTER,
            // 熔断阈值的json格式
            guardThreshold = "{\"threshold\":100,\"period\":10,\"fail\":true}"
    )
    public void guard(Param param) {
        System.out.println("校验通过");
    }

}
```

#### 自行执行校验流程

业务级验证流程, 在业务逻辑中调用显式的调用流程

```java
public class BizService {
    public void guardInBiz(Param param) {
        // 1.构建校验流程上下文
        ValidationContext context = new SpringValidationContext();
        // 如果需要熔断可以设置对应的参数，优先级按照每个流程的设置来，全局的熔断配置不影响流程单独配置的熔断参数
        ChainOptions chainOptions = ChainOptions.builder()
                // 启用熔断
                .enableGuard(true)
                // 熔断模式，可选：COUNTER,RATE
                .guardMode(GuardMode.RATE)
                // 熔断阈值
                .guardThreshold(new InterceptRateThreshold())
                .build();
        context.setChainOptions(chainOptions);

        // 2.获取验证链再执行
        ValidationChain chain = null;
        chain = ChainExecutorHelper.getChain("您的验证链ID");
        chain = ChainExecutorHelper.getChain("您想使用的存储流程的分组", "您的验证链ID");

        // 直接抛出异常的执行
        chain.execute(context);

        // 带返回值的执行
        ValidationResult result = chain.executeResult(context);

        // 根据返回值中的信息处理

        // 工具类直接执行
        ChainExecutorHelper.validateHere("您的验证链ID", context);
        ChainExecutorHelper.validateHere("您想使用的存储流程的分组", "您的验证链ID", context);
    }
}
```

2. 定义校验链配置：

`json格式`

```json
[
  {
    "chainId": "user.create",
    "node": [
      {
        "order": 1,
        "script": "",
        "language": "",
        "message": "参数不能为空",
        "fastFail": true
      }
    ]
  }
]
```

`xml格式`

```xml

<validation>
    <chain id="user.update">
        <!-- 角色数据权限校验 -->
        <node language="Groovy" order="1" message="校验失败">
            <![CDATA[
            def workNo = param.workNo;
            def roleService = beanContext.getBean("roleService");
            def roleList = roleService.getRoleList(workNo);
            def flag = false;
            // 角色权限校验
            if(roleList != null || roleList.size() > 0) {
               if("admin" in roleList){
                // 获取用户数据权限
                def dataRoleList = roleService.getDataRoleList(workNo);
                return "update" in dataRoleList;
               }
            }
            return flag;
            ]]>
        </node>
        <!-- 再使用qle对修改的内容进行校验 -->
        <node language="QLExpress4" order="2" message="校验失败">
            <![CDATA[
            if (NotNull(param.name) == false) {return false;}
            if (NotNull(param.age) == false) {return false;}
            if (NotNull(param.sex) == false) {return false;}
            if (NotNull(param.address) == false) {return false;}
            if (InClosedRange(18, 60, param.age) == false) {return false;}
            roleList = InvokeBeanMethod("roleService", "getDataRoleList", param.workNo);
            if(roleList.size() == 0) {return false;}
            return param.address == InvokeBeanMethod("addressService", "getAddress");
            ]]>
        </node>
    </chain>
</validation>
```

## 扩展开发

### 自定义校验器

实现 [Validator]或者继承[BaseValidator]接口并注册到 SPI：

```java
public class CustomValidator extends BaseValidator {
    @Override
    public Object compile(String script) throws Exception {
        // 编译逻辑，实现这个逻辑后可对脚本进行编译缓存
        return null;
    }

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

### 自定义业务校验统计器

`业务校验统计器的意义在于让您对自己的业务进行分析，比如某块业务因为什么条件导致一直被拦截`

实现[BizValidationStatistics]或者继承[BaseBizValidationStatistics]并且注册到spring中，如果不自定义，会使用框架默认的统计器，但是无法做到数据持久化以及分布式系统统计

```java
public class CustomBizValidationStatistics extends BaseBizValidationStatistics {
    /**
     * 调用次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementCount(String chainId) {

    }

    /**
     * 调用成功次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementPassedCount(String chainId) {

    }

    /**
     * 熔断次数加1
     *
     * @param chainId 链ID
     */
    @Override
    public void incrementGuardCount(String chainId) {

    }

    /**
     * 拦截次数加1
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     */
    @Override
    public void incrementValidationCount(String chainId, String nodeName, String condition) {

    }

    /**
     * 获取调用次数
     *
     * @param chainId 链ID
     * @return 调用次数
     */
    @Override
    public Long count(String chainId) {

    }

    /**
     * 获取通过次数
     *
     * @param chainId 链ID
     * @return 通过次数
     */
    @Override
    public Long passedCount(String chainId) {

    }

    /**
     * 获取熔断次数
     *
     * @param chainId 链ID
     * @return 拦截次数
     */
    @Override
    public Long guardCount(String chainId) {

    }

    /**
     * 获取拦截次数
     *
     * @param chainId   链ID
     * @param nodeName  节点名称
     * @param condition 拦截条件
     * @return 拦截次数
     */
    @Override
    public Long validationCount(String chainId, String nodeName, String condition) {

    }

    /**
     * 节点拦截次数
     *
     * @param chainId  链ID
     * @param nodeName 节点名称
     * @return key 拦截条件 value 拦截次数
     */
    @Override
    public Map<String, Long> nodeValidationCount(String chainId, String nodeName) {

    }

    /**
     * 链拦截率
     *
     * @param chainId 链ID
     * @return 拦截率
     */
    @Override
    public BigDecimal chainValidationRate(String chainId) {

    }

    /**
     * 链拦截指标
     *
     * @param chainId 链ID
     * @return 拦截指标
     */
    @Override
    public List<ValidationMetrics> validationMetrics(String chainId) {

    }
}
```

### 自定义熔断器

#### 计数熔断器

实现[CounterGuard]

```java

@Commpent
public class CustomCounterGuard implements CounterGuard {

    @Override
    public List<String> chainId() {
        // 如果返回为空则代表全局的计数熔断都会使用这个熔断器
        return null;
    }

    @Override
    public Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold) {
        CounterThreshold counterThreshold = (CounterThreshold) guardThreshold;
        // 判断是否超过了阈值，超过阈值会执行降级操作
        return null;
    }

    @Override
    public Long increment(String chainId) {
        // 自增
        return null;
    }

    @Override
    public Long getCount(String chainId) {
        // 获取当前失败的次数
        return;
    }

    @Override
    public void clear(String chainId) {
        // 清除
    }

    @Override
    public void fallBack(String chainId, ValidationContext context) {
        // 在这里实现降级逻辑
    }
}
```

#### 拦截率熔断器

`拦截率熔断器的应该是要配合业务校验统计器使用的，目的就是为了在达到一定阈值时进行降级操作`

实现[InterceptRateGuard]

```java
public class CustomInterceptRateGuard implements InterceptRateGuard {

    /**
     * 链路ID
     *
     * @return 链路ID
     */
    @Override
    public List<String> chainId() {
        // 如果返回为空则代表全局的计数熔断都会使用这个熔断器
        return null;
    }

    /**
     * 是否超过阈值
     *
     * @param chainId        流程id
     * @param guardThreshold 熔断阈值
     * @return true 超过阈值
     */
    @Override
    public Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold) {
        InterceptRateThreshold interceptRateThreshold = (InterceptRateThreshold) guardThreshold;
        // 这里实现判断阈值的逻辑
        return true;
    }

    /**
     * 降级操作
     *
     * @param chainId 链路ID
     * @param context 上下文
     */
    @Override
    public void fallBack(String chainId, ValidationContext context) {
        // 在这里实现降级逻辑
    }
}
```

```
自己定义的熔断器都需要注册到spring中
```

## 贡献指南

欢迎提交 Issue 和 Pull Request 来改进 dyna-guard。

## 许可证

[Apache 2.0]

## 联系方式

<img src="doc/wechat.png" width="350">
<img src="doc/yangpeng.png" width="350">