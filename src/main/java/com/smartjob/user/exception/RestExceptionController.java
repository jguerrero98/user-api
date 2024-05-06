package com.smartjob.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionController {

    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<ExceptionType> handleAllExceptions(BusinessException ex) {
        ExceptionType error = new ExceptionType();
        error.setMessage(ex.getMessage());
        return new ResponseEntity<>(error, ex.getHttpStatus());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ResponseEntity<ExceptionType> userNameNotFound(UsernameNotFoundException ex) {
        ExceptionType error = new ExceptionType();
        error.setMessage("");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ExceptionType error = new ExceptionType();
        error.setMessage("Validacion fallida: " + bindingResult.getFieldErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
