package org.blb.controller.rent.productController;

import lombok.AllArgsConstructor;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.controller.api.rent.product.FindProductControllerApi;
import org.blb.service.rent.productServise.FindProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/rents")
public class FindProductController implements FindProductControllerApi {

    private final FindProductService findProductService;

    @GetMapping
    public ResponseEntity<ProductSearchResponse> findProducts(
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page) {
        ProductSearchResponse response = findProductService.findProducts(region, category, name, page);

        if (response.getError() != null) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = findProductService.findProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
