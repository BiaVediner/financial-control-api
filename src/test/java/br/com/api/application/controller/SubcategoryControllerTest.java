package br.com.api.application.controller;

import br.com.api.application.dto.SubcategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.impl.SubcategoryServiceImpl;
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
class SubcategoryControllerTest {

    @InjectMocks
    private SubcategoryController subcategoryController;

    @Mock
    private SubcategoryServiceImpl subcategoryService;

    @Test
    void testCreateWhenCategoryDoesNotExistsThenThrowNotFoundException() throws BadRequestException, NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doThrow(NotFoundException.class).when(subcategoryService).createSubcategory(subcategoryDTO);

        assertThrows(NotFoundException.class, () -> subcategoryController.createSubcategory(subcategoryDTO));
    }

    @Test
    void testCreateWhenSubcategoryNameAlreadyExistsThenThrowBadRequestException() throws BadRequestException, NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doThrow(BadRequestException.class).when(subcategoryService).createSubcategory(subcategoryDTO);

        assertThrows(BadRequestException.class, () -> subcategoryController.createSubcategory(subcategoryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(subcategoryService.createSubcategory(subcategoryDTO)).thenReturn(1L);

        ResponseEntity<SubcategoryDTO> response = subcategoryController.createSubcategory(subcategoryDTO);

        assertNotNull(response.getBody().getId());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    void testUpdateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() throws BadRequestException, NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doThrow(NotFoundException.class).when(subcategoryService).updateSubcategory(subcategoryDTO, 1L);

        assertThrows(NotFoundException.class, () -> subcategoryController.updateSubcategory(1L, subcategoryDTO));
    }

    @Test
    void testUpdateWhenCategoryDoesNotExistsThenThrowNotFoundException() throws BadRequestException, NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doThrow(NotFoundException.class).when(subcategoryService).updateSubcategory(subcategoryDTO, 1L);

        assertThrows(NotFoundException.class, () -> subcategoryController.updateSubcategory(1L, subcategoryDTO));
    }

    @Test
    void testUpdateWhenSubcategoryNameAlreadyExistsThenThrowBadRequestException() throws BadRequestException, NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doThrow(BadRequestException.class).when(subcategoryService).updateSubcategory(subcategoryDTO, 1L);

        assertThrows(BadRequestException.class, () -> subcategoryController.updateSubcategory(1L, subcategoryDTO));
    }

    @Test
    void testUpdateWhenRequestIsValidThenUpdateSuccessfully() throws NotFoundException, BadRequestException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        doNothing().when(subcategoryService).updateSubcategory(subcategoryDTO, 1L);

        ResponseEntity<Void> response = subcategoryController.updateSubcategory(1L, subcategoryDTO);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testDeleteWhenSubcategoryNotFoundThenThrowNotFoundException() throws BadRequestException, NotFoundException {
        doThrow(NotFoundException.class).when(subcategoryService).deleteSubcategory(1L);

        assertThrows(NotFoundException.class, () -> subcategoryController.deleteSubcategory(1L));
    }

    @Test
    void testDeleteWhenSubcategoryIsNotDeletableThenThrowBadRequestException() throws BadRequestException, NotFoundException {
        doThrow(BadRequestException.class).when(subcategoryService).deleteSubcategory(1L);

        assertThrows(BadRequestException.class, () -> subcategoryController.deleteSubcategory(1L));
    }

    @Test
    void testDeleteWhenCategoryIsDeletableThenDeleteSuccessfully() throws NotFoundException, BadRequestException {
        doNothing().when(subcategoryService).deleteSubcategory(1L);

        ResponseEntity<Void> response = subcategoryController.deleteSubcategory(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetByIdWhenSubcategoryDoesNotExistsThenThrowNotFoundException() throws NotFoundException {
        doThrow(NotFoundException.class).when(subcategoryService).getSubcategoryById(1L);

        assertThrows(NotFoundException.class, () -> subcategoryController.getSubcategoryById(1L));
    }

    @Test
    void testGetByIdWhenSubcategoryExistsThenReturnCategoryDTO() throws NotFoundException {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(subcategoryService.getSubcategoryById(1L)).thenReturn(subcategoryDTO);

        ResponseEntity<SubcategoryDTO> response = subcategoryController.getSubcategoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetWhenThereIsNoSubcategoriesThenReturnEmptyList() {
        when(subcategoryService.getSubcategories(any(), any())).thenReturn(emptyList());

        ResponseEntity<List<SubcategoryDTO>> response = subcategoryController.getSubcategories("name", 1L);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void testGetWhenSubcategoryExistsThenReturnCategoryDTO() {
        SubcategoryDTO subcategoryDTO = mock(SubcategoryDTO.class);

        when(subcategoryService.getSubcategories(any(), any())).thenReturn(List.of(subcategoryDTO));

        ResponseEntity<List<SubcategoryDTO>> response = subcategoryController.getSubcategories("name", 1L);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}