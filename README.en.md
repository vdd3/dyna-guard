# dyna-guard - Dynamic Validation Framework

dyna-guard is a Java-based dynamic validation framework that supports multiple scripting languages and rule engines. It
provides a flexible validation chain mechanism, allowing developers to define and execute complex business validation
logic through configuration.

## Technical Documentation [Click to Enter](https://www.yuque.com/yuqueyonghuqdqyqs/kswtr7)

## Example Project [Click to Enter](https://github.com/vdd3/dyna-guard-example)

## Project Structure

```
dyna-guard/
├── dyna-guard-core/                 # Core module, containing validation engine, rule parsing and other core functions
├── dyna-guard-engine/               # Engine module, containing different rule engines
└── dyna-guard-spring-boot-starter/  # Spring Boot Starter, providing auto-configuration and integration support
```


## Technical Architecture

dyna-guard adopts a modular design with core modules separated from Spring Boot Starter for use in different
environments:

1. **Core Module** (`dyna-guard-core`) provides basic validation capabilities
2. **Spring Boot Integration Module** (`dyna-guard-spring-boot-starter`) provides auto-configuration and Spring
   integration
3. **Engine Module** (`dyna-guard-engine`) provides different rule engines such as Groovy, JavaScript and Aviator

The framework implements plug-in extension through SPI (Service Provider Interface) mechanism, supporting dynamic
loading of different validators, parsers and other components.

## Features

- **Multi-rule Engine Support**: Supports Groovy, JavaScript, QLExpress4 and Aviator
- **Flexible Validation Chain Mechanism**: Define validation nodes through configuration, supporting multiple data
  sources (JSON, XML, SQL)
- **Spring Boot Integration**: Provides Starter module, ready to use out of the box
- **Dynamic Rule Loading**: Supports loading validation rules from local files, databases and other sources
- **Circuit Breaker Mechanism**: Built-in counter circuit breaker to prevent system overload
- **AOP Interception**: Method-level validation interception, automatically executing validation process
- **Plugin Mechanism**: Supports dynamic loading of different validators, parsers and other components
- **Validation Tracking**: Supports validation tracking, recording interception trigger conditions, allowing users to
  perceive and analyze intercepted business

## Installation and Usage

### Maven Dependency

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-spring-boot-starter</artifactId>
    <version>0.0.5-beta</version>
