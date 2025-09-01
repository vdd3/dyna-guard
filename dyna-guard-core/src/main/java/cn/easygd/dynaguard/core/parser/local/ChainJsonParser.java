package cn.easygd.dynaguard.core.parser.local;

import cn.easygd.dynaguard.core.chain.ValidationChain;
import cn.easygd.dynaguard.core.holder.ChainConfigHolder;
import cn.easygd.dynaguard.domain.ValidationNode;
import cn.easygd.dynaguard.domain.config.ChainDataConfig;
import cn.easygd.dynaguard.domain.config.ChainJsonConfig;
import cn.easygd.dynaguard.domain.config.LocalChainDataConfig;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import cn.easygd.dynaguard.domain.enums.ParserTypeEnum;
import cn.easygd.dynaguard.domain.exception.ValidationChainParserException;
import cn.easygd.dynaguard.utils.BeanMapUtils;
import cn.easygd.dynaguard.utils.JsonUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * json文件解析器
 *
 * @author VD
 */
public class ChainJsonParser extends LocalChainFileParser {

    /**
     * 日志
     */
    private static final Logger log = LoggerFactory.getLogger(ChainJsonParser.class);

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
                JsonNode jsonNode = JsonUtils.parse(fileInfo);
                if (Objects.isNull(jsonNode)) {
                    log.error("json file parse error");
                    return Lists.newArrayList();
                }
                jsonNode.elements().forEachRemaining(jsonNodeElement -> {
                    ValidationChain chain = new ValidationChain();
                    chain.setChainId(jsonNodeElement.get(config.getChainIdField()).asText());
                    chain.setGroup(type().getType());

                    List<ValidationNode> nodeList = Lists.newArrayList();
                    jsonNodeElement.get(config.getNodeField()).elements().forEachRemaining(element -> {
                        ValidationNode node = new ValidationNode();
                        String nodeNameField = config.getNodeNameField();
                        if (element.has(nodeNameField)) {
                            node.setNodeName(element.get(nodeNameField).asText());
                        }
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
