package com.dg.core.parser.local;

import com.dg.core.chain.ValidationChain;
import com.dg.core.holder.ChainConfigHolder;
import com.dg.domain.ValidationNode;
import com.dg.domain.config.ChainDataConfig;
import com.dg.domain.config.ChainXmlConfig;
import com.dg.domain.config.LocalChainDataConfig;
import com.dg.domain.config.ValidationChainConfig;
import com.dg.domain.enums.ParserTypeEnum;
import com.dg.domain.exception.ValidationChainParserException;
import com.dg.utils.BeanMapUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * xml文件解析器
 *
 * @author VD
 * @date 2025/8/3 10:59
 */
public class ChainXmlParser extends LocalChainFileParser {

    /**
     * 初始化
     *
     * @param config 配置
     */
    @Override
    public void init(ValidationChainConfig config) {
        Map<String, String> dataMap = config.getXmlChainDataMap();
        // 需要对监听文件路径进行分割
        Map<String, Function<String, Object>> converterMap = Maps.newHashMap();
        ChainDataConfig.fieldConsumer()
                .andThen(LocalChainDataConfig.fieldConsumer())
                .accept(converterMap);
        ChainXmlConfig chainXmlConfig = BeanMapUtils.map2Bean(dataMap, converterMap, ChainXmlConfig.class);
        ChainConfigHolder.registerDataConfig(type().getType(), chainXmlConfig);
    }

    /**
     * 解析文件
     *
     * @param fileInfoList 文件信息
     * @return 验证链
     */
    @Override
    protected List<ValidationChain> parseFile(List<String> fileInfoList) {
        ChainXmlConfig config = (ChainXmlConfig) ChainConfigHolder.getDataConfig(type().getType());

        // 解析
        List<ValidationChain> resultList = Lists.newArrayList();
        try {
            for (String fileInfo : fileInfoList) {
                Document document = DocumentHelper.parseText(fileInfo);
                Element rootElement = document.getRootElement();
                List<Element> chainElementList = rootElement.elements(config.getChainField());
                chainElementList.forEach(chainElement -> {
                    ValidationChain chain = new ValidationChain();
                    String chainId = chainElement.attributeValue(config.getChainIdField());
                    List<Element> nodeElementList = chainElement.elements(config.getNodeField());
                    chain.setChainId(chainId);
                    chain.setGroup(type().getType());
                    chain.setGuardExpire(Long.parseLong(chainElement.attributeValue(config.getGuardExpireField())));
                    chain.setGuardThreshold(Long.parseLong(chainElement.attributeValue(config.getGuardThresholdField())));
                    chain.setNodes(nodeElementList.stream().map(nodeElement -> {
                        ValidationNode node = new ValidationNode();
                        node.setLanguage(nodeElement.attributeValue(config.getLanguageField()));
                        node.setScript(nodeElement.getText());
                        node.setOrder(Integer.parseInt(nodeElement.attributeValue(config.getOrderField())));
                        node.setMessage(nodeElement.attributeValue(config.getMessageField()));
                        node.setFastFail(Boolean.parseBoolean(nodeElement.attributeValue(config.getFastFailField())));
                        return node;
                    }).sorted(Comparator.comparingInt(ValidationNode::getOrder)).collect(Collectors.toList()));
                    resultList.add(chain);
                });
            }
        } catch (Exception e) {
            throw new ValidationChainParserException("xml parser exception : " + e.getMessage(), e);
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
        return ParserTypeEnum.XML;
    }
}
