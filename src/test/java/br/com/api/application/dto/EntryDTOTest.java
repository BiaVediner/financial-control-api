package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.EntryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntryDTOTest {
    @Test
    void testDto() {
        EntryDTO entryDTO = new EntryDTO();
        entryDTO.setId(1L);
        entryDTO.setValue(BigDecimal.ONE);
        entryDTO.setDate(LocalDate.now());
        entryDTO.setSubcategoryId(1L);
        entryDTO.setCommentary("Tests for DTO");

        assertEquals(1L, entryDTO.getId());
        assertEquals(BigDecimal.ONE, entryDTO.getValue());
        assertEquals(LocalDate.now(), entryDTO.getDate());
        assertEquals(1L, entryDTO.getSubcategoryId());
        assertEquals("Tests for DTO", entryDTO.getCommentary());
    }

    @Test
    void testToEntity() {
        SubcategoryEntity subcategoryEntity = new SubcategoryEntity();
        subcategoryEntity.setId(2L);

        EntryDTO entryDTO = new EntryDTO();
        entryDTO.setId(1L);
        entryDTO.setValue(BigDecimal.ONE);
        entryDTO.setDate(LocalDate.now());
        entryDTO.setSubcategoryId(subcategoryEntity.getId());
        entryDTO.setCommentary("Tests for DTO");

        EntryEntity entry = entryDTO.toEntity(subcategoryEntity);

        assertEquals(entryDTO.getValue(), entry.getValue());
        assertEquals(entryDTO.getDate(), entry.getDate());
        assertEquals(entryDTO.getSubcategoryId(), entry.getSubcategory().getId());
        assertEquals(entryDTO.getCommentary(), entry.getCommentary());
    }

    @Test
    void testDtoWithInvalidName() {
        EntryDTO entryDTO = new EntryDTO();
        entryDTO.setId(1L);
        entryDTO.setDate(LocalDate.now());
        entryDTO.setSubcategoryId(1L);

        assertThrows(BadRequestException.class, entryDTO::validate);
    }

    @Test
    void testDtoWithInvalidCategoryId() {
        EntryDTO entryDTO = new EntryDTO();
        entryDTO.setId(1L);
        entryDTO.setValue(BigDecimal.ONE);
        entryDTO.setDate(LocalDate.now());
        entryDTO.setCommentary("Tests for DTO");

        assertThrows(BadRequestException.class, entryDTO::validate);
    }
}