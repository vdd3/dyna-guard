package cn.easygd.dynaguard.domain.exception;

/**
 * 验证流程监听器异常
 *
 * @author VD
 * @date 2025/8/12 20:17
 */
public class ValidationChainListenerException extends RuntimeException {

    public ValidationChainListenerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationChainListenerException(String message) {
        super(message);
    }

}
