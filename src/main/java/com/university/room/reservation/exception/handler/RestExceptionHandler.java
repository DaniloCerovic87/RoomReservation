package com.university.room.reservation.exception.handler;

import com.university.room.reservation.exception.ResourceNotFoundException;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.exception.response.ApiError;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final MessageSource messageSource;


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError.getDefaultMessage(), new Object[]{}, LocaleContextHolder.getLocale()))
                .collect(Collectors.toList());
        ApiError apiError = ApiError.builder()
                .status(BAD_REQUEST.value())
                .message("Validation failed")
                .errorMessages(errorMessages)
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        String errorMessage = messageSource.getMessage(ex.getMessageKey(), ex.getParams(), LocaleContextHolder.getLocale());
        ApiError apiError = ApiError.builder()
                .status(BAD_REQUEST.value())
                .message("Malformed request")
                .debugMessage(errorMessage).build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        String errorMessage = messageSource.getMessage(ex.getMessageKey(), new Object[]{}, LocaleContextHolder.getLocale());
        ApiError apiError = ApiError.builder()
                .status(NOT_FOUND.value())
                .message("Resource not found")
                .debugMessage(errorMessage).build();
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
