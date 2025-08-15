package com.easytu.dynaguard.domain.exception;

/**
 * 流程sql执行异常
 *
 * @author VD
 * @date 2025/8/12 21:33
 */
public class ChainSqlExecuteException extends RuntimeException {

    public ChainSqlExecuteException(String errorMessage) {
        super(errorMessage);
    }

    public ChainSqlExecuteException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
