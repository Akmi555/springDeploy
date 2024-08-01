package org.blb.controller.rent.categoryController;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.blb.DTO.rent.categoryDto.CategoryCreateRequestDto;
import org.blb.DTO.rent.categoryDto.CategoryResponseDto;
import org.blb.controller.api.rent.category.AddCategoryControllerApi;
import org.blb.service.rent.categoryService.AddCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/admin/rent/categories")
@AllArgsConstructor
public class AddCategoryController implements AddCategoryControllerApi {

  private final AddCategoryService addCategoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryCreateRequestDto requestDto) {
        return addCategoryService.addCategory(requestDto.getName());
    }

}
