package org.invoicebuilder.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorType.ERROR,
                ex.getLocalizedMessage(),
                Collections.singletonList(ex.getLocalizedMessage()),
                ex.toString()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "MethodArgumentNotValidException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    public ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<String> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();

        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add(String.format("%s : %s", error.getField(), error.getDefaultMessage()));
        }


        for (ObjectError error : bindingResult.getGlobalErrors()) {
            errors.add(String.format("%s : %s", error.getObjectName(), error.getDefaultMessage()));
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                ex.getLocalizedMessage(),
                errors,
                ex.toString()
        );

        if (headers != null) {
            headers.add("API_ERROR_TYPE", "BindException");
        }

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

}
