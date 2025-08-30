package cn.easygd.dynaguard.domain.exception;

/**
 * 流程sql执行异常
 *
 * @author VD
 */
public class ChainSqlExecuteException extends RuntimeException {

    public ChainSqlExecuteException(String errorMessage) {
        super(errorMessage);
    }

    public ChainSqlExecuteException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
