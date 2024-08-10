package org.blb.controller;

import jakarta.validation.ConstraintViolationException;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.errorDto.FieldErrorDto;
import org.blb.DTO.validationErrorDto.ValidationErrorDto;
import org.blb.DTO.validationErrorDto.ValidationErrorsDto;
import org.blb.exeption.AlreadyExistException;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.NullArgException;
import org.blb.exeption.RestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handlerNotFoundException(NotFoundException exception){
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .message(exception.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<StandardResponseDto> handlerRestException(RestException exception) {
        return new ResponseEntity<>(new StandardResponseDto(exception.getMessage()), exception.getStatus());
    }


        @ExceptionHandler(AlreadyExistException.class)
        public ResponseEntity<ErrorResponseDto> handlerAlreadyExistException(AlreadyExistException exception){
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .message(exception.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }


        @ExceptionHandler(NullPointerException.class)
        public ResponseEntity<ErrorResponseDto> handlerNullPointerException(NullPointerException exception){
            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .message(exception.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(NullArgException.class)
        public ResponseEntity<String> handlerNullArgException(NullArgException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        //при валилации данных когда @RequestParam или @PathVariable
        //а также при валидации сущностей, управляемых JPA
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponseDto> handlerConstraintViolationException(ConstraintViolationException exception){
            List<FieldErrorDto> fieldErrors = new ArrayList<>();

            exception.getConstraintViolations().forEach(violation -> {
                FieldErrorDto fieldError = FieldErrorDto.builder()
                        .field(violation.getPropertyPath().toString())
                        .message(violation.getMessage())
                        .build();
                fieldErrors.add(fieldError);
            });

            ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                    .message("Validation failed")
                    .fieldErrors(fieldErrors)
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        //при валидации данных с @RequestBody
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage()));
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

        @ExceptionHandler(value = MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorsDto> handleValidationException(MethodArgumentNotValidException e) {

            List<ValidationErrorDto> validationErrors = new ArrayList<>();

            List<ObjectError> errors = e.getBindingResult().getAllErrors();

            for (ObjectError error : errors) {
                FieldError fieldError = (FieldError)error;
                ValidationErrorDto errorDto = ValidationErrorDto.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build();

                validationErrors.add(errorDto);
            }

            return ResponseEntity
                    .badRequest()
                    .body(ValidationErrorsDto.builder()
                            .errors(validationErrors)
                            .build());
        }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException exception) {
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .message("An unexpected error occurred: " + exception.getMessage())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
