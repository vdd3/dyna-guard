package cn.easygd.dynaguard.domain.exception;

/**
 * 熔断异常
 *
 * @author VD
 * @version v 0.1 2025/8/13 19:23
 */
public class GuardException extends RuntimeException {

    public GuardException(String message) {
        super(message);
    }
}
