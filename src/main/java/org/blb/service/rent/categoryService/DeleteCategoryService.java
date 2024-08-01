package org.blb.service.rent.categoryService;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.user.User;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DeleteCategoryService {

    private final CategoryRepository categoryRepository;
    private final UserFindService userFindService;


    public ResponseEntity<?> deleteCategory(Long id) {
        User user = userFindService.getUserFromContext();

        if (user.getRole().getRole().equals("ADMIN")) {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                return new ResponseEntity<>(new OneMessageDTO("Category was successfully deleted"), HttpStatus.NO_CONTENT);
            } else {
                throw new NotFoundException("Category not found with id: " + id);
            }
        } else {
            // Пользователь не имеет прав
            throw new AccessDeniedException("You do not have permission to delete a category.");
        }
    }
}
