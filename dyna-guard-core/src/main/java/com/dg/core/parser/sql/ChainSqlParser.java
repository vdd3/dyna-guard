package com.dg.core.parser.sql;

import com.dg.core.chain.ValidationChain;
import com.dg.core.holder.ChainConfigHolder;
import com.dg.core.parser.ValidationChainParser;
import com.dg.domain.config.ChainDataConfig;
import com.dg.domain.config.ChainSqlConfig;
import com.dg.domain.config.ValidationChainConfig;
import com.dg.domain.constants.ParserTypeEnumerable;
import com.dg.domain.enums.ParserTypeEnum;
import com.dg.domain.exception.ChainSqlExecuteException;
import com.dg.domain.exception.ValidationChainParserException;
import com.dg.utils.BeanMapUtils;
import com.dg.utils.ChainSqlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.sql.ResultSet;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * sql 解析器
 *
 * @author VD
 * @date 2025/8/5 20:10
 */
public class ChainSqlParser implements ValidationChainParser {

    /**
     * 初始化
     *
     * @param config 配置
     */
    @Override
    public void init(ValidationChainConfig config) {
        Map<String, Function<String, Object>> converterMap = Maps.newHashMap();
        ChainDataConfig.fieldConsumer()
                .andThen(ChainSqlConfig.fieldConsumer())
                .accept(converterMap);
        ChainSqlConfig sqlConfig = BeanMapUtils.map2Bean(config.getSqlChainDataMap(), converterMap, ChainSqlConfig.class);
        ChainConfigHolder.registerDataConfig(type().getType(), sqlConfig);
        try {
            // 驱动初始化
            if (Objects.nonNull(sqlConfig) && StringUtils.isNotBlank(sqlConfig.getDriverClassName())) {
                Class.forName(sqlConfig.getDriverClassName());
            }
        } catch (Exception e) {
            throw new ValidationChainParserException("sql parser init driver exception : " + e.getMessage(), e);
        }
    }

    /**
     * 解析
     *
     * @param config 配置
     * @return 验证链
     */
    @Override
    public List<ValidationChain> parse(ValidationChainConfig config) {
        List<ValidationChain> chainList = Lists.newArrayList();
        ChainDataConfig dataConfig = ChainConfigHolder.getDataConfig(ParserTypeEnum.SQL.getType());
        if (Objects.isNull(dataConfig)) {
            return chainList;
        }
        ChainSqlConfig chainSqlConfig = (ChainSqlConfig) dataConfig;

        // 先查询总数
        String countSql = ChainSqlUtils.genCountSql(chainSqlConfig);
        Long count = ChainSqlUtils.executeSql(chainSqlConfig, countSql, this::executeCountSql);
        if (count == 0) {
            return chainList;
        }

        // 再查询分页数据
        String pageSql = ChainSqlUtils.genPageSql(chainSqlConfig, 1, 500);
        List<ChainSqlDO> sqlDOList = ChainSqlUtils.executeSql(chainSqlConfig, pageSql, ChainSqlUtils::executeSelectSql);
        int dateSize = sqlDOList.size();
        int pageNo = 2;
        while (count > dateSize) {
            pageSql = ChainSqlUtils.genPageSql(chainSqlConfig, pageNo, 500);
            List<ChainSqlDO> pageData = ChainSqlUtils.executeSql(chainSqlConfig, pageSql, ChainSqlUtils::executeSelectSql);
            sqlDOList.addAll(pageData);
            dateSize += pageData.size();
            pageNo++;
        }

        // 根据chainId进行分组
        Map<String, List<ChainSqlDO>> chainSqlMap = sqlDOList.stream().collect(Collectors.groupingBy(ChainSqlDO::getChainId));
        chainSqlMap.forEach((chainId, chainSqlDOList) -> {
            ValidationChain chain = new ValidationChain();
            chain.setGroup(type().getType());
            chain.setChainId(chainId);
            chainSqlDOList.stream().findFirst().ifPresent(source -> {
                chain.setGuardExpire(source.getGuardExpire());
                chain.setGuardThreshold(source.getGuardThreshold());
            });
            chain.setNodes(chainSqlDOList.stream()
                    .sorted(Comparator.comparingInt(ChainSqlDO::getOrder))
                    .map(ChainSqlDO::converter2node)
                    .collect(Collectors.toList()));
            chainList.add(chain);
        });

        return chainList;
    }

    /**
     * 解析
     *
     * @param info 内容
     * @return 验证链
     */
    @Override
    public List<ValidationChain> parse(String info) {
        return null;
    }

    /**
     * 执行计数sql
     *
     * @param resultSet      结果集
     * @param chainSqlConfig 链sql配置
     * @return 数量
     */
    private Long executeCountSql(ResultSet resultSet, ChainSqlConfig chainSqlConfig) {
        try {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0L;
        } catch (Exception e) {
            throw new ChainSqlExecuteException("count sql execute converter result exception", e);
        }
    }


    /**
     * 类型
     *
     * @return 类型
     */
    @Override
    public ParserTypeEnumerable type() {
        return ParserTypeEnum.SQL;
    }
}
