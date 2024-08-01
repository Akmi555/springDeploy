package org.blb.controller.api.region;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.region.RegionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/regions")
//@CrossOrigin(origins = "http://localhost:5173")
public interface FindRegionApi {
    @Operation(summary = "Getting all regions", description = "The operation is available to everyone, return all regions")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return all regions",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class))),

            @ApiResponse(responseCode = "404", description = "No regions found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"No regions found\"}")))
    })

    @GetMapping
    ResponseEntity<List<RegionDTO>> findAllRegions();

    @Operation(summary = "Getting region by ID", description = "The operation is available to everyone, return region by ID")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return region by ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class))),

            @ApiResponse(responseCode = "404", description = "Region with ID = ...  not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Region with ID = 25 not found\"}")))
    })

    @GetMapping("/findById")
    ResponseEntity<RegionDTO> findRegionById(@Valid @RequestParam Long id);

    @Operation(summary = "Getting region by name", description = "The operation is available to everyone, return region by name")
    @ApiResponses( value = {
            @ApiResponse(responseCode = "200", description ="Return region by name",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegionDTO.class))),

            @ApiResponse(responseCode = "404", description = "Region with name ...  not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Region with name '...' not found\"}")))
    })
    @GetMapping("/findBy")
    ResponseEntity<RegionDTO> findRegionByName(@Valid @RequestParam String region);
}
