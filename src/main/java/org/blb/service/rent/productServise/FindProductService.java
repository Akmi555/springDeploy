package org.blb.service.rent.productServise;

import lombok.AllArgsConstructor;
import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.errorDto.FieldErrorDto;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.repository.region.RegionRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.rent.categoryService.FindCategoryService;
import org.blb.service.util.rentMapping.ProductConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FindProductService {

    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final FindRegionService findRegionService;
    private final FindCategoryService findCategoryService;
    private final int defaultPageSize = 10;

    public ProductSearchResponse findProducts(String regionName, String categoryName, String productName, int page) {
        List<FieldErrorDto> fieldErrors = new ArrayList<>();

        // Search for region and category, if specified
        Optional<Region> regionOpt = Optional.ofNullable(regionName)
                .filter(name -> !name.isEmpty())
                .flatMap(findRegionService::findRegionByNameOptional);
        Optional<Category> categoryOpt = Optional.ofNullable(categoryName)
                .filter(catName -> !catName.isEmpty())
                .flatMap(findCategoryService::findByName);

        // Checking for errors only for the specified parameters
        if (regionName != null && !regionName.isEmpty()) {
            validateRegion(regionOpt, fieldErrors);
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            validateCategory(categoryOpt, fieldErrors);
        }

        // If there are errors, return a response with an empty list of products
        if (!fieldErrors.isEmpty()) {
            return new ProductSearchResponse(new ArrayList<>(),
                    new ErrorResponseDto("Errors occurred", fieldErrors), 0, 0);
        }

        // Setting pagination
        Pageable pageable = PageRequest.of(page, defaultPageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = findProductsByCriteria(regionOpt, categoryOpt, productName, pageable);

        List<ProductResponseDto> dtos = productPage.getContent().stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());

        // Если список продуктов пуст, добавить сообщение об ошибке
        if (dtos.isEmpty()) {
            fieldErrors.add(new FieldErrorDto("products", "No products found for the given criteria"));
        }


        // Create a ProductSearchResponse object, including totalElements and totalPages
        return new ProductSearchResponse(
                dtos,
                fieldErrors.isEmpty() ? null : new ErrorResponseDto("Errors occurred", fieldErrors),
                page,
                productPage.getTotalPages()
        );
    }

    private void validateRegion(Optional<Region> regionOpt, List<FieldErrorDto> fieldErrors) {
        if (regionOpt.isEmpty()) {
            fieldErrors.add(new FieldErrorDto("region", "Region not found"));
        }
    }

    private void validateCategory(Optional<Category> categoryOpt, List<FieldErrorDto> fieldErrors) {
        if (categoryOpt.isEmpty()) {
            fieldErrors.add(new FieldErrorDto("category", "Category not found"));
        }
    }

    private Page<Product> findProductsByCriteria(Optional<Region> regionOpt, Optional<Category> categoryOpt, String productName, Pageable pageable) {
        // Here we implement the logic of search depending on the presence of parameters
        if (regionOpt.isPresent() && categoryOpt.isPresent() && productName != null) {
            return productRepository.findAllByRegionAndCategoryAndNameStartingWithIgnoreCase(regionOpt.get(), categoryOpt.get(), productName, pageable);
        } else if (regionOpt.isPresent() && categoryOpt.isPresent()) {
            return productRepository.findAllByRegionAndCategory(regionOpt.get(), categoryOpt.get(), pageable);
        } else if (regionOpt.isPresent() && productName != null) {
            return productRepository.findAllByRegionAndNameStartingWithIgnoreCase(regionOpt.get(), productName, pageable);
        } else if (categoryOpt.isPresent() && productName != null) {
            return productRepository.findAllByCategoryAndNameStartingWithIgnoreCase(categoryOpt.get(), productName, pageable);
        } else if (regionOpt.isPresent()) {
            return productRepository.findAllByRegion(regionOpt.get(), pageable);
        } else if (categoryOpt.isPresent()) {
            return productRepository.findAllByCategory(categoryOpt.get(), pageable);
        } else if (productName != null) {
            return productRepository.findByNameStartingWithIgnoreCase(productName, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public ProductResponseDto findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));
        return productConverter.toDto(product);
    }
}
