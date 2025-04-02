package br.com.api.domain.services;

import br.com.api.application.dto.EntryDTO;
import br.com.api.application.dto.StatementDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface EntryService {
    Long createEntry(EntryDTO request) throws NotFoundException, BadRequestException;
    void updateEntry(EntryDTO request, Long id) throws NotFoundException, BadRequestException;
    void deleteEntry(Long id) throws NotFoundException;
    EntryDTO getEntryById(Long id) throws NotFoundException;
    List<EntryDTO> getEntries(LocalDate startDate, LocalDate endDate, Long subcategoryId) throws BadRequestException;
    StatementDTO getStatement(LocalDate startDate, LocalDate endDate, Long categoryId) throws BadRequestException;
}
