CREATE TABLE `validation_chain`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `chain_id`        varchar(64)   NOT NULL COMMENT '流程ID（关联流程链的唯一标识）',
    `script`          varchar(2000) NOT NULL COMMENT '脚本内容（存储具体的脚本代码）',
    `language`        varchar(32)   NOT NULL COMMENT '脚本语言（如：groovy、javascript、python）',
    `message`         varchar(512)  NOT NULL COMMENT '提示信息（脚本执行相关的说明或错误提示）',
    `fast_fail`       tinyint(1) NOT NULL DEFAULT 0 COMMENT '快速失败标识（0-不启用，1-启用：脚本执行失败后终止整个流程）',
    `order`           int(11) NOT NULL DEFAULT 10 COMMENT '排序号（用于同一流程内多个脚本的执行顺序）',
    `guard_expire`    bigint(20) NOT NULL DEFAULT 10 COMMENT '过期时间（脚本执行完成之后，如果未返回结果，则超过该时间则认为脚本执行失败）',
    `guard_threshold` bigint(20) NOT NULL DEFAULT 100 COMMENT '告警阈值（脚本执行完成之后，如果返回结果超过该值则认为脚本执行失败）',
    `deleted`         bigint(20) NOT NULL DEFAULT 0 COMMENT '删除状态（0-未删除，id-已删除）',
    `create_time`     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY               `idx_chain_id` (`chain_id`) COMMENT '流程ID索引',
    KEY               `idx_update_time` (`update_time`) COMMENT '更新时间索引',
    KEY               `idx_deleted` (`deleted`) COMMENT '删除状态索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证流程'