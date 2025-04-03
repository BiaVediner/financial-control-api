package br.com.api.domain.services.impl;

import br.com.api.application.dto.SubcategoryDTO;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubcategoryServiceImplTest {
    @InjectMocks
    private SubcategoryServiceImpl subcategoryService;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testCreateWhenCategoryDoesNotExistsThenThrowNotFoundException() {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subcategoryService.createSubcategory(subcategoryDTO));
    }

    @Test
    void testCreateWhenSubcategoryNameAlreadyExistsThenThrowBadRequestException() {
        CategoryEntity category = mock(CategoryEntity.class);
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(subcategoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> subcategoryService.createSubcategory(subcategoryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);
        CategoryEntity category = mock(CategoryEntity.class);

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(subcategoryRepository.existsByName(any())).thenReturn(false);
        when(subcategoryRepository.save(any())).thenReturn(subcategoryEntity);

        Long subcategoryId = subcategoryService.createSubcategory(subcategoryDTO);

        assertEquals(subcategoryDTO.getId(), subcategoryId);
    }

    @Test
    void testUpdateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subcategoryService.updateSubcategory(subcategoryDTO, 1L));
    }

    @Test
    void testUpdateWhenCategoryDoesNotExistsThenThrowNotFoundException() {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.of(subcategoryEntity));
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subcategoryService.updateSubcategory(subcategoryDTO, 1L));
    }

    @Test
    void testUpdateWhenSubcategoryNameAlreadyExistsThenThrowBadRequestException() {
        CategoryEntity category = mock(CategoryEntity.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.of(subcategoryEntity));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(subcategoryRepository.existsByName(any())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> subcategoryService.updateSubcategory(subcategoryDTO, 1L));
    }

    @Test
    void testUpdateWhenRequestIsValidThenUpdateSuccessfully() throws NotFoundException, BadRequestException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);
        CategoryEntity category = mock(CategoryEntity.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.of(subcategoryEntity));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(subcategoryRepository.existsByName(any())).thenReturn(false);

        subcategoryService.updateSubcategory(subcategoryDTO, subcategoryEntity.getId());

        verify(subcategoryRepository).save(subcategoryEntity);
    }

    @Test
    void testDeleteWhenSubcategoryNotFoundThenThrowNotFoundException() {
        when(subcategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subcategoryService.deleteSubcategory(1L));
    }

    @Test
    void testDeleteWhenSubcategoryIsNotDeletableThenThrowBadRequestException() {
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(subcategoryRepository.findById(1L)).thenReturn(Optional.of(subcategoryEntity));
        when(subcategoryEntity.isDeletable()).thenReturn(false);

        assertThrows(BadRequestException.class, () -> subcategoryService.deleteSubcategory(1L));
    }

    @Test
    void testDeleteWhenCategoryIsDeletableThenDeleteSuccessfully() throws NotFoundException, BadRequestException {
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(subcategoryRepository.findById(1L)).thenReturn(Optional.of(subcategoryEntity));
        when(subcategoryEntity.isDeletable()).thenReturn(true);

        subcategoryService.deleteSubcategory(1L);

        verify(subcategoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetByIdWhenSubcategoryDoesNotExistsThenThrowNotFoundException() {
        when(subcategoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> subcategoryService.getSubcategoryById(1L));
    }

    @Test
    void testGetByIdWhenSubcategoryExistsThenReturnCategoryDTO() throws NotFoundException {
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(subcategoryRepository.findById(1L)).thenReturn(Optional.of(subcategoryEntity));
        when(subcategoryEntity.toDto()).thenReturn(mock(SubcategoryDTO.class));

        SubcategoryDTO subcategory = subcategoryService.getSubcategoryById(1L);

        assertNotNull(subcategory);
    }

    @Test
    void testGetWhenSubcategoriesExistsThenReturnCategoryDTO() {
        SubcategoryEntity subcategoryEntity = mock(SubcategoryEntity.class);

        when(subcategoryRepository.findAll(any(Specification.class))).thenReturn(List.of(subcategoryEntity));

        List<SubcategoryDTO> subcategoryDTOS = subcategoryService.getSubcategories(null, null);

        assertNotNull(subcategoryDTOS);
        assertEquals(1, subcategoryDTOS.size());
    }

    @Test
    void testGetWhenSubcategoriesDoesNotExistsThenReturnEmptyList() {
        when(subcategoryRepository.findAll(any(Specification.class))).thenReturn(emptyList());

        List<SubcategoryDTO> subcategoryDTOS = subcategoryService.getSubcategories(null, null);

        assertNotNull(subcategoryDTOS);
        assertTrue(subcategoryDTOS.isEmpty());
    }
}