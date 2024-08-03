package org.blb.controller.api.rent.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.validationErrorDto.ValidationErrorsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/rent")
public interface AddProductControllerApi {

    @Operation(summary = "Add new product with image", description = "The operation is available to registered users to add a new product with an image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product successfully created\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid JSON format or error during product creation",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Invalid JSON format or error during product creation: ...\"}"))),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"An unexpected error occurred. Please try again later.\" }")))
    })
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<?> addNewProduct(
            @RequestPart("product") String productJson,
            @RequestPart(required = false, value = "image") MultipartFile image);

    @Operation(summary = "Add new product", description = "The operation is available to registered users to add a new product without an image.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"Product successfully created\"}"))),
            @ApiResponse(responseCode = "404", description = "Region or category not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"errors\": [\n" +
                                    "        {\n" +
                                    "            \"field\": \"category\",\n" +
                                    "            \"message\": \"Category with name ... not found.\"\n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"field\": \"region\",\n" +
                                    "            \"message\": \"Region with name ... not found.\"\n" +
                                    "        }\n" +
                                    "    ]\n" +
                                    "}"))),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"message\": \"An unexpected error occurred. Please try again later.\" }")))
    })
    @PostMapping(consumes = "application/json")
    ResponseEntity<?> addNewProduct(@RequestBody ProductCreateRequestDto request);
}

