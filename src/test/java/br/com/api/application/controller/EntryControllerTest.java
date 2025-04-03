package br.com.api.application.controller;

import br.com.api.application.dto.EntryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.impl.EntryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
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
class EntryControllerTest {

    @InjectMocks
    private EntryController entryController;

    @Mock
    private EntryServiceImpl entryService;

    @Test
    void testCreateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        doThrow(NotFoundException.class).when(entryService).createEntry(entryDTO);

        assertThrows(NotFoundException.class, () -> entryController.createEntry(entryDTO));
    }

    @Test
    void testCreateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        when(entryService.createEntry(entryDTO)).thenReturn(1L);

        ResponseEntity<EntryDTO> response = entryController.createEntry(entryDTO);

        assertNotNull(response.getBody().getId());
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
    }

    @Test
    void testUpdateWhenEntryDoesNotExistsThenThrowNotFoundException() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        doThrow(NotFoundException.class).when(entryService).updateEntry(entryDTO, 1L);

        assertThrows(NotFoundException.class, () -> entryController.updateEntry(1L, entryDTO));
    }

    @Test
    void testUpdateWhenSubcategoryDoesNotExistsThenThrowNotFoundException() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        doThrow(NotFoundException.class).when(entryService).updateEntry(entryDTO, 1L);

        assertThrows(NotFoundException.class, () -> entryController.updateEntry(1L, entryDTO));
    }

    @Test
    void testUpdateWhenRequestIsValidThenReturnId() throws NotFoundException, BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        doNothing().when(entryService).updateEntry(entryDTO, 1L);

        ResponseEntity<Void> response = entryController.updateEntry(1L, entryDTO);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testDeleteWhenEntryDoesNotExistsThenThrowNotFoundException() throws NotFoundException {
        doThrow(NotFoundException.class).when(entryService).deleteEntry(1L);

        assertThrows(NotFoundException.class, () -> entryController.deleteEntry(1L));
    }


    @Test
    void testDeleteWhenEntryIsDeletableThenDeleteSuccessfully() throws NotFoundException {
        doNothing().when(entryService).deleteEntry(1L);

        ResponseEntity<Void> response = entryController.deleteEntry(1L);

        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetByIdWhenEntryDoesNotExistsThenThrowNotFoundException() throws NotFoundException {
        doThrow(NotFoundException.class).when(entryService).getEntryById(1L);

        assertThrows(NotFoundException.class, () -> entryController.getEntryById(1L));
    }

    @Test
    void testGetByIdWhenEntryExistsThenReturnEntryDTO() throws NotFoundException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        when(entryService.getEntryById(any())).thenReturn(entryDTO);

        ResponseEntity<EntryDTO> response = entryController.getEntryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetWhenThereIsNoEntriesThenReturnEmptyList() throws BadRequestException {
        when(entryService.getEntries(any(), any(), any())).thenReturn(emptyList());

        ResponseEntity<List<EntryDTO>> response = entryController.getEntries(LocalDate.now(), LocalDate.now(),1L);

        assertNotNull(response);
        assertEquals(HttpStatusCode.valueOf(204), response.getStatusCode());
    }

    @Test
    void testGetWhenEntryExistsThenReturnEntryDTO() throws BadRequestException {
        EntryDTO entryDTO = mock(EntryDTO.class);

        when(entryService.getEntries(any(), any(), any())).thenReturn(List.of(entryDTO));

        ResponseEntity<List<EntryDTO>> response = entryController.getEntries(LocalDate.now(), LocalDate.now(),1L);

        assertNotNull(response);
        assertEquals(1, response.getBody().size());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}