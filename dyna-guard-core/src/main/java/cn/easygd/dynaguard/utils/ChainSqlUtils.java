package cn.easygd.dynaguard.utils;

import cn.easygd.dynaguard.core.parser.sql.ChainSqlDO;
import cn.easygd.dynaguard.domain.config.ChainSqlConfig;
import cn.easygd.dynaguard.domain.exception.ChainSqlExecuteException;
import com.google.common.collect.Lists;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * 流程sql工具类
 *
 * @author VD
 * @version v 0.1 2025/8/7 18:19
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
     * 获取更新sql
     */
    private static final String SELECT_UPDATE_SQL = "select %s from %s where %s >= %s";

    /**
     * 获取流程列表sql
     */
    private static final String SELECT_BY_CHAIN_ID_SQL = "select %s from %s where %s in (%s) and %s = 0";

    /**
     * 生成流程数量sql
     *
     * @param sqlConfig 配置
     * @return sql
     */
    public static String genCountSql(ChainSqlConfig sqlConfig) {
        return String.format(COUNT_SQL, sqlConfig.getTableName(), formatColumn(sqlConfig.getDeletedField()));
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
                formatColumn(sqlConfig.getDeletedField()),
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
                formatColumn(sqlConfig.getUpdateTimeField()),
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
                formatColumn(sqlConfig.getChainIdField()),
                chainIdList.stream().map(source -> String.format("'%s'", source)).collect(Collectors.joining(",")),
                sqlConfig.getDeletedField());
    }

    /**
     * 获取字段列表
     *
     * @param sqlConfig 配置
     * @return 字段列表
     */
    public static List<String> getColumnList(ChainSqlConfig sqlConfig) {
        return Lists.newArrayList(formatColumn("id"),
                formatColumn(sqlConfig.getChainIdField()),
                formatColumn(sqlConfig.getLanguageField()),
                formatColumn(sqlConfig.getScriptField()),
                formatColumn(sqlConfig.getDeletedField()),
                formatColumn(sqlConfig.getCreateTimeField()),
                formatColumn(sqlConfig.getUpdateTimeField()),
                formatColumn(sqlConfig.getOrderField()),
                formatColumn(sqlConfig.getMessageField()),
                formatColumn(sqlConfig.getFastFailField()),
                formatColumn(sqlConfig.getGuardExpireField()),
                formatColumn(sqlConfig.getGuardThresholdField()));
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
            throw new ChainSqlExecuteException("select sql execute converter result exception", e);
        }
    }

    /**
     * 格式化字段
     *
     * @param column 字段
     * @return 字段
     */
    public static String formatColumn(String column) {
        return String.format("`%s`", column);
    }
}
