package com.philips.itaap.utility.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

public final class ServiceException extends ResponseStatusException {
    private static final long serialVersionUID = -23598639273124256L;
    private final Integer errCode;

    public ServiceException(HttpStatusCode status, int errorCode, String reason) {
        super(status, reason);
        this.errCode = errorCode;
    }

    public Integer getErrCode() {
        return this.errCode;
    }
}
