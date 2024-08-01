package org.blb.DTO.validationErrorDto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class ValidationErrorDto {

    private String field;
    private String message;

    public ValidationErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
