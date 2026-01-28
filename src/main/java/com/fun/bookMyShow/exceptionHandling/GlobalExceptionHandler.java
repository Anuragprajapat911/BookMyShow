package com.fun.bookMyShow.exceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request)

    {

        ErrorResponse errorResponse = new ErrorResponse(new Date() ,HttpStatus.NOT_FOUND.value()

                                ,"Not Found"
                                ,ex.getMessage()
                                    ,request.getDescription(false));


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(SeatUnavilableExcepation.class)
    public ResponseEntity<?> SeatUnavilableExcepation(SeatUnavilableExcepation ex, WebRequest request)

    {

        ErrorResponse errorResponse = new ErrorResponse(new Date() ,HttpStatus.NOT_FOUND.value()

                ,"Seat Not Available"
                ,ex.getMessage()
                ,request.getDescription(false));


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request)

    {

        ErrorResponse errorResponse = new ErrorResponse(new Date() ,HttpStatus.NOT_FOUND.value()

                ,"Internel server Error By By"
                ,ex.getMessage()
                ,request.getDescription(false));


        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

}
