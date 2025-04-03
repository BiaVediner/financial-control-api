package br.com.api.application.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatementDTOTest {
    @Test
    void testDto() {
        CategoryDTO categoryDTO = new CategoryDTO();
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setCategory(categoryDTO);
        statementDTO.setBalance(BigDecimal.TEN);
        statementDTO.setIncome(BigDecimal.valueOf(20));
        statementDTO.setExpense(BigDecimal.TEN);

        assertEquals(categoryDTO, statementDTO.getCategory());
        assertEquals(BigDecimal.TEN, statementDTO.getBalance());
        assertEquals(BigDecimal.TEN, statementDTO.getExpense());
        assertEquals(BigDecimal.valueOf(20), statementDTO.getIncome());
    }

    @Test
    void testToDto() {
        CategoryDTO categoryDTO = new CategoryDTO();
        StatementDTO statementDTO = StatementDTO.toDTO(categoryDTO, BigDecimal.valueOf(20), BigDecimal.TEN, BigDecimal.TEN);

        assertEquals(categoryDTO, statementDTO.getCategory());
        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.CEILING), statementDTO.getBalance());
        assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.CEILING), statementDTO.getExpense());
        assertEquals(BigDecimal.valueOf(20).setScale(2, RoundingMode.CEILING), statementDTO.getIncome());
    }
}