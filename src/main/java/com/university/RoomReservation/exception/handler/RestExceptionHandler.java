package com.university.RoomReservation.exception.handler;

import com.university.RoomReservation.exception.response.ApiError;
import com.university.RoomReservation.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        ApiError apiError = ApiError.builder()
                .status(BAD_REQUEST.value())
                .message("Malformed request")
                .debugMessage(ex.getMessage()).build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .status(NOT_FOUND.value())
                .message("Resource not found")
                .debugMessage(ex.getMessage()).build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralException(Exception ex) {
        ApiError apiError = ApiError.builder()
                .status(INTERNAL_SERVER_ERROR.value())
                .message("General server exception - " + ex.getMessage())
                .debugMessage(Objects.isNull(ex.getCause()) ? null : ex.getCause().toString()).build();
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return ResponseEntity.status(apiError.getStatus()).body(apiError);
    }

}
