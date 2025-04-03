package br.com.api.application.controller;

import br.com.api.application.dto.CategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryServiceImpl categoryService;

    @Test
    void testCreateWhenCategoryNameAlreadyExistsThenThrowBadRequestException() throws BadRequestException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        doThrow(BadRequestException.class).when(categoryService).createCategory(any());

        assertThrows(BadRequestException.class, () -> categoryController.createCategory(categoryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws BadRequestException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        when(categoryService.createCategory(categoryDTO)).thenReturn(1L);

        ResponseEntity<CategoryDTO> response = categoryController.createCategory(categoryDTO);

        assertNotNull(response.getBody().getId());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    void testUpdateWhenCategoryIdDoesNotExistsThenThrowNotFoundException() throws BadRequestException, NotFoundException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        doThrow(NotFoundException.class).when(categoryService).updateCategory(categoryDTO, 1L);

        assertThrows(NotFoundException.class, () -> categoryController.updateCategory(1L, categoryDTO));
    }

    @Test
    void testUpdateWhenCategoryNameAlreadyExistsThenThrowBadRequestException() throws BadRequestException, NotFoundException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        doThrow(BadRequestException.class).when(categoryService).updateCategory(categoryDTO, 1L);

        assertThrows(BadRequestException.class, () -> categoryController.updateCategory(1L, categoryDTO));
    }

    @Test
    void testUpdateCategoryWhenRequestIsValidThenUpdateSuccessfully() throws BadRequestException, NotFoundException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        doNothing().when(categoryService).updateCategory(categoryDTO, 1L);

        ResponseEntity<Void> response = categoryController.updateCategory(1L, categoryDTO);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testDeleteWhenCategoryNotFoundThenThrowNotFoundException() throws NotFoundException, BadRequestException {
        doThrow(NotFoundException.class).when(categoryService).deleteCategory(1L);

        assertThrows(NotFoundException.class, () -> categoryController.deleteCategory(1L));
    }

    @Test
    void testDeleteWhenSubcategoryIsNotDeletableThenThrowBadRequestException() throws NotFoundException, BadRequestException {
        doThrow(BadRequestException.class).when(categoryService).deleteCategory(1L);

        assertThrows(BadRequestException.class, () -> categoryController.deleteCategory(1L));
    }

    @Test
    void testDeleteWhenCategoryIsDeletableThenDeleteSuccessfully() throws NotFoundException, BadRequestException {
        doNothing().when(categoryService).deleteCategory(1L);

        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetByIdWhenCategoryDoesNotExistsThenThrowNotFoundException() throws NotFoundException {
        doThrow(NotFoundException.class).when(categoryService).getCategoryById(1L);

        assertThrows(NotFoundException.class, () -> categoryController.getCategoryById(1L));
    }

    @Test
    void testGetByIdWhenCategoryExistsThenReturnCategoryDTO() throws NotFoundException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        when(categoryService.getCategoryById(1L)).thenReturn(categoryDTO);

        ResponseEntity<CategoryDTO> response = categoryController.getCategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetWhenThereIsNoCategoriesThenReturnEmptyList() {
        when(categoryService.getCategories(any())).thenReturn(emptyList());

        ResponseEntity<List<CategoryDTO>> response = categoryController.getCategories("name");

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void testGetWhenCategoryExistsThenReturnCategoryDTO() {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        when(categoryService.getCategories(any())).thenReturn(List.of(categoryDTO));

        ResponseEntity<List<CategoryDTO>> response = categoryController.getCategories("name");

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}