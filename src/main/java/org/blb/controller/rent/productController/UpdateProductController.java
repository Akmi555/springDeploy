package org.blb.controller.rent.productController;


import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.controller.api.rent.product.UpdateProductControllerApi;
import org.blb.service.rent.productServise.UpdateProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/rent")
public class UpdateProductController implements UpdateProductControllerApi {

    private final UpdateProductService updateProductService;

    @Override
    @PutMapping("{id}")
    public ResponseEntity<OneMessageDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductCreateRequestDto productCreateRequestDto) {
        return updateProductService.updateProduct(id, productCreateRequestDto);
    }
}
