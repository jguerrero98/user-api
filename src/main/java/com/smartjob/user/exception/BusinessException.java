package com.smartjob.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException{

    private final String message;
    private final HttpStatus httpStatus;

}
