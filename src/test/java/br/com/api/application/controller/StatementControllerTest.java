package br.com.api.application.controller;

import br.com.api.application.dto.StatementDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.services.impl.EntryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @InjectMocks
    private StatementController statementController;

    @Mock
    private EntryServiceImpl entryService;

    @Test
    void testGetStatementWhenStartDateIsNullThenThrowBadRequestException() throws BadRequestException {
        doThrow(BadRequestException.class).when(entryService).getStatement(null, LocalDate.now(), 1L);

        assertThrows(BadRequestException.class, () -> statementController.getStatement(null, LocalDate.now(), 1L));
    }

    @Test
    void testGetStatementWhenEndDateIsNullThenThrowBadRequestException() throws BadRequestException {
        doThrow(BadRequestException.class).when(entryService).getStatement(LocalDate.now(), null, 1L);

        assertThrows(BadRequestException.class, () -> statementController.getStatement(LocalDate.now(), null, 1L));
    }

    @Test
    void testGetStatementWhenCategoryIdIsInvalidThenThrowBadRequestException() throws BadRequestException {
        doThrow(BadRequestException.class).when(entryService).getStatement(LocalDate.now(), LocalDate.now(), 0L);

        assertThrows(BadRequestException.class, () -> statementController.getStatement(LocalDate.now(), LocalDate.now(), 0L));
    }

    @Test
    void testGetStatementWhenRequestIsValidThenReturnStatementDTO() throws BadRequestException {
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setBalance(BigDecimal.TEN);

        when(entryService.getStatement(any(), any(), any())).thenReturn(statementDTO);

        ResponseEntity<StatementDTO> response = statementController.getStatement(LocalDate.now(), LocalDate.now(), null);

        assertNotNull(response.getBody());
        assertEquals(BigDecimal.TEN, response.getBody().getBalance());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetStatementWhenThereIsNoEntriesThenReturnStatementDTO() throws BadRequestException {
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setBalance(BigDecimal.ZERO);

        when(entryService.getStatement(any(), any(), any())).thenReturn(statementDTO);

        ResponseEntity<StatementDTO> response = statementController.getStatement(LocalDate.now(), LocalDate.now(), null);

        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ZERO, response.getBody().getBalance());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    void testGetStatementWhenRequestIsValidAndThereIsCategoryIdThenReturnStatementDTO() throws BadRequestException {
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setBalance(BigDecimal.ONE);

        when(entryService.getStatement(any(), any(), any())).thenReturn(statementDTO);

        ResponseEntity<StatementDTO> response = statementController.getStatement(LocalDate.now(), LocalDate.now(), 1L);

        assertNotNull(response.getBody());
        assertEquals(BigDecimal.ONE, response.getBody().getBalance());
        assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}