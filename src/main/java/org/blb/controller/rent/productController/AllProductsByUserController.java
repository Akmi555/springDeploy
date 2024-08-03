package org.blb.controller.rent.productController;

import lombok.AllArgsConstructor;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.controller.api.rent.product.AllProductsByUserControllerApi;
import org.blb.service.rent.productServise.AllProductsByUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/rents/my")
public class AllProductsByUserController implements AllProductsByUserControllerApi {

    private final AllProductsByUserService allProductsByUserService;


    @GetMapping
    public ResponseEntity<ProductSearchResponse> getUserProducts(
            @RequestParam(defaultValue = "0") int page) {
        ProductSearchResponse response = allProductsByUserService.findUserProducts(page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
