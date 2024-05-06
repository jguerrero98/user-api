package com.smartjob.user.util;

import com.smartjob.user.exception.BusinessException;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

public class Util {

    public static void validatePatter(String value, String regex, String message) {
        if (!Pattern.matches(regex, value)) {
            throw new BusinessException(message, HttpStatus.BAD_REQUEST);
        }
    }

}
