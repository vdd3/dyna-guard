package cn.easygd.dynaguard.domain.exception;

/**
 * 验证流程监听器异常
 *
 * @author VD
 *
 */
public class ValidationChainListenerException extends RuntimeException {

    public ValidationChainListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationChainListenerException(String message) {
        super(message);
    }

}
