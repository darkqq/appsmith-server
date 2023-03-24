package com.appsmith.server.configurations.annotation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class EndPointProtectionExceptionHandler {

    @ExceptionHandler(AccessRestrictedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Mono<AccessRestrictedException> handleException(AccessRestrictedException exception){
        return Mono.just(exception);
    }
}
