package com.dg.core.chain;

import com.dg.core.engine.Validator;
import com.dg.core.engine.qle.GuardScriptExecutor;
import com.dg.core.holder.ChainConfigHolder;
import com.dg.core.holder.ChainFilePathParserHolder;
import com.dg.core.listener.ValidationChainListener;
import com.dg.core.parser.ValidationChainParser;
import com.dg.core.path.ChainFilePathParser;
import com.dg.domain.config.ValidationChainConfig;
import com.dg.domain.exception.ValidationChainParserException;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 验证链管理
 *
 * @author VD
 * @date 2025/7/29 21:28
 */
public class ValidationChainManager {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ValidationChainManager.class);

    /**
     * 单例
     */
    private static volatile ValidationChainManager instance;

    /**
     * 配置
     */
    private ValidationChainConfig config;

    /**
     * 缓存
     */
    private final Map<String, Map<String, ValidationChain>> CHAIN_CACHE = Maps.newConcurrentMap();

    /**
     * 规则引擎
     */
    private final Map<String, Validator> ENGINES = Maps.newConcurrentMap();

    /**
     * 解析器
     */
    private final Map<String, ValidationChainParser> PARSERS = Maps.newConcurrentMap();

    /**
     * 文件路径解析器
     */
    private final Map<String, ChainFilePathParser> PATH_PARSERS = Maps.newConcurrentMap();

    /**
     * 流程监听器
     */
    private final Map<String, ValidationChainListener> LISTENERS = Maps.newConcurrentMap();

    /**
     * 加载验证链
     */
    public void loadChain() {
        // 初始化配置
        ChainConfigHolder.init(config);
        // 初始化文件路径解析器
        ChainFilePathParser filePathParser = PATH_PARSERS.get(config.getPathParserName());
        ChainFilePathParserHolder.init(filePathParser);
        // 初始化qle执行器
        GuardScriptExecutor.init();

        // 根据配置获取对应解析器
        config.getParserList().forEach(parserType -> {
            ValidationChainParser parser = PARSERS.get(parserType);
            if (Objects.isNull(parser)) {
                log.warn("parser not found : {}", parserType);
                return;
            }

            // 初始化内容，一般是对配置的初始化
            try {
                parser.init(config);
            } catch (Exception e) {
                log.error("parser init error type : {} , msg : {}", parserType, e.getMessage(), e);
                throw new ValidationChainParserException(String.format("parser init error type : %s , msg : %s", parserType, e.getMessage()), e);
            }

            // 解析内容，解析流程时并不会方式脚本的执行器，解析完成后一并放入，如果有新的解析器，
            // 不需要关注怎么获取对应的脚本执行器，只需要实现解析流程即可
            List<ValidationChain> chainList;
            try {
                chainList = parser.parse(config);
            } catch (Exception e) {
                log.error("parser parse error type : {} , msg : {}", parserType, e.getMessage(), e);
                throw new ValidationChainParserException(String.format("parser parse error type : %s , msg : %s", parserType, e.getMessage()), e);
            }

            // 放入对应的脚本解析器中
            if (CollectionUtils.isNotEmpty(chainList)) {
                chainList.forEach(chain -> {
                    chain.getNodes().forEach(node -> node.setValidator(ENGINES.get(node.getLanguage())));
                    register(parser.type().getType(), chain.getChainId(), chain);
                });
            }
        });

        // 启动监听
        config.getParserList().forEach(parserType -> {
            ValidationChainListener chainListener = LISTENERS.get(parserType);
            if (Objects.nonNull(chainListener)) {
                try {
                    chainListener.register();
                } catch (Exception e) {
                    log.error("validation chain listener register error : {}", e.getMessage());
                }
            } else {
                log.info("validation chain listener not found : {}", parserType);
            }
        });
    }

    /**
     * 获取验证链
     *
     * @param group   分组
     * @param chainId 链ID
     * @return 验证链
     */
    public ValidationChain getChain(String group, String chainId) {
        if (StringUtils.isBlank(group)) {
            return getChain(chainId);
        }
        return Optional.ofNullable(CHAIN_CACHE.getOrDefault(group, Maps.newConcurrentMap()).get(chainId)).orElse(null);
    }

    /**
     * 获取验证链
     *
     * @param chainId 链ID
     * @return 验证链
     */
    public ValidationChain getChain(String chainId) {
        List<String> parserList = config.getParserList();
        for (String group : parserList) {
            ValidationChain chain = getChain(group, chainId);
            if (Objects.nonNull(chain)) {
                return chain;
            }
        }
        return null;
    }

    /**
     * 获取验证链解析器
     *
     * @param type 解析器类型
     * @return 解析器
     */
    public ValidationChainParser getChainParser(String type) {
        return PARSERS.get(type);
    }

    /**
     * 注册验证链
     *
     * @param group   分组
     * @param chainId 链ID
     * @param chain   验证链
     */
    public void register(String group, String chainId, ValidationChain chain) {
        CHAIN_CACHE.computeIfAbsent(group, k -> Maps.newConcurrentMap()).put(chainId, chain);
        log.info("validation chain register success : [{}=={}]", group, chainId);
    }

    /**
     * 刷新分组下的验证链
     */
    public void refreshChain(String group) {
        // 获取对应解析器
        ValidationChainParser parser = getChainParser(group);
        if (Objects.nonNull(parser)) {
            List<ValidationChain> validationChainList = parser.parse(config);
            if (CollectionUtils.isNotEmpty(validationChainList)) {
                Map<String, ValidationChain> chainMap = validationChainList.stream()
                        .peek(chain -> chain.getNodes().forEach(node -> node.setValidator(ENGINES.get(node.getLanguage()))))
                        .collect(Collectors.toMap(ValidationChain::getChainId, Function.identity(), (old, newOne) -> newOne, Maps::newConcurrentMap));
                CHAIN_CACHE.put(group, chainMap);
            }
        }
    }

    /**
     * 更新验证链
     *
     * @param group    分组
     * @param chainMap 验证链
     */
    public void updateChain(String group, Map<String, ValidationChain> chainMap) {
        chainMap.forEach((chainId, chain) -> chain.getNodes().forEach(node -> node.setValidator(ENGINES.get(node.getLanguage()))));
        CHAIN_CACHE.getOrDefault(group, Maps.newConcurrentMap()).putAll(chainMap);
    }

    /**
     * 批量删除验证链
     *
     * @param group    分组
     * @param chainIds 链ID
     */
    public void removeChain(String group, String... chainIds) {
        Map<String, ValidationChain> chainMap = CHAIN_CACHE.getOrDefault(group, Maps.newHashMap());
        if (MapUtils.isNotEmpty(chainMap)) {
            for (String chainId : chainIds) {
                chainMap.remove(chainId);
                log.info("[{}=={}] remove success", group, chainId);
            }
        }
    }

    /**
     * 设置配置
     *
     * @param config 配置
     */
    public void setConfig(ValidationChainConfig config) {
        this.config = config;
    }

    /**
     * 获取实例
     *
     * @return 规则引擎管理类实例
     */
    public static ValidationChainManager getInstance() {
        if (instance == null) {
            synchronized (ValidationChainManager.class) {
                if (instance == null) {
                    instance = new ValidationChainManager();
                }
            }
        }
        return instance;
    }

    private ValidationChainManager() {
        // 加载规则引擎
        ServiceLoader<Validator> enginesLoader = ServiceLoader.load(Validator.class);
        enginesLoader.forEach(engine -> ENGINES.put(engine.getLanguage(), engine));
        // 加载解析器
        ServiceLoader<ValidationChainParser> parserLoader = ServiceLoader.load(ValidationChainParser.class);
        parserLoader.forEach(parser -> PARSERS.put(parser.type().getType(), parser));
        // 路径解析器
        ServiceLoader<ChainFilePathParser> pathParserLoader = ServiceLoader.load(ChainFilePathParser.class);
        pathParserLoader.forEach(parser -> PATH_PARSERS.put(parser.getParserName(), parser));
        // 监听器
        ServiceLoader<ValidationChainListener> listenerLoader = ServiceLoader.load(ValidationChainListener.class);
        listenerLoader.forEach(listener -> LISTENERS.put(listener.type().getType(), listener));
    }
}
