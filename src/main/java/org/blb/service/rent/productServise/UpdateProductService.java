package org.blb.service.rent.productServise;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.region.RegionDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UpdateProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UserFindService userFindService;
    private final FindRegionService findRegionService;

    public ResponseEntity<OneMessageDTO> updateProduct(Long productId, ProductCreateRequestDto productCreateRequestDto) {
        Optional<Product> existingProductOpt = productRepository.findById(productId);

        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            User currentUser = userFindService.getUserFromContext();

            // Checking that the current user is the owner of the product
            if (!existingProduct.getUser().getId().equals(currentUser.getId())) {
                return new ResponseEntity<>(new OneMessageDTO("You do not have the rights to update this product."), HttpStatus.CONFLICT); // The user does not have permissions to update this product
            }

            if (productCreateRequestDto.getName() != null) {
                existingProduct.setName(productCreateRequestDto.getName());
            }
            if (productCreateRequestDto.getCategory() != null) {
                Optional<Category> categoryOpt = categoryRepository.findByName(productCreateRequestDto.getCategory().getName());
                if (categoryOpt.isPresent()) {
                    existingProduct.setCategory(categoryOpt.get());
                } else {
                    return new ResponseEntity<>(new OneMessageDTO("Category with name " + productCreateRequestDto.getCategory().getName() + " not found."), HttpStatus.NOT_FOUND);
                }
            }

            if (productCreateRequestDto.getPrice() != null) {
                existingProduct.setPrice(productCreateRequestDto.getPrice());
            }
            if (productCreateRequestDto.getIsInStock() != null) {
                existingProduct.setIsInStock(productCreateRequestDto.getIsInStock());
            }
            if (productCreateRequestDto.getDescription() != null) {
                existingProduct.setDescription(productCreateRequestDto.getDescription());
            }
            if (productCreateRequestDto.getRegion() != null) {
                RegionDTO regionDTO = findRegionService.findRegionByName(productCreateRequestDto.getRegion().getRegionName());
                if (regionDTO != null) {
                    Region region = new Region();
                    region.setRegionName(regionDTO.getRegionName());
                    existingProduct.setRegion(region);
                } else {
                    return new ResponseEntity<>(new OneMessageDTO("Region with name " + productCreateRequestDto.getRegion().getRegionName() + " not found."), HttpStatus.NOT_FOUND);
                }
            }

            productRepository.save(existingProduct);

            return new ResponseEntity<>(new OneMessageDTO("Your product has been successfully updated."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OneMessageDTO("Product with name " + productId + " not found."), HttpStatus.NOT_FOUND);
        }
    }


}
