package cn.easygd.dynaguard.domain.exception;

/**
 * 验证链引擎异常
 *
 * @author VD
 */
public class ValidationChainEngineException extends RuntimeException {

    public ValidationChainEngineException(String message) {
        super(message);
    }

    public ValidationChainEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
