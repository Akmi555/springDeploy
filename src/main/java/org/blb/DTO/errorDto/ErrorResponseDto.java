package org.blb.DTO.errorDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorResponseDto {
    private String message;
    private List<FieldErrorDto> fieldErrors;

    public ErrorResponseDto(String message) {
        this.message = message;
    }

    public ErrorResponseDto(String message, List<FieldErrorDto> fieldErrors) {
        this.message = message;
        this.fieldErrors = fieldErrors;
    }
}
