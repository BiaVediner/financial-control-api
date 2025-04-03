package br.com.api.domain.services.impl;

import br.com.api.application.dto.EntryDTO;
import br.com.api.application.dto.StatementDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.EntryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import br.com.api.resources.repositories.EntryRepository;
import br.com.api.resources.repositories.SubcategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
class EntryServiceImplTest {

    @Mock
    private EntryRepository entryRepository;

    @Mock
    private SubcategoryRepository subcategoryRepository;

    @InjectMocks
    private EntryServiceImpl entryService;

    @Test
    void testCreateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() {
        EntryDTO entryDTO = mock(EntryDTO.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> entryService.createEntry(entryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);
        SubcategoryEntity subcategory = mock(SubcategoryEntity.class);
        EntryEntity entryEntity = mock(EntryEntity.class);

        when(subcategoryRepository.findById(any())).thenReturn(Optional.of(subcategory));
        when(entryRepository.save(any())).thenReturn(entryEntity);

        Long entryId = entryService.createEntry(entryDTO);

        assertEquals(entryEntity.getId(), entryId);
    }

    @Test
    void testUpdateWhenEntryDoesNotExistsThenThrowNotFoundException() {
        EntryDTO entryDTO = mock(EntryDTO.class);

        when(entryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> entryService.updateEntry(entryDTO, 1L));
    }

    @Test
    void testUpdateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() {
        EntryDTO entryDTO = mock(EntryDTO.class);
        EntryEntity entryEntity = mock(EntryEntity.class);

        when(entryRepository.findById(any())).thenReturn(Optional.of(entryEntity));
        when(subcategoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> entryService.updateEntry(entryDTO, entryEntity.getId()));
    }

    @Test
    void testUpdateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);
        SubcategoryEntity subcategory = mock(SubcategoryEntity.class);
        EntryEntity entryEntity = mock(EntryEntity.class);

        when(entryRepository.findById(any())).thenReturn(Optional.of(entryEntity));
        when(subcategoryRepository.findById(any())).thenReturn(Optional.of(subcategory));

        entryService.updateEntry(entryDTO, entryEntity.getId());

