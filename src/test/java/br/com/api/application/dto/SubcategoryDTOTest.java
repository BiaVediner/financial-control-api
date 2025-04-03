package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubcategoryDTOTest {
    @Test
    void testDto() {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(1L);
        subcategoryDTO.setName("Teste");
        subcategoryDTO.setCategoryId(2L);

        assertEquals(1L, subcategoryDTO.getId());
        assertEquals("Teste", subcategoryDTO.getName());
        assertEquals(2L, subcategoryDTO.getCategoryId());
    }

    @Test
    void testToEntity() {
        CategoryEntity category = new CategoryEntity();
        category.setId(2L);

        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(1L);
        subcategoryDTO.setName("Teste");
        subcategoryDTO.setCategoryId(category.getId());

        SubcategoryEntity subcategoryEntity = subcategoryDTO.toEntity(category);

        assertEquals(subcategoryDTO.getName(), subcategoryEntity.getName());
        assertEquals(subcategoryDTO.getCategoryId(), subcategoryEntity.getCategory().getId());
    }

    @Test
    void testDtoWithInvalidName() {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(1L);

        assertThrows(BadRequestException.class, subcategoryDTO::validate);
    }

    @Test
    void testDtoWithInvalidCategoryId() {
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setId(1L);
        subcategoryDTO.setName("Teste");

        assertThrows(BadRequestException.class, subcategoryDTO::validate);
    }
}