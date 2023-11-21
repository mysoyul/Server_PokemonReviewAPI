package com.pokemonreview.api.exceptions.advice;

import com.pokemonreview.api.exceptions.ErrorObject;
import com.pokemonreview.api.exceptions.MyResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MyResourceException.class)
    public ResponseEntity<ErrorObject> handleResourceNotFoundException(MyResourceException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(ex.getStatusCode());
        errorObject.setMessage(ex.getMessage());

        log.error(ex.getMessage(), ex);

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(ex.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorObject> handleException(Exception e) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());  //500
        errorObject.setMessage(e.getMessage());

        log.error(e.getMessage(), e);

        return new ResponseEntity<ErrorObject>(errorObject, HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public void accessDeniedExceptionHandler(Exception e) {
        throw new AccessDeniedException(e.getMessage());
    }

    //401
    @ExceptionHandler(value = BadCredentialsException.class)
    public void badCredentialExceptionHandler(BadCredentialsException e){
        throw new BadCredentialsException(e.getMessage());
    }
}