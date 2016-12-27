package me.j360.shiro.webclient.servlet.shiro.exception;

import org.apache.shiro.authc.CredentialsException;

public class ExpiredQrcodeException extends CredentialsException {

    /**
     * Creates a new ExpiredQrcodeException.
     */
    public ExpiredQrcodeException() {
        super();
    }

    /**
     * Constructs a new ExpiredQrcodeException.
     *
     * @param message the reason for the exception
     */
    public ExpiredQrcodeException(String message) {
        super(message);
    }

    /**
     * Constructs a new ExpiredQrcodeException.
     *
     * @param cause the underlying Throwable that caused this exception to be thrown.
     */
    public ExpiredQrcodeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new ExpiredQrcodeException.
     *
     * @param message the reason for the exception
     * @param cause   the underlying Throwable that caused this exception to be thrown.
     */
    public ExpiredQrcodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
