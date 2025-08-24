package cn.easygd.dynaguard.domain.exception;

/**
 * 验证链引擎异常
 *
 * @author VD
 * @version v 0.1 2025/8/13 21:10
 */
public class ValidationChainEngineException extends RuntimeException {

    public ValidationChainEngineException(String message) {
        super(message);
    }

    public ValidationChainEngineException(String message, Throwable cause) {
        super(message, cause);
    }
}
