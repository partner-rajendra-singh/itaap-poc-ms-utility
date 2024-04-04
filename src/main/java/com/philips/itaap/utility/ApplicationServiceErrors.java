package com.philips.itaap.utility;

import com.philips.itaap.ms.dev.base.exception.ServiceErrorsFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
enum ApplicationServiceErrors implements ServiceErrorsFactory {

    /* 
    Define all errors reported in application here as part of enum constants. 
    For every error, define httpstatus code, internal error code and a custom description for the error message. 
    It is advised to use the 4 digit internal error code as a standard for all services. 
    Below are the sample enum constants. 
    With this definition, global exception handling in place, caller gets a consistent error message. 
    Ex: 
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1500, "User not found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 1501, "Bad request");
    */
    ;
    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String defaultDesc;
}