</dependency>
```

#### Additional Language Options

Starting from version 0.0.5, the framework only supports QLExpress4 by default without additional language dependencies.
If you need other languages, please import the corresponding dependencies separately:

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

### Basic Configuration

Add configuration in `application.yml`:

```yaml
validation:
  # Parser configuration. If group is not specified when getting validation chain, the process under the corresponding group will be obtained according to parser priority
  parser: sql,xml,json

  # Path parser name. Generally no need to set, use framework default parser
  pathParserName: spring

  # Classes to intercept. If using method-level validation, set to class names that need to be intercepted
  validationMethod: **Service

  # Security strategy
  enableSecurityStrategy: false

  # Business tracing
  enableBizTrace: false

  # Circuit breaker configuration
  enableGuard: false
  # Circuit breaker mode
  guardMode: COUNTER

  # Chain file paths, supporting multiple paths separated by commas
  chainFilePath: classpath:chain/*Chain.xml,classpath:chain/*Chain.json

  # SQL chain data mapping configuration (for database connection and field mapping, listener enabled by default)
  sqlChainDataMap:
    enableListener: true
    url: jdbc:mysql://localhost:3306/db
    driverClassName: com.mysql.cj.jdbc.Driver
    username: root
    password: root
    # Listener thread pool core thread count (implemented with scheduled tasks)
    corePoolSize: 1
    # First task interval (seconds)
    firstListenerInterval: 120
    # Subsequent task interval (seconds)
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

  # XML chain data mapping configuration (for XML tag mapping and listener)
  xmlChainDataMap:
    enableListener: true
    # Listener file paths
    listenerFileList: classpath:chain/*Chain.xml
    # Listener file interval
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

  # JSON chain data mapping configuration (for JSON field mapping and listener)
  jsonChainDataMap:
    enableListener: true
    # Listener file paths
    listenerFileList: classpath:chain/*Chain.json
    # Listener file interval
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


Add configuration in `application.properties`:

```properties
# Parser configuration. If group is not specified when getting validation chain, the process under the corresponding group will be obtained according to parser priority
validation.parser=sql,xml,json
# Path parser name. Generally no need to set, use framework default parser
validation.pathParserName=spring
# Classes to intercept. If using method-level validation, set to class names that need to be intercepted
validation.validationMethod=**Service
# Chain file paths, supporting multiple paths separated by commas
validation.chainFilePath=classpath:chain/*Chain.xml,classpath:chain/*Chain.json
# Security strategy. If enabled, scripts cannot modify parameter content
validation.enableSecurityStrategy=false
# Business tracing
validation.enableBizTrace=false
# Circuit breaker configuration
validation.enableGuard=false
# Circuit breaker mode
validation.guardMode=COUNTER
# SQL chain data mapping configuration, mainly for database connection and field mapping, listener enabled by default
validation.sqlChain-data-map[enableListener]=true
validation.sqlChainDataMap[url]=jdbc:mysql://localhost:3306/db
validation.sqlChainDataMap[driverClassName]=com.mysql.cj.jdbc.Driver
validation.sqlChainDataMap[username]=root
validation.sqlChainDataMap[password]=root
# Listener is implemented using scheduled task thread pool, this is the core thread count
validation.sqlChainDataMap[corePoolSize]=1
# First task interval (seconds)
validation.sqlChainDataMap[firstListenerInterval]=120
# Subsequent task interval (seconds)
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
# XML chain data mapping configuration, mainly for XML tag mapping and listener
validation.xmlChainDataMap[enableListener]=true
# Listener file paths
validation.xmlChainDataMap[listenerFileList]=classpath:chain/*Chain.xml
# Listener file interval
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
# JSON chain data mapping configuration, mainly for JSON field mapping and listener
validation.jsonChainDataMap[enableListener]=true
# Listener file paths
validation.jsonChainDataMap[listenerFileList]=classpath:chain/*Chain.json
# Listener file interval
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

### Usage Example

Add [@EnableValidation] annotation on Spring Boot application startup
class `Adding this annotation will automatically load the entire process when Spring Boot starts`

#### Automatic Validation Process Execution

Configure `validation.validationMethod` in configuration file to define classes to intercept, supporting wildcards

Add [@DynamicGuard] annotation on method implementation classes that need validation:

```java

@Service("simpleService")
public class SimpleServiceImpl implements SimpleService {

    /**
     * Single script as validation chain
     *
     * @param param Request parameter
     */
    @DynamicGuard(group = "json", chainId = "user.create")
    @Override
    public void oneNode(Param param) {
        System.out.println("Validation passed");
    }

    /**
     * Multiple scripts combined as validation chain
     *
     * @param param Request parameter
     */
    @DynamicGuard(group = "xml", chainId = "user.update")
    @Override
    public void moreNode(Param param) {
        System.out.println("Validation passed");
    }

    /**
     * Match validation chain group by priority
     *
     * @param param Request parameter
     */
    @DynamicGuard(chainId = "user.create")
    @Override
    public void sqlNode(Param param) {
        System.out.println("Validation passed");
    }

    /**
     * Circuit breaker
     *
     * @param param Request parameter
     */
    @Override
    @DynamicGuard(chainId = "user.create",
            // Enable circuit breaker
            enableGuard = true,
            // Circuit breaker mode, options: COUNTER,RATE
            guardMode = GuardMode.COUNTER,
            // Circuit breaker threshold in JSON format
            guardThreshold = "{\"threshold\":100,\"period\":10,\"fail\":true}"
    )
    public void guard(Param param) {
        System.out.println("Validation passed");
    }
}
```

#### Manual Validation Process Execution

Business-level validation process, calling process explicitly in business logic:

```java
public class BizService {
    public void guardInBiz(Param param) {
        // 1. Build validation context
        ValidationContext context = new SpringValidationContext();
        // If circuit breaker is needed, corresponding parameters can be set. Priority follows each process setting, global circuit breaker configuration does not affect individual process configurations
        ChainOptions chainOptions = ChainOptions.builder()
                // Enable circuit breaker
                .enableGuard(true)
                // Circuit breaker mode, options: COUNTER,RATE
                .guardMode(GuardMode.RATE)
                // Circuit breaker threshold
                .guardThreshold(new InterceptRateThreshold())
                .build();
        context.setChainOptions(chainOptions);

        // 2. Get validation chain and execute
        ValidationChain chain = null;
        chain = ChainExecutorHelper.getChain("Your validation chain ID");
        chain = ChainExecutorHelper.getChain("Group of storage process you want to use", "Your validation chain ID");

        // Execute with direct exception throwing
        chain.execute(context);

        // Execute with return value
        ValidationResult result = chain.executeResult(context);

        // Process based on information in return value

        // Direct execution with utility class
        ChainExecutorHelper.validateHere("Your validation chain ID", context);
        ChainExecutorHelper.validateHere("Group of storage process you want to use", "Your validation chain ID", context);
    }
}
```


2. Define validation chain configuration:

`JSON format`

```json
[
  {
    "chainId": "user.create",
    "node": [
      {
        "order": 1,
        "script": "",
        "language": "",
        "message": "Parameter cannot be empty",
        "fastFail": true
      }
    ]
  }
]
```


`XML format`

```xml

