package cn.easygd.dynaguard.core.chain;

import cn.easygd.dynaguard.core.engine.Validator;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.core.holder.ChainFilePathParserHolder;
import cn.easygd.dynaguard.core.listener.ValidationChainListener;
import cn.easygd.dynaguard.core.parser.ValidationChainParser;
import cn.easygd.dynaguard.core.path.ChainFilePathParser;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.exception.ValidationChainListenerException;
import cn.easygd.dynaguard.domain.exception.ValidationChainParserException;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
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
     * 初始化
     */
    public void init() {
        if (Objects.isNull(config)) {
            throw new IllegalArgumentException("validationChainConfig is null , please set config");
        }

        // 初始化配置
        ChainConfigHolder.init(config);

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

        // 初始化文件路径解析器
        ChainFilePathParser filePathParser = PATH_PARSERS.get(config.getPathParserName());
        ChainFilePathParserHolder.init(filePathParser);
    }

    /**
     * 加载验证链
     */
    public void loadChain() {
        // 根据配置获取对应解析器
        config.getParserList().forEach(parserType -> {
            ValidationChainParser parser = PARSERS.get(parserType);
            if (Objects.isNull(parser)) {
                log.warn("parser not found : {}", parserType);
                return;
            }

            // 初始化内容，一般是对配置的初始化
            try {
                if (log.isDebugEnabled()) {
                    log.debug("parser init start : {}", parserType);
                }

                parser.init(config);
            } catch (Exception e) {
                log.error("parser init error type : {} , msg : {}", parserType, e.getMessage(), e);
                throw new ValidationChainParserException(String.format("parser init error type : %s , msg : %s", parserType, e.getMessage()), e);
            }

            // 解析内容，解析流程时并不会方式脚本的执行器，解析完成后一并放入，如果有新的解析器，
            // 不需要关注怎么获取对应的脚本执行器，只需要实现解析流程即可
            List<ValidationChain> chainList;
            try {
                if (log.isDebugEnabled()) {
                    log.debug("parser parse start : {}", parserType);
                }
                chainList = parser.parse(config);
            } catch (Exception e) {
                log.error("parser parse error type : {} , msg : {}", parserType, e.getMessage(), e);
                throw new ValidationChainParserException(String.format("parser parse error type : %s , msg : %s", parserType, e.getMessage()), e);
            }
            if (log.isDebugEnabled()) {
                log.debug("parser parse end : {} , this group chain size : {}", parserType, chainList.size());
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
                log.info("validation chain listener register start : {}", parserType);
                try {
                    chainListener.register();
                } catch (Exception e) {
                    log.error("validation chain listener register error : {}", e.getMessage());
                    throw new ValidationChainListenerException(String.format("validation chain listener register error type : %s , msg : %s", parserType, e.getMessage()), e);
                }
            } else {
                log.warn("validation chain listener not found : {}", parserType);
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
            log.info("refresh chain start , group : {}", parser.type().getType());
            List<ValidationChain> validationChainList = parser.parse(config);
            if (CollectionUtils.isNotEmpty(validationChainList)) {
                Map<String, ValidationChain> chainMap = validationChainList.stream()
                        .peek(chain -> chain.getNodes().forEach(node -> node.setValidator(ENGINES.get(node.getLanguage()))))
                        .collect(Collectors.toMap(ValidationChain::getChainId, Function.identity(), (old, newOne) -> newOne, Maps::newConcurrentMap));
                CHAIN_CACHE.put(group, chainMap);
            }
            log.info("refresh chain end , group : {}", parser.type().getType());
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
    }
}
