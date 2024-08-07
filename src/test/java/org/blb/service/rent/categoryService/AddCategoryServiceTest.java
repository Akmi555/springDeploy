package org.blb.service.rent.categoryService;

import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.exeption.AlreadyExistException;
import org.blb.models.rent.Category;
import org.blb.models.user.User;
import org.blb.models.user.Role;
import org.blb.repository.rent.CategoryRepository;
import org.blb.service.user.UserFindService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AddCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserFindService userFindService;

    @InjectMocks
    private AddCategoryService addCategoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory_success() {
        String categoryName = "NewCategory";
        User adminUser = createAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(adminUser);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

        Category savedCategory = new Category();
        savedCategory.setName(categoryName);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        ResponseEntity<?> response = addCategoryService.addCategory(categoryName);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New category with nameNewCategorysuccessfully created", ((OneMessageDTO) response.getBody()).getMessage());
        verify(categoryRepository, times(1)).findByName(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testAddCategory_alreadyExists() {
        String categoryName = "ExistingCategory";
        User adminUser = createAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(adminUser);

        Category existingCategory = new Category();
        existingCategory.setName(categoryName);
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(existingCategory));

        AlreadyExistException exception = assertThrows(AlreadyExistException.class, () -> addCategoryService.addCategory(categoryName));
        assertEquals("Category with name ExistingCategory already exists.", exception.getMessage());
        verify(categoryRepository, times(1)).findByName(categoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testAddCategory_insufficientPermissions() {
        String categoryName = "NewCategory";
        User nonAdminUser = createNonAdminUser();
        when(userFindService.getUserFromContext()).thenReturn(nonAdminUser);

        ResponseEntity<?> response = addCategoryService.addCategory(categoryName);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("You do not have the rights to add a category.", ((OneMessageDTO) response.getBody()).getMessage());
        verify(categoryRepository, never()).findByName(anyString());
        verify(categoryRepository, never()).save(any(Category.class));
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