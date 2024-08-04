package org.blb.controller.rent.productController;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.controller.api.rent.product.AddProductControllerApi;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.rent.productServise.AddProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/rent")
@AllArgsConstructor
public class AddProductController implements AddProductControllerApi {

    private final AddProductService addProductService;
    private final ObjectMapper objectMapper;


    // Method for adding a product with an image
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> addNewProduct(
            @RequestPart("product") String productJson,
            @RequestPart(required = false, value = "image") MultipartFile image) {
        try {
            // Convert JSON to ProductCreateRequestDto object
            ProductCreateRequestDto request = objectMapper.readValue(productJson, ProductCreateRequestDto.class);
            // Add a product with an image
            return addProductService.addProduct(request, image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid JSON format or error during product creation: " + e.getMessage());
        }
    }

    // Method for adding a product without image
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        return addProductService.addProductWithoutImage(request);
    }
}
