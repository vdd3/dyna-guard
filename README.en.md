# Dyna-Guard - Dynamic Validation Framework

Dyna-Guard is a Java-based dynamic validation framework that supports multiple scripting languages and rule engines. It
provides a flexible validation chain mechanism, allowing developers to define and execute complex business validation
logic through configuration.

## Technical Documentation [Click to Enter](https://www.yuque.com/yuqueyonghuqdqyqs/kswtr7)

## Example Project [Click to Enter](https://github.com/vdd3/dyna-guard-example)

## Project Structure

```
dyna-guard/
├── dyna-guard-core/           # Core module, including validation engine, rule parsing and other core functions
└── dyna-guard-spring-boot-starter/  # Spring Boot Starter, providing auto-configuration and integration support
```

## Technical Architecture

Dyna-Guard adopts a modular design with core module and Spring Boot Starter separated for use in different environments:

1. **Core Module** (`dyna-guard-core`) provides basic validation capabilities
2. **Spring Boot Integration Module** (`dyna-guard-spring-boot-starter`) provides auto-configuration and Spring
   integration

The framework implements plug-in extension through SPI (Service Provider Interface) mechanism, supporting dynamic
loading of different validators, parsers and other components.

## Features

- **Multi-rule Engine Support**: Supports Groovy, JavaScript, QLExpress and Spring Expression Language (SpEL)
- **Flexible Validation Chain Mechanism**: Define validation nodes through configuration, supporting multiple data
  sources (JSON, XML, SQL)
- **Spring Boot Integration**: Provides Starter module, ready to use out of the box
- **Dynamic Rule Loading**: Supports loading validation rules from local files, databases and other sources
- **Circuit Breaker Mechanism**: Built-in counter circuit breaker to prevent system overload
- **AOP Interception**: Method-level validation interception based on annotations

## Security

