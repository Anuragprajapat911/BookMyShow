package com.fun.bookMyShow.exceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@Data
@RestControllerAdvice
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private Date timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
