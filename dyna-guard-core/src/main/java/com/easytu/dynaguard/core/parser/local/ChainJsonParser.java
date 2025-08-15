package com.easytu.dynaguard.core.parser.local;

import com.easytu.dynaguard.core.chain.ValidationChain;
import com.easytu.dynaguard.core.holder.ChainConfigHolder;
import com.easytu.dynaguard.domain.ValidationNode;
import com.easytu.dynaguard.domain.config.ChainDataConfig;
import com.easytu.dynaguard.domain.config.ChainJsonConfig;
import com.easytu.dynaguard.domain.config.LocalChainDataConfig;
import com.easytu.dynaguard.domain.config.ValidationChainConfig;
import com.easytu.dynaguard.domain.enums.ParserTypeEnum;
import com.easytu.dynaguard.domain.exception.ValidationChainParserException;
import com.easytu.dynaguard.utils.BeanMapUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * json文件解析器
 *
 * @author VD
 * @date 2025/8/3 10:59
 */
public class ChainJsonParser extends LocalChainFileParser {

    /**
     * objectMapper
     */
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 忽略重复key
        OBJECT_MAPPER.enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY);
    }

    /**
     * 初始化
     *
     * @param config 配置
     */
    @Override
    public void init(ValidationChainConfig config) {
        // 需要对监听文件路径进行分割
        Map<String, Function<String, Object>> converterMap = Maps.newHashMap();
        ChainDataConfig.fieldConsumer()
                .andThen(LocalChainDataConfig.fieldConsumer())
                .accept(converterMap);
        ChainJsonConfig chainJsonConfig = BeanMapUtils.map2Bean(config.getJsonChainDataMap(), converterMap, ChainJsonConfig.class);
        ChainConfigHolder.registerDataConfig(type().getType(), chainJsonConfig);
    }

    /**
     * 解析文件
     *
     * @param fileInfoList 文件信息
     * @return 验证链
     */
    @Override
    protected List<ValidationChain> parseFile(List<String> fileInfoList) {
        ChainJsonConfig config = (ChainJsonConfig) ChainConfigHolder.getDataConfig(type().getType());

        List<ValidationChain> resultList = Lists.newArrayList();
        try {
            for (String fileInfo : fileInfoList) {
                JsonNode jsonNode = OBJECT_MAPPER.readTree(fileInfo);
                jsonNode.iterator().forEachRemaining(jsonNodeElement -> {
                    ValidationChain chain = new ValidationChain();
                    chain.setChainId(jsonNodeElement.get(config.getChainIdField()).asText());
                    chain.setGroup(type().getType());
                    chain.setGuardExpire(jsonNodeElement.get(config.getGuardExpireField()).asLong());
                    chain.setGuardThreshold(jsonNodeElement.get(config.getGuardThresholdField()).asLong());
                    List<ValidationNode> nodeList = Lists.newArrayList();

                    jsonNodeElement.get(config.getNodeField()).elements().forEachRemaining(element -> {
                        ValidationNode node = new ValidationNode();
                        node.setScript(element.get(config.getScriptField()).asText());
                        node.setLanguage(element.get(config.getLanguageField()).asText());
                        node.setOrder(element.get(config.getOrderField()).asInt());
                        node.setMessage(element.get(config.getMessageField()).asText());
                        node.setFastFail(element.get(config.getFastFailField()).asBoolean());
                        nodeList.add(node);
                    });

                    chain.setNodes(nodeList.stream().sorted(Comparator.comparing(ValidationNode::getOrder)).collect(Collectors.toList()));
                    resultList.add(chain);
                });
            }
        } catch (Exception e) {
            throw new ValidationChainParserException("json parser exception : " + e.getMessage(), e);
        }

        return resultList;
    }

    /**
     * 类型
     *
     * @return 类型
     */
    @Override
    public ParserTypeEnum type() {
        return ParserTypeEnum.JSON;
    }
}
