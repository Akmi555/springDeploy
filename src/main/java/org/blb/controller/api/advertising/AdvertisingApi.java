package org.blb.controller.api.advertising;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.advertisingDto.AdditionalResponseDto;
import org.blb.DTO.advertisingDto.AdvertisingResponseDto;
import org.blb.DTO.advertisingDto.ShortAdvertisingResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/ads")
@CrossOrigin(origins = "http://localhost:5173")
public interface AdvertisingApi {

    @Operation(summary = "Finding advertising by email", description = "The operation is available to all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertising found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertising found successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertising not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertising not found\"}")))
    })
    @GetMapping("/email/{email}")
    ResponseEntity<AdvertisingResponseDto> findByAdvertiserEmail(@PathVariable String email);


    @Operation(summary = "Finding advertising by ID", description = "The operation is available to all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertising found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertising found successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertising not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertising not found\"}")))
    })
    @GetMapping("/{id}")
    ResponseEntity<AdditionalResponseDto> findAdvertisingById(@PathVariable Long id);


    @Operation(summary = "Finding five random advertisements", description = "The operation is available to all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertisements found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertisements found successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertisements not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertisements not found\"}")))
    })
    @GetMapping()
    ResponseEntity<List<ShortAdvertisingResponseDto>> findFiveRandomAdvertisings();




    @Operation(summary = "Finding all advertisements", description = "The operation is available to all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Information about advertisements found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Advertisements found successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Advertisements not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Advertisements not found\"}")))
    })
    @GetMapping("/findAll")
    ResponseEntity<List<AdvertisingResponseDto>> findAll();

    @GetMapping("/createadvertising")
       @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<String> startAdvertisements() ;
}