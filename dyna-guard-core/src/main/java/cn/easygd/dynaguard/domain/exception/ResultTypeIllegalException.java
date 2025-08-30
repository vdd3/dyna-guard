package cn.easygd.dynaguard.domain.exception;

/**
 * 返回结果类型非法异常
 *
 * @author VD
 */
public class ResultTypeIllegalException extends RuntimeException {

    public ResultTypeIllegalException(String message) {
        super(message);
    }

    public ResultTypeIllegalException() {
        super("返回结果必须为布尔值");
    }
}
