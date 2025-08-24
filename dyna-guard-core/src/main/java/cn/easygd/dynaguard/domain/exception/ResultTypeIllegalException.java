package cn.easygd.dynaguard.domain.exception;

/**
 * 返回结果类型非法异常
 *
 * @author VD
 * @version v 0.1 2025/8/12 19:47
 */
public class ResultTypeIllegalException extends RuntimeException {

    public ResultTypeIllegalException(String message) {
        super(message);
    }

    public ResultTypeIllegalException() {
        super("返回结果必须为布尔值");
    }
}
