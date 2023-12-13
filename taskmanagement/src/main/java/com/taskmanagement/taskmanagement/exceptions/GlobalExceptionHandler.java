package com.taskmanagement.taskmanagement.exceptions;


import com.taskmanagement.taskmanagement.constants.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse>resourceNotFoundException(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ResourceNotFoundExceptionForStatus.class)
    public ResponseEntity<ApiResponse>resourceNotFoundExceptionForStatus(ResourceNotFoundExceptionForStatus ex){
        String message = ex.getMessage();
        ApiResponse apiResponse = new ApiResponse(message,false);
        return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ResponseEntity<Map<String,String>> handleMethodArgsNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> resp = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error)->{
       String fieldName=((FieldError)error).getField();
       String message = error.getDefaultMessage();
       resp.put(fieldName,message);
        });
        return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String,String>> requestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception){
        Map<String , String> resp = new HashMap<>();
        String message = exception.getMessage();
        String method = exception.getMethod();
        resp.put(message,method);

        return new ResponseEntity<>(resp, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String,String>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException methodArgumentTypeMismatchException){
        Map<String,String> resp = new HashMap<>();
        String message = methodArgumentTypeMismatchException.getMessage();
        String name = methodArgumentTypeMismatchException.getName();
        resp.put(name,message);

        return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<Map<String,String>> badRequestException(HttpClientErrorException.BadRequest badRequestException){
        Map<String,String> resp = new HashMap<>();
        String message = badRequestException.getMessage();
        String name = String.valueOf(badRequestException.getCause());
        resp.put(name,message);

        return new ResponseEntity<>(resp,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleDuplicateEntryException(Exception ex) {
        String errorMessage =  ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
