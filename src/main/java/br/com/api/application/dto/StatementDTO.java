package br.com.api.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatementDTO {
    private CategoryDTO category;
    private BigDecimal income;
    private BigDecimal expense;
    private BigDecimal balance;

    public static StatementDTO toDTO(CategoryDTO category, BigDecimal income, BigDecimal expense, BigDecimal balance) {
        StatementDTO statementDTO = new StatementDTO();
        statementDTO.setCategory(category);
        statementDTO.setIncome(income.setScale(2, RoundingMode.CEILING));
        statementDTO.setExpense(expense.setScale(2, RoundingMode.CEILING));
        statementDTO.setBalance(balance.setScale(2, RoundingMode.CEILING));
        return statementDTO;
    }
}
