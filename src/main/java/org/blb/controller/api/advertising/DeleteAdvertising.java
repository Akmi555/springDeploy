package org.blb.controller.api.advertising;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/admin/advertising")
public interface DeleteAdvertising {

    @Operation(summary = "Deleting advertising", description = "The operation is available to the owner of the advertising or admin")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertising deleting",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertising removed successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertising not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertising not found\"}")))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<StandardResponseDto> deleteAdvertising(@PathVariable Long id);
}
