package org.blb.service.rent.productServise;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.region.RegionJustWithNameDto;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.productDto.ProductCreateRequestDto;
import org.blb.DTO.validationErrorDto.ValidationErrorDto;
import org.blb.DTO.validationErrorDto.ValidationErrorsDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
import org.blb.service.util.rentMapping.ProductConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AddProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductConverter productConverter;
    private final UserFindService userFindService;
    private final RegionRepository regionRepository;
    private final SupabaseService supabaseService;

    public ResponseEntity<?> addProduct(ProductCreateRequestDto requestDto, MultipartFile image) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        // Validate and fetch the Category and Region entity
        Category category = validateAndFetchCategory(requestDto.getCategory(), errors);
        Region region = validateAndFetchRegion(requestDto.getRegion(), errors);

        // If there are any validation errors, return them
        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new ValidationErrorsDto(errors), HttpStatus.BAD_REQUEST);
        }

        // Proceed with product creation
        try {
            Product productForAdd = productConverter.fromDto(requestDto);

            productForAdd.setIsInStock(requestDto.getIsInStock() != null ? requestDto.getIsInStock() : true);

            User user = userFindService.getUserFromContext();
            productForAdd.setUser(user);

            // Set fetched category and region
            productForAdd.setCategory(category);
            productForAdd.setRegion(region);
            productForAdd.setDescription(requestDto.getDescription());

            String imageUrl = supabaseService.uploadImage(image);
            productForAdd.setLink(imageUrl);

            productRepository.save(productForAdd);
            return new ResponseEntity<>(new OneMessageDTO("Product successfully created"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new OneMessageDTO("An unexpected error occurred. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Category validateAndFetchCategory(CategoryCreateRequestDto categoryDto, List<ValidationErrorDto> errors) {
        if (categoryDto == null) {
            errors.add(new ValidationErrorDto("category", "Category not found"));
            return null;
        }

        return categoryRepository.findByName(categoryDto.getName())
                .orElseGet(() -> {
                    errors.add(new ValidationErrorDto("category", "Category with name " + categoryDto.getName() + " not found."));
                    return null; // Return null if category is not found
                });
    }

    private Region validateAndFetchRegion(RegionJustWithNameDto regionDto, List<ValidationErrorDto> errors) {
        if (regionDto == null || regionDto.getRegionName() == null) {
            errors.add(new ValidationErrorDto("region", "Region not found"));
            return null;
        }

        return regionRepository.findByRegionName(regionDto.getRegionName())
                .orElseGet(() -> {
                    errors.add(new ValidationErrorDto("region", "Region with name " + regionDto.getRegionName() + " not found."));
                    return null; // Return null if region is not found
                });
    }

    @Transactional
    public ResponseEntity<?> addProductWithoutImage(ProductCreateRequestDto requestDto) {
        List<ValidationErrorDto> errors = new ArrayList<>();

        // Validate and fetch the Category and Region entity
        Category category = validateAndFetchCategory(requestDto.getCategory(), errors);
        Region region = validateAndFetchRegion(requestDto.getRegion(), errors);

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(new ValidationErrorsDto(errors), HttpStatus.BAD_REQUEST);
        }

        try {
            Product productForAdd = productConverter.fromDto(requestDto);

            productForAdd.setIsInStock(requestDto.getIsInStock() != null ? requestDto.getIsInStock() : true);
            User user = userFindService.getUserFromContext();
            productForAdd.setUser(user);
            productForAdd.setCategory(category);
            productForAdd.setRegion(region);
            productForAdd.setDescription(requestDto.getDescription());

            productRepository.save(productForAdd);
            return new ResponseEntity<>(new OneMessageDTO("Product successfully created"), HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new OneMessageDTO("An unexpected error occurred. Please try again later."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
