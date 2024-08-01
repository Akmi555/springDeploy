package org.blb.DTO.appDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "ErrorValidation", description = "Any error validation message from server ")
public class ResponseErrors {
        @Schema(description = "Request field which not pass validation", example = "email")
        private final String fieldName;
        @Schema(description = "Message with problem", example = "size must be between 2 and 15")
        private final String message;


}
