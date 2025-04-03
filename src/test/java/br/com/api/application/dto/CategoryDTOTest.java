package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.CategoryEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryDTOTest {
    @Test
    void testDto() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Teste");
        categoryDTO.setSubcategories(List.of(new SubcategoryDTO()));

        assertEquals(1L, categoryDTO.getId());
        assertEquals("Teste", categoryDTO.getName());
    }

    @Test
    void testToEntity() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);
        categoryDTO.setName("Teste");

        CategoryEntity categoryEntity = categoryDTO.toEntity();

        assertEquals(categoryDTO.getName(), categoryEntity.getName());
    }

    @Test
    void testDtoWithInvalidName() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(1L);

        assertThrows(BadRequestException.class, categoryDTO::validate);
    }
}