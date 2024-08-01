package org.blb.service.util.rentMapping;


import lombok.AllArgsConstructor;
import org.blb.DTO.region.RegionDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.DTO.user.UserJustWithNameDto;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.user.UserFindService;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductConverter {

    public final CategoryRepository categoryRepository;
    public final RegionRepository regionRepository;
    public final UserFindService userFindService;


    public Product fromDto(ProductCreateRequestDto dto){
        Product product = new Product();

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setIsInStock(dto.getIsInStock() != null ? dto.getIsInStock() : true);
        product.setDescription(dto.getDescription());

        if (dto.getCategory() != null) {
            product.setCategory(categoryRepository.findByName(dto.getCategory().getName()).orElse(null));
        }

        if (dto.getRegion() != null) {
            product.setRegion(regionRepository.findByRegionName(dto.getRegion().getRegionName()).orElse(null));
        }

        User user = userFindService.getUserFromContext();
        product.setUser(user);

        return product;
    }

    public ProductResponseDto toDto(Product product){
        ProductResponseDto productResponseDto = new ProductResponseDto();
        UserJustWithNameDto userDto = new UserJustWithNameDto(product.getUser().getName());

        if(product.getName() != null){
            productResponseDto.setName(product.getName());
        }
        if (product.getCategory() != null) {
            CategoryCreateRequestDto categoryDTO = new CategoryCreateRequestDto(product.getCategory().getName());
            productResponseDto.setCategory(categoryDTO);
        }
        if(product.getPrice() != null){
            productResponseDto.setPrice(product.getPrice());
        }
        //if(product.getIsInStock() != null){
         //   productResponseDto.setIsInStock(product.getIsInStock());
        //}

        if(product.getDescription() != null){
            productResponseDto.setDescription(product.getDescription());
        }

        if (product.getRegion() != null) {
            RegionJustWithNameDto regionDto = new RegionJustWithNameDto(product.getRegion().getRegionName());
           // RegionDTO regionDto = new RegionDTO(product.getRegion().getRegionName());
            productResponseDto.setRegion(regionDto);
        }
        if(product.getUser() !=null){
            productResponseDto.setOwner(userDto);
        }

        return productResponseDto;
    }
}
