package br.com.api.application.controller;

import br.com.api.application.dto.StatementDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.services.EntryService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/statements")
public class StatementController {
    @Autowired
    private EntryService entryService;

    @GetMapping
    public ResponseEntity<StatementDTO> getStatement(
            @RequestParam(name = "startDate") @JsonFormat(pattern="dd/MM/yyyy") LocalDate startDate,
            @RequestParam(name = "endDate") @JsonFormat(pattern="dd/MM/yyyy") LocalDate endDate,
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) throws BadRequestException {
        StatementDTO statementDTO = entryService.getStatement(startDate, endDate, categoryId);

        return ResponseEntity.ok(statementDTO);
    }
}
