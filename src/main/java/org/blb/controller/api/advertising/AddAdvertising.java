package org.blb.controller.api.advertising;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.advertisingDto.AdvertisingRequestDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/admin/advertising")
public interface AddAdvertising {
    @Operation(summary = "Adding new advertising", description = "The operation is available to registered users, add new advertising")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertising added",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertising added successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertising not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertising not found\"}")))
    })
    @PostMapping("/add")
    ResponseEntity<AdvertisingResponseDto> addAdvertising(@RequestBody @Valid AdvertisingRequestDto dto);
}