<validation>
    <chain id="user.update">
        <!-- Role data permission validation -->
        <node language="Groovy" order="1" message="Validation failed">
            <![CDATA[
            def workNo = param.workNo;
            def roleService = beanContext.getBean("roleService");
            def roleList = roleService.getRoleList(workNo);
            def flag = false;
            // Role permission validation
            if(roleList != null || roleList.size() > 0) {
               if("admin" in roleList){
                // Get user data permissions
                def dataRoleList = roleService.getDataRoleList(workNo);
                return "update" in dataRoleList;
               }
            }
            return flag;
            ]]>
        </node>
        <!-- Use qle to validate modified content -->
        <node language="QLExpress4" order="2" message="Validation failed">
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

## Extension Development

### Custom Validator

Implement [Validator] or extend [BaseValidator] interface and register to SPI:

```java
public class CustomValidator extends BaseValidator {
    @Override
    public Object compile(String script) throws Exception {
        // Compilation logic, after implementing this logic, scripts can be compiled and cached
        return null;
    }

    @Override
    public Boolean validate(String script, ValidationContext context) {
        // Implement validation logic
        return true;
    }

    @Override
    public String getLanguage() {
        return "custom";
    }
}
```

Add implementation class in `META-INF/services/com.easytu.dynaguard.core.engine.Validator` file:

```
com.example.CustomValidator
```

### Custom Business Validation Statistics

`The meaning of business validation statistics is to allow you to analyze your business, such as why certain business blocks are constantly intercepted`

Implement [BizValidationStatistics] or extend [BaseBizValidationStatistics] and register to Spring. If not customized,
the framework's default statistics will be used, but data persistence and distributed system statistics cannot be
achieved:

