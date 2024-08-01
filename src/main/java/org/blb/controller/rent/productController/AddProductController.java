package org.blb.controller.rent.productController;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.controller.api.rent.product.AddProductControllerApi;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.rent.productServise.AddProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/rent")
@AllArgsConstructor
public class AddProductController implements AddProductControllerApi {

    private final AddProductService addProductService;

    @PostMapping
    public ResponseEntity<?> addNewProduct(@Valid @RequestBody ProductCreateRequestDto request) {
        return addProductService.addProduct(request);
    }
}
