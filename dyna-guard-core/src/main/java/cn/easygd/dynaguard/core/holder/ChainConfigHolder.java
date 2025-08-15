package cn.easygd.dynaguard.core.holder;

import cn.easygd.dynaguard.domain.config.ChainDataConfig;
import cn.easygd.dynaguard.domain.config.ValidationChainConfig;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 流程配置全局持有器
 *
 * @author VD
 * @date 2025/8/7 17:15
 */
public class ChainConfigHolder {

    /**
     * 持有器
     */
    private volatile static ChainConfigHolder holder;

    /**
     * 配置
     */
    private ValidationChainConfig config;

    /**
     * 数据配置
     */
    private final Map<String, ChainDataConfig> DATA_CONFIG = Maps.newConcurrentMap();

    /**
     * 初始化
     *
     * @param config 配置
     */
    public static void init(ValidationChainConfig config) {
        if (holder == null) {
            synchronized (ChainConfigHolder.class) {
                if (holder == null) {
                    holder = new ChainConfigHolder();
                }
            }
        }
        holder.config = config;
    }

    private ChainConfigHolder() {
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    public static ValidationChainConfig getConfig() {
        return holder.config;
    }

    /**
     * 注册数据配置
     *
     * @param group      分组
     * @param dataConfig 数据配置
     */
    public static void registerDataConfig(String group, ChainDataConfig dataConfig) {
        holder.DATA_CONFIG.put(group, dataConfig);
    }

    /**
     * 获取数据配置
     *
     * @param group 分组
     * @return 数据配置
     */
    public static ChainDataConfig getDataConfig(String group) {
        return holder.DATA_CONFIG.get(group);
    }
}