        verify(entryRepository).save(entryEntity);
    }

    @Test
    void testDeleteWhenEntryDoesNotExistsThenThrowNotFoundException() {
        when(entryRepository.existsById(any())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> entryService.deleteEntry(1L));
    }


    @Test
    void testDeleteWhenEntryIsDeletableThenDeleteSuccessfully() throws NotFoundException {
        when(entryRepository.existsById(any())).thenReturn(true);

        entryService.deleteEntry(1L);

        verify(entryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetByIdWhenEntryDoesNotExistsThenThrowNotFoundException() {
        when(entryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> entryService.getEntryById(1L));
    }

    @Test
    void testGetByIdWhenEntryExistsThenReturnEntryDTO() throws NotFoundException {
        EntryEntity entryEntity = mock(EntryEntity.class);

        when(entryRepository.findById(any())).thenReturn(Optional.of(entryEntity));
        when(entryEntity.toDTO()).thenReturn(mock(EntryDTO.class));

        EntryDTO entryDTO = entryService.getEntryById(1L);

        assertNotNull(entryDTO);
    }

    @Test
    void testGetWhenEntriesExistsThenReturnCategoryDTO() throws BadRequestException {
        EntryEntity entry = mock(EntryEntity.class);

        when(entryRepository.findAll(any(Specification.class))).thenReturn(List.of(entry));

        List<EntryDTO> entries = entryService.getEntries(null, null, null);

        assertNotNull(entries);
        assertEquals(1, entries.size());
    }

    @Test
    void testGetWhenEntriesExistsAndThereIsCategoryIdThenReturnCategoryDTO() throws BadRequestException {
        List<EntryEntity> entryEntities = new java.util.ArrayList<>(List.of(
                mockEntryEntity(BigDecimal.valueOf(10.50), 1L, 2L, 2L),
                mockEntryEntity(BigDecimal.valueOf(10.50), 2L, 1L, 1L),
                mockEntryEntity(BigDecimal.valueOf(-10.00), 3L, 2L, 2L)
        ));

        when(entryRepository.findAllByDateBetween(any(), any())).thenReturn(entryEntities);

        List<EntryDTO> entries = entryService.getEntries(LocalDate.now(), LocalDate.now(), 2L);

        assertNotNull(entries);
        assertEquals(2, entries.size());
    }

    @Test
    void testGetWhenEntriesDoesNotExistsThenReturnEmptyList() throws BadRequestException {
        when(entryRepository.findAll(any(Specification.class))).thenReturn(emptyList());

        List<EntryDTO> entries = entryService.getEntries(null, null, null);

        assertNotNull(entries);
        assertTrue(entries.isEmpty());
    }

    @Test
    void testGetStatementWhenStartDateIsNullThenThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> entryService.getStatement(null, LocalDate.now(), 1L));
    }

    @Test
    void testGetStatementWhenEndDateIsNullThenThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> entryService.getStatement(LocalDate.now(), null, 1L));
    }

    @Test
    void testGetStatementWhenCategoryIdIsInvalidThenThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> entryService.getStatement(LocalDate.now(), LocalDate.now(), 0L));
    }

    @Test
    void testGetStatementWhenRequestIsValidThenReturnStatementDTO() throws BadRequestException {
        List<EntryEntity> entryEntities = new java.util.ArrayList<>(List.of(
                mockEntryEntity(BigDecimal.valueOf(10.50), 1L, 2L, 2L),
                mockEntryEntity(BigDecimal.valueOf(10.50), 2L, 1L, 2L),
                mockEntryEntity(BigDecimal.valueOf(-10.00), 3L, 2L, 2L)
        ));

        when(entryRepository.findAllByDateBetween(any(), any())).thenReturn(entryEntities);

        StatementDTO result = entryService.getStatement(LocalDate.now(), LocalDate.now(), null);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(21).setScale(2, RoundingMode.CEILING), result.getIncome());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.CEILING), result.getExpense());
        assertEquals(BigDecimal.valueOf(11).setScale(2, RoundingMode.CEILING), result.getBalance());
    }

    @Test
    void testGetStatementWhenThereIsNoEntriesThenReturnStatementDTO() throws BadRequestException {
        when(entryRepository.findAllByDateBetween(any(), any())).thenReturn(emptyList());

        StatementDTO result = entryService.getStatement(LocalDate.now(), LocalDate.now(), null);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING), result.getIncome());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING), result.getExpense());
        assertEquals(BigDecimal.valueOf(0).setScale(2, RoundingMode.CEILING), result.getBalance());
    }

    @Test
    void testGetStatementWhenRequestIsValidAndThereIsCategoryIdThenReturnStatementDTO() throws BadRequestException {
        List<EntryEntity> entryEntities = new java.util.ArrayList<>(List.of(
                mockEntryEntity(BigDecimal.valueOf(10.50), 1L, 2L, 2L),
                mockEntryEntity(BigDecimal.valueOf(10.50), 2L, 1L, 2L),
                mockEntryEntity(BigDecimal.valueOf(-10.00), 3L, 2L, 2L)
        ));

        when(entryRepository.findAllByDateBetween(any(), any())).thenReturn(entryEntities);

        StatementDTO result = entryService.getStatement(LocalDate.now(), LocalDate.now(), 2L);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(10.50).setScale(2, RoundingMode.CEILING), result.getIncome());
        assertEquals(BigDecimal.valueOf(10).setScale(2, RoundingMode.CEILING), result.getExpense());
        assertEquals(BigDecimal.valueOf(0.5).setScale(2, RoundingMode.CEILING), result.getBalance());
        assertEquals(entryEntities.get(0).getSubcategory().getCategory().getId(), result.getCategory().getId());
    }

    private EntryEntity mockEntryEntity(BigDecimal value, Long id, Long categoryId, Long subcategoryId) {
        EntryEntity entry = new EntryEntity();
        entry.setId(id);
        entry.setDate(LocalDate.now());
        entry.setValue(value);

        SubcategoryEntity subcategory = new SubcategoryEntity();
        subcategory.setId(subcategoryId);
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);
        subcategory.setCategory(category);

        entry.setSubcategory(subcategory);

        return entry;
    }
}