package org.blb.controller.api.rent.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rent")
public interface UpdateProductControllerApi {

    @Operation(summary = "Update a product by ID",
            description = "Updates an existing product identified by its ID. " +
                    "Note: The fields `imageUrl` , `category` and `region` will be ignored.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Product successfully updated",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product successfully updated\"}"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"timestamp\": \"2024-07-30T16:41:37.865+00:00\",\n" +
                                    "    \"status\": 403,\n" +
                                    "    \"error\": \"Forbidden\",\n" +
                                    "    \"path\": \"/api/rent/...\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404",
                    description = "Product not found with the given ID",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product with ID ... not found.\"}"))),
            @ApiResponse(responseCode = "409", description = "Only the author can update their ad",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"You do not have permission to update this product\"}"))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Internal server error.\"}")))
    })
    @PutMapping("/{id}")
    ResponseEntity<OneMessageDTO> updateProduct(
            @Parameter(description = "ID of the product to be updated", example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Product data to update. Fields `category` and `region` will be ignored.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductCreateRequestDto.class),
                            examples = @ExampleObject(value = "{ \"name\": \"New Product Name\", \"price\": 99.99, \"description\": \"Updated description\" }")))
            @RequestBody ProductCreateRequestDto productCreateRequestDto);
}
