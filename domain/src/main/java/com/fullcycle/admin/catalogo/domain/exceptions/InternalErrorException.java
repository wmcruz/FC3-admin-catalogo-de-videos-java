package com.fullcycle.admin.catalogo.domain.exceptions;

public class InternalErrorException extends NoStacktraceException {

    protected InternalErrorException(final String aMessage, final Throwable throwable) {
        super(aMessage, throwable);
    }

    public static InternalErrorException with(final String message, final Throwable throwable) {
        return new InternalErrorException(message, throwable);
    }
}