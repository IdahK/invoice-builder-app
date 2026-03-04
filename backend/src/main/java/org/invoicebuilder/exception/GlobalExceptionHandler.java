package org.invoicebuilder.exception;

import org.invoicebuilder.exception.common.ResourceNotFoundException;
import org.invoicebuilder.exception.common.UnsupportedApiVersionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        ArrayList<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.add(String.format("%s: %s", error.getField(), error.getDefaultMessage())));
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                "Validation failed",
                errors
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
                "Validation failed",
                errors
        );

        if (headers != null) {
            headers.add("API_ERROR_TYPE", "BindException");
        }

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorType.ERROR,
                "Resource not found",
                Collections.singletonList("The requested resource could not be found")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "ResourceNotFoundException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorType.ERROR,
                "Resource not found",
                Collections.singletonList("The requested resource could not be found")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "EntityNotFoundException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex) {
        ArrayList<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> 
            errors.add(String.format("%s: %s", violation.getPropertyPath(), violation.getMessage())));

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                "Validation failed",
                errors
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "ConstraintViolationException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                ErrorType.ERROR,
                "Data conflict",
                Collections.singletonList("The request could not be completed due to a data conflict")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "DataIntegrityViolationException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                "Invalid request format",
                Collections.singletonList("Request body must be valid JSON. Please check your request syntax and try again.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "HttpMessageNotReadableException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String error = String.format("Parameter '%s' should be of type %s", 
            ex.getName(), ex.getRequiredType().getSimpleName());
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                "Invalid parameter type",
                Collections.singletonList(error + ". Please check the parameter type and try again.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "MethodArgumentTypeMismatchException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String error = String.format("Required parameter '%s' is missing", ex.getParameterName());
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ErrorType.ERROR,
                "Missing required parameter",
                Collections.singletonList(error + ". Please include all required parameters.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "MissingServletRequestParameterException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuilder supportedMethods = new StringBuilder();
        ex.getSupportedHttpMethods().forEach(method -> 
            supportedMethods.append(method).append(", "));
        
        String error = String.format("Request method '%s' not supported. Supported methods: %s", 
            ex.getMethod(), supportedMethods.toString().replaceAll(", $", ""));

        ApiError apiError = new ApiError(
                HttpStatus.METHOD_NOT_ALLOWED,
                ErrorType.ERROR,
                "Method not allowed",
                Collections.singletonList(error + ". Please use a supported HTTP method.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "HttpRequestMethodNotSupportedException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        StringBuilder supportedTypes = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(type -> 
            supportedTypes.append(type).append(", "));
        
        String error = String.format("Content type '%s' not supported. Supported types: %s", 
            ex.getContentType(), supportedTypes.toString().replaceAll(", $", ""));

        ApiError apiError = new ApiError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                ErrorType.ERROR,
                "Unsupported content type",
                Collections.singletonList(error + ". Please use a supported content type.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "HttpMediaTypeNotSupportedException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String error = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorType.ERROR,
                "Endpoint not found",
                Collections.singletonList("The requested endpoint does not exist. Please check the URL and try again.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "NoHandlerFoundException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(UnsupportedApiVersionException.class)
    public ResponseEntity<ApiError> handleUnsupportedApiVersionException(UnsupportedApiVersionException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ErrorType.ERROR,
                "API version not supported",
                Collections.singletonList("This API version is not supported. Please use a supported version.")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "UnsupportedApiVersionException");
        headers.add("X-Supported-Versions", ex.getSupportedVersions());

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorType.ERROR,
                "Server error",
                Collections.singletonList(ex.getMessage())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.add("API_ERROR_TYPE", "GenericException");

        return new ResponseEntity<>(apiError, headers, apiError.getStatus());
    }

}
