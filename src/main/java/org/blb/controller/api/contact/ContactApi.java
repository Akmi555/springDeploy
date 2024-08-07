package org.blb.controller.api.contact;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.contactDTO.ContactDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/contact")
public interface ContactApi {
    @Operation(summary = "Sending contact form to BLB Team", description = "The operation is available to everyone, send contact data to BLB Team")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Information about contact form sending",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Contact form was sent successfully\"}"))),
            @ApiResponse(responseCode = "400", description = "Validation Bad Request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Validation error: name must not be blank, email must be valid, message must not be blank\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Failed to send email\"}")))
    })
    @PostMapping
    ResponseEntity<StandardResponseDto> addContact(@Valid @RequestBody ContactDTO dto);
    }
