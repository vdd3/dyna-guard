package com.dg.utils;

import com.dg.core.parser.sql.ChainSqlDO;
import com.dg.domain.config.ChainSqlConfig;
import com.dg.domain.exception.ChainSqlExecuteException;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.BiFunction;

/**
 * 流程sql工具类
 *
 * @author VD
 * @date 2025/8/7 18:19
 */
public class ChainSqlUtils {

    /**
     * 获取流程数量sql
     */
    private static final String COUNT_SQL = "select count(1) from %s where %s = 0";

    /**
     * 获取分页sql
     */
    private static final String PAGE_SQL = "select %s from %s where %s = 0 order by id limit %s,%s";

    /**
     * 获取分页更新sql
     */
    private static final String SELECT_UPDATE_SQL = "select %s from %s where %s >= %s";

    /**
     * 获取流程列表sql
     */
    private static final String SELECT_BY_CHAIN_ID_SQL = "select %s from %s where %s in (%s)";

    /**
     * 生成流程数量sql
     *
     * @param sqlConfig 配置
     * @return sql
     */
    public static String genCountSql(ChainSqlConfig sqlConfig) {
        return String.format(COUNT_SQL, sqlConfig.getTableName(), sqlConfig.getDeletedField());
    }

    /**
     * 生成分页sql
     *
     * @param sqlConfig 配置
     * @param pageNum   页码
     * @param pageSize  每页数量
     * @return sql
     */
    public static String genPageSql(ChainSqlConfig sqlConfig, Integer pageNum, Integer pageSize) {
        return String.format(PAGE_SQL,
                String.join(",", getColumnList(sqlConfig)),
                sqlConfig.getTableName(),
                sqlConfig.getDeletedField(),
                (pageNum - 1) * pageSize,
                pageSize);
    }

    /**
     * 生成查询更新sql
     *
     * @param sqlConfig  配置
     * @param updateTime 更新时间
     * @return sql
     */
    public static String genSelectUpdateSql(ChainSqlConfig sqlConfig, String updateTime) {
        return String.format(SELECT_UPDATE_SQL,
                String.join(",", getColumnList(sqlConfig)),
                sqlConfig.getTableName(),
                sqlConfig.getUpdateTimeField(),
                updateTime);
    }

    /**
     * 生成查询指定流程sql
     *
     * @param sqlConfig   配置
     * @param chainIdList 流程id列表
     * @return sql
     */
    public static String genSelectByChainIdSql(ChainSqlConfig sqlConfig, List<String> chainIdList) {
        return String.format(SELECT_BY_CHAIN_ID_SQL,
                String.join(",", getColumnList(sqlConfig)),
                sqlConfig.getTableName(),
                sqlConfig.getChainIdField(),
                String.join(",", chainIdList));
    }

    /**
     * 获取字段列表
     *
     * @param sqlConfig 配置
     * @return 字段列表
     */
    public static List<String> getColumnList(ChainSqlConfig sqlConfig) {
        return Lists.newArrayList(sqlConfig.getChainIdField(),
                sqlConfig.getLanguageField(),
                sqlConfig.getScriptField(),
                sqlConfig.getDeletedField(),
                sqlConfig.getCreateTimeField(),
                sqlConfig.getUpdateTimeField(),
                sqlConfig.getOrderField(),
                sqlConfig.getMessageField(),
                sqlConfig.getFastFailField(),
                sqlConfig.getGuardExpireField(),
                sqlConfig.getGuardThresholdField());
    }

    /**
     * 执行sql
     *
     * @param chainSqlConfig 链sql配置
     * @param sql            sql
     * @param function       函数
     * @param <T>            返回类型
     * @return 结果
     */
    public static <T> T executeSql(ChainSqlConfig chainSqlConfig, String sql, BiFunction<ResultSet, ChainSqlConfig, T> function) {
        // jdbc链接
        String url = chainSqlConfig.getUrl();
        // 用户名
        String username = chainSqlConfig.getUsername();
        // 密码
        String password = chainSqlConfig.getPassword();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

            // 执行sql
            ResultSet resultSet = statement.executeQuery();
            return function.apply(resultSet, chainSqlConfig);
        } catch (ChainSqlExecuteException e) {
            throw e;
        } catch (Exception e) {
            throw new ChainSqlExecuteException("chain sql execute exception : " + e.getMessage(), e);
        }
    }

    /**
     * 执行查询sql
     *
     * @param resultSet      结果集
     * @param chainSqlConfig 链sql配置
     * @return 数据
     */
    public static List<ChainSqlDO> executeSelectSql(ResultSet resultSet, ChainSqlConfig chainSqlConfig) {
        try {
            List<ChainSqlDO> doList = Lists.newArrayList();
            while (resultSet.next()) {
                ChainSqlDO chainSqlDO = new ChainSqlDO();
                chainSqlDO.setChainId(resultSet.getString(chainSqlConfig.getChainIdField()));
                chainSqlDO.setLanguage(resultSet.getString(chainSqlConfig.getLanguageField()));
                chainSqlDO.setScript(resultSet.getString(chainSqlConfig.getScriptField()));
                chainSqlDO.setDeleted(resultSet.getBoolean(chainSqlConfig.getDeletedField()));
                chainSqlDO.setCreateTime(resultSet.getDate(chainSqlConfig.getCreateTimeField()));
                chainSqlDO.setUpdateTime(resultSet.getDate(chainSqlConfig.getUpdateTimeField()));
                chainSqlDO.setOrder(resultSet.getInt(chainSqlConfig.getOrderField()));
                chainSqlDO.setMessage(resultSet.getString(chainSqlConfig.getMessageField()));
                chainSqlDO.setFastFail(resultSet.getBoolean(chainSqlConfig.getFastFailField()));
                chainSqlDO.setGuardExpire(resultSet.getLong(chainSqlConfig.getGuardExpireField()));
                chainSqlDO.setGuardThreshold(resultSet.getLong(chainSqlConfig.getGuardThresholdField()));
                doList.add(chainSqlDO);
            }
            return doList;
        } catch (Exception e) {
            throw new ChainSqlExecuteException("count select sql execute converter result exception", e);
        }
    }
}
