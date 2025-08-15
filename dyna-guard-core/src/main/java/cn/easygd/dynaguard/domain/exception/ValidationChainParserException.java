package cn.easygd.dynaguard.domain.exception;

/**
 * 验证流程解析器异常
 *
 * @author VD
 * @date 2025/8/12 20:17
 */
public class ValidationChainParserException extends RuntimeException {

    public ValidationChainParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationChainParserException(String message) {
        super(message);
    }

}