[![Security Status](https://www.murphysec.com/platform3/v31/badge/1957809854577401856.svg)](https://www.murphysec.com/console/report/1957067370900410368/1957809854577401856)

## Installation and Usage

### Maven Dependency

```xml

<dependency>
    <groupId>cn.easygd</groupId>
    <artifactId>dyna-guard-spring-boot-starter</artifactId>
    <version>0.0.4</version>
</dependency>
```

### Basic Configuration

Add configuration in `application.yml`:

```yaml
validation:
  # Parser configuration. If group is not specified when obtaining validation chain, the corresponding group process will be obtained according to parser priority
  parser: sql,xml,json

  # Parser name for parsing file paths. Generally no need to set, use framework default parser
  pathParserName: spring

  # Classes to intercept. If using method-level validation, set to class names that need to be intercepted
  validationMethod: **Service

  # Path of process files. Supports multiple paths separated by commas
  chainFilePath: classpath:chain/*Chain.xml,classpath:chain/*Chain.json

# Configuration for SQL data storing chain (for database connection and field mapping, listener enabled by default)
sqlChainDataMap:
  enableListener: true
  url: jdbc:mysql://localhost:3306/db
  driverClassName: com.mysql.cj.jdbc.Driver
  username: root
  password: root
  # Core thread count of listener thread pool (implemented via scheduled tasks)
  corePoolSize: 1
  # Interval for first task (seconds)
  firstListenerInterval: 120
  # Interval for subsequent tasks (seconds)
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
  guardExpireField: guard_expire
  guardThresholdField: guard_threshold

# Configuration for XML storing chain (for XML tag mapping and listener)
xmlChainDataMap:
  enableListener: true
  # Path of files to monitor
  listenerFileList: classpath:chain/*Chain.xml
  # Interval for monitoring files
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

# Configuration for JSON storing chain (for JSON field mapping and listener)
jsonChainDataMap:
  enableListener: true
  # Path of files to monitor
  listenerFileList: classpath:chain/*Chain.json
  # Interval for monitoring files
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
# Parser configuration. If group is not specified when obtaining validation chain, the corresponding group process will be obtained according to parser priority
validation.parser=sql,xml,json
# Parser name for parsing file paths. Generally no need to set, use framework default parser
validation.pathParserName=spring
# Classes to intercept. If using method-level validation, set to class names that need to be intercepted
validation.validationMethod=**Service
# Path of process files. Supports multiple paths separated by commas
validation.chainFilePath=classpath:chain/*Chain.xml,classpath:chain/*Chain.json
# Configuration for SQL data storing chain. Mainly for database connection and field mapping, listener enabled by default
validation.sqlChain-data-map[enableListener]=true
validation.sqlChainDataMap[url]=jdbc:mysql://localhost:3306/db
validation.sqlChainDataMap[driverClassName]=com.mysql.cj.jdbc.Driver
validation.sqlChainDataMap[username]=root
validation.sqlChainDataMap[password]=root
# Listener is implemented using scheduled task thread pool. This is the core thread count of the thread pool
validation.sqlChainDataMap[corePoolSize]=1
# Interval for first task (seconds)
validation.sqlChainDataMap[firstListenerInterval]=120
# Interval for subsequent tasks (seconds)
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
validation.sqlChainDataMap[guardExpireField]=guard_expire
validation.sqlChainDataMap[guardThresholdField]=guard_threshold
# Configuration for XML storing chain. Mainly for XML tag mapping and listener
validation.xmlChainDataMap[enableListener]=true
# Path of files to monitor
validation.xmlChainDataMap[listenerFileList]=classpath:chain/*Chain.xml
# Interval for monitoring files
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
# Configuration for JSON storing chain. Mainly for JSON field mapping and listener
validation.jsonChainDataMap[enableListener]=true
# Path of files to monitor
validation.jsonChainDataMap[listenerFileList]=classpath:chain/*Chain.json
# Interval for monitoring files
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

Add [@EnableValidation] on your Spring Boot project startup class

`Adding this annotation will automatically load the entire process when Spring Boot starts`

Add [@DynamicGuard] annotation on the method implementation class that needs validation:

```
java
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
     * Match validation chain group according to priority
     *
     * @param param Request parameter
     */
    @DynamicGuard(chainId = "user.create")
    @Override
    public void sqlNode(Param param) {
        System.out.println("Validation passed");
    }

    /**
     * Test circuit breaker
     *
     * @param param Request parameter
     */
    @Override
    @DynamicGuard(chainId = "user.create", enableGuard = true)
    public void guard(Param param) {
        System.out.println("Validation passed");
    }

}
```

Business-level validation process, calling the process explicitly in business logic:

```
java
public class BizService {
public void guardInBiz(Param param) {
// Build your validation process context
ValidationContext context = new SpringValidationContext();

        // Get process and execute it yourself
        ValidationChain chain = null;
        chain = ChainExecutorHelper.getChain("Your validation chain ID");
        chain = ChainExecutorHelper.getChain("Group of storage process you want to use", "Your validation chain ID");

        // Execute with direct exception throwing
        chain.execute(context);

        // Execute with return value
        ValidationResult result = chain.executeResult(context);

        // Execute with circuit breaker and exception throwing
        chain.executeGuard(context);

        // Execute with circuit breaker and return value
        result = chain.executeGuardResult(context);

        // You can handle based on information in the return value


        // Helper class executes for you
        ChainExecutorHelper.validateHere("Your validation chain ID", context);
        ChainExecutorHelper.validateHere("Group of storage process you want to use", "Your validation chain ID", context);

    }
}
```

2. Define validation chain configuration:

`JSON format`

```
json
[
{
"chainId": "user.create",
"node": [
{
"order": 1,
"script": "#param != null && #param.name != null",
"language": "SpEl",
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
        <node language="GuardScript" order="2" message="Validation failed">
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

Implement [Validator] interface or extend [BaseValidator] and register to SPI:

```
java
public class CustomValidator extends BaseValidator {
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

### Custom Counter Circuit Breaker, highly recommended to write your own counter circuit breaker

Implement [CounterGuard]

```
java
public class CustomCounterGuard implements CounterGuard {

    @Override
    public List<String> chainId() {
        // This defines the processes you need to circuit break
        return null;
    }

    @Override
    public Boolean isExceedThreshold(String chainId, Long threshold) {
        // Determine if threshold is exceeded. Exceeding threshold will execute degradation logic
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
        return;
    }

    @Override
    public void clear(String chainId) {
        // Clear
    }

    @Override
    public void rollback(String chainId) {
        // Implement your degradation logic here
    }
}
```

Add implementation class in `` file:

```
com.example.CustomCounterGuard
```

## Contribution Guide

Welcome to submit Issues and Pull Requests to improve Dyna-Guard.

## License

[Apache 2.0]

## Contact

<img src="doc/wechat.png" width="350">
<img src="doc/yangpeng.png" width="350">
