package br.com.api.domain.services.impl;

import br.com.api.application.dto.CategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import br.com.api.resources.repositories.CategoryRepository;
import br.com.api.resources.repositories.SubcategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Test
    void testCreateWhenCategoryNameAlreadyExistsThenThrowBadRequestException() {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        when(categoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.createCategory(categoryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws BadRequestException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.existsByName(any())).thenReturn(false);
        when(categoryRepository.save(any())).thenReturn(categoryEntity);

        Long categoryId = categoryService.createCategory(categoryDTO);

        assertEquals(categoryEntity.getId(), categoryId);
    }

    @Test
    void testUpdateWhenCategoryIdDoesNotExistsThenThrowNotFoundException() {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(categoryDTO, 1L));
    }

    @Test
    void testUpdateWhenCategoryNameAlreadyExistsThenThrowBadRequestException() {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(categoryEntity));
        when(categoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.updateCategory(categoryDTO, categoryEntity.getId()));
    }

    @Test
    void testUpdateCategoryWhenRequestIsValidThenUpdateSuccessfully() throws BadRequestException, NotFoundException {
        CategoryDTO categoryDTO = mock(CategoryDTO.class);
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(categoryEntity));
        when(categoryRepository.existsByName(any())).thenReturn(false);

        categoryService.updateCategory(categoryDTO, categoryEntity.getId());

        verify(categoryRepository).save(categoryEntity);
    }

    @Test
    void testDeleteWhenCategoryNotFoundThenThrowNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void testDeleteWhenSubcategoryIsNotDeletableThenThrowBadRequestException() {
        CategoryEntity categoryEntity = mock(CategoryEntity.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryEntity.getSubcategories()).thenReturn(List.of(subcategoryEntity));
        when(subcategoryEntity.isDeletable()).thenReturn(false);

        assertThrows(BadRequestException.class, () -> categoryService.deleteCategory(1L));
    }

    @Test
    void testDeleteWhenCategoryIsDeletableThenDeleteSuccessfully() throws NotFoundException, BadRequestException {
        CategoryEntity categoryEntity = mock(CategoryEntity.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryEntity.getSubcategories()).thenReturn(List.of(subcategoryEntity));
        when(subcategoryEntity.isDeletable()).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(subcategoryRepository).delete(subcategoryEntity);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void testGetByIdWhenCategoryDoesNotExistsThenThrowNotFoundException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    @Test
    void testGetByIdWhenCategoryExistsThenReturnCategoryDTO() throws NotFoundException {
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryEntity.toDto(any())).thenReturn(mock(CategoryDTO.class));

        CategoryDTO categoryDTO = categoryService.getCategoryById(1L);

        assertNotNull(categoryDTO);
    }

    @Test
    void testGetWhenCategoriesExistsThenReturnCategoryDTO() {
        CategoryEntity categoryEntity = mock(CategoryEntity.class);

        when(categoryRepository.findAll(any(Specification.class))).thenReturn(List.of(categoryEntity));

        List<CategoryDTO> categoryDTO = categoryService.getCategories(null);

        assertNotNull(categoryDTO);
        assertEquals(1, categoryDTO.size());
    }

    @Test
    void testGetWhenCategoriesDoesNotExistsThenReturnEmptyList() {
        when(categoryRepository.findAll(any(Specification.class))).thenReturn(emptyList());

        List<CategoryDTO> categoryDTO = categoryService.getCategories(null);

        assertNotNull(categoryDTO);
        assertTrue(categoryDTO.isEmpty());
    }
}