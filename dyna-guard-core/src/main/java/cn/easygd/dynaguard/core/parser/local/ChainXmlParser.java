package cn.easygd.dynaguard.core.parser.local;

import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.domain.ValidationNode;
import cn.easygd.dynaguard.domain.config.ChainDataConfig;
import cn.easygd.dynaguard.domain.config.ChainXmlConfig;
import cn.easygd.dynaguard.domain.config.LocalChainDataConfig;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;
import cn.easygd.dynaguard.domain.exception.ValidationChainParserException;
import cn.easygd.dynaguard.utils.BeanMapUtils;
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
