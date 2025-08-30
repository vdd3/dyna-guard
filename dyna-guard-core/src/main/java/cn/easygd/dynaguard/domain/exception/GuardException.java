package cn.easygd.dynaguard.domain.exception;

/**
 * 熔断异常
 *
 * @author VD
 */
public class GuardException extends RuntimeException {

    public GuardException(String message) {
        super(message);
    }
}
