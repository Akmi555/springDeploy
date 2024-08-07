package org.blb.service.rent.categoryService;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserFindService userFindService;

    @InjectMocks
    private DeleteCategoryService deleteCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteCategory_success() {
        Long categoryId = 1L;
        User adminUser = createAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(adminUser);
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        ResponseEntity<?> response = deleteCategoryService.deleteCategory(categoryId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Category was successfully deleted", ((OneMessageDTO) response.getBody()).getMessage());
        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_notFound() {
        Long categoryId = 1L;
        User adminUser = createAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(adminUser);
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> deleteCategoryService.deleteCategory(categoryId));
        assertEquals("Category not found with id: 1", exception.getMessage());
        verify(categoryRepository, times(1)).existsById(categoryId);
        verify(categoryRepository, never()).deleteById(categoryId);
    }

    @Test
    void testDeleteCategory_insufficientPermissions() {
        Long categoryId = 1L;
        User nonAdminUser = createNonAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(nonAdminUser);

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> deleteCategoryService.deleteCategory(categoryId));
        assertEquals("You do not have permission to delete a category.", exception.getMessage());
        verify(categoryRepository, never()).existsById(anyLong());
        verify(categoryRepository, never()).deleteById(anyLong());
    }

    private User createAdminUser() {
        User user = new User();
        Role role = new Role();
        role.setRole("ADMIN");
        user.setRole(role);
        return user;
    }

    private User createNonAdminUser() {
        User user = new User();
        Role role = new Role();
        role.setRole("USER");
        user.setRole(role);
        return user;
    }
}