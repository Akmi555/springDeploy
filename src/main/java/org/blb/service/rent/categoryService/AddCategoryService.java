package org.blb.service.rent.categoryService;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.DTO.rent.categoryDto.CategoryResponseDto;
import org.blb.exeption.AlreadyExistException;
import org.blb.models.rent.Category;
import org.blb.models.user.User;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class AddCategoryService {

    private final CategoryRepository categoryRepository;
    private final UserFindService userFindService;
    private static final Logger logger = Logger.getLogger(AddCategoryService.class.getName());


    public ResponseEntity<?> addCategory(String categoryName) {
        User user = userFindService.getUserFromContext();
        logger.info("User role: " + user.getRole());

        if (user.getRole().getRole().equals("ADMIN")) {
            Optional<Category> existingCategory = categoryRepository.findByName(categoryName);

            if (existingCategory.isPresent()) {
                throw new AlreadyExistException("Category with name " + categoryName + " already exists.");
            }

            Category newCategory = new Category();
            newCategory.setName(categoryName);
            Category savedCategory = categoryRepository.save(newCategory);

            CategoryResponseDto responseDto = new CategoryResponseDto(savedCategory.getName());
            return new ResponseEntity<>(new OneMessageDTO("New category with name" + savedCategory.getName() + "successfully created"), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(new OneMessageDTO("You do not have the rights to add a category."), HttpStatus.FORBIDDEN);
        }
    }
}