```java
public class CustomBizValidationStatistics extends BaseBizValidationStatistics {
    /**
     * Increment call count
     *
     * @param chainId Chain ID
     */
    @Override
    public void incrementCount(String chainId) {

    }

    /**
     * Increment successful call count
     *
     * @param chainId Chain ID
     */
    @Override
    public void incrementPassedCount(String chainId) {

    }

    /**
     * Increment circuit breaker count
     *
     * @param chainId Chain ID
     */
    @Override
    public void incrementGuardCount(String chainId) {

    }

    /**
     * Increment interception count
     *
     * @param chainId   Chain ID
     * @param nodeName  Node name
     * @param condition Interception condition
     */
    @Override
    public void incrementValidationCount(String chainId, String nodeName, String condition) {

    }

    /**
     * Get call count
     *
     * @param chainId Chain ID
     * @return Call count
     */
    @Override
    public Long count(String chainId) {

    }

    /**
     * Get pass count
     *
     * @param chainId Chain ID
     * @return Pass count
     */
    @Override
    public Long passedCount(String chainId) {

    }

    /**
     * Get circuit breaker count
     *
     * @param chainId Chain ID
     * @return Interception count
     */
    @Override
    public Long guardCount(String chainId) {

    }

    /**
     * Get interception count
     *
     * @param chainId   Chain ID
     * @param nodeName  Node name
     * @param condition Interception condition
     * @return Interception count
     */
    @Override
    public Long validationCount(String chainId, String nodeName, String condition) {

    }

    /**
     * Node interception count
     *
     * @param chainId  Chain ID
     * @param nodeName Node name
     * @return key interception condition value interception count
     */
    @Override
    public Map<String, Long> nodeValidationCount(String chainId, String nodeName) {

    }

    /**
     * Chain interception rate
     *
     * @param chainId Chain ID
     * @return Interception rate
     */
    @Override
    public BigDecimal chainValidationRate(String chainId) {

    }

    /**
     * Chain interception metrics
     *
     * @param chainId Chain ID
     * @return Interception metrics
     */
    @Override
    public List<ValidationMetrics> validationMetrics(String chainId) {

    }
}
```

### Custom Circuit Breaker

#### Counter Circuit Breaker

Implement [CounterGuard]

```java

@Component
public class CustomCounterGuard implements CounterGuard {

    @Override
    public List<String> chainId() {
        // If return is empty, it means all counter circuit breakers will use this circuit breaker globally
        return null;
    }

    @Override
    public Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold) {
        CounterThreshold counterThreshold = (CounterThreshold) guardThreshold;
        // Determine if threshold is exceeded, exceeding threshold will execute fallback operation
        return null;
    }

    @Override
    public Long increment(String chainId) {
        // Increment
        return null;
    }

    @Override
    public Long getCount(String chainId) {
        // Get current failure count
        return null;
    }

    @Override
    public void clear(String chainId) {
        // Clear
    }

    @Override
    public void fallBack(String chainId, ValidationContext context) {
        // Implement fallback logic here
    }
}
```

#### Interception Rate Circuit Breaker

`Interception rate circuit breaker should be used with business validation statistics, aiming to perform fallback operations when reaching certain thresholds`

Implement [InterceptRateGuard]

```java
public class CustomInterceptRateGuard implements InterceptRateGuard {

    /**
     * Chain ID
     *
     * @return Chain ID
     */
    @Override
    public List<String> chainId() {
        // If return is empty, it means all counter circuit breakers will use this circuit breaker globally
        return null;
    }

    /**
     * Whether threshold is exceeded
     *
     * @param chainId        Process ID
     * @param guardThreshold Circuit breaker threshold
     * @return true if threshold is exceeded
     */
    @Override
    public Boolean isExceedThreshold(String chainId, GuardThreshold guardThreshold) {
        InterceptRateThreshold interceptRateThreshold = (InterceptRateThreshold) guardThreshold;
        // Implement threshold judgment logic here
        return true;
    }

    /**
     * Fallback operation
     *
     * @param chainId Chain ID
     * @param context Context
     */
    @Override
    public void fallBack(String chainId, ValidationContext context) {
        // Implement fallback logic here
    }
}
```

```
All custom circuit breakers need to be registered to Spring
```

## Contribution Guide

Welcome to submit Issues and Pull Requests to improve dyna-guard.

## License

[Apache 2.0]

## Contact

<img src="doc/wechat.png" width="350">
<img src="doc/yangpeng.png" width="350">