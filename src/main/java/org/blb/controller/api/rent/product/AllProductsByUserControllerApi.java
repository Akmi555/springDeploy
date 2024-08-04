package org.blb.controller.api.rent.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.blb.DTO.rent.ProductSearchResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rents/my")
public interface AllProductsByUserControllerApi {

    /**
     * Find all products of an authorized user.
     *
     * @return ResponseEntity with the response containing found products or errors.
     */
    @Operation(summary = "Search Products by current-authorized user",
            description = "Operation to search for products of an authorized user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{ \"products\": [...], \"error\": null }"))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"timestamp\": \"2024-08-03T15:30:44.753+00:00\",\n" +
                                    "    \"status\": 403,\n" +
                                    "    \"error\": \"Forbidden\",\n" +
                                    "    \"path\": \"/api/rents/my\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "404", description = "No products by current user",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"message\": \"You have not products yet.\"\n" +
                                    "}")))
    })
    @GetMapping
    ResponseEntity<ProductSearchResponse> getUserProducts(
            @RequestParam(defaultValue = "0") int page);
}
