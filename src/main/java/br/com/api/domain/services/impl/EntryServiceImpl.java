package br.com.api.domain.services.impl;

import br.com.api.application.dto.EntryDTO;
import br.com.api.application.dto.StatementDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.EntryService;
import br.com.api.resources.entities.EntryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import br.com.api.resources.repositories.EntryRepository;
import br.com.api.resources.repositories.SubcategoryRepository;
import br.com.api.resources.specifications.EntrySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_END_DATE_FILTER;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_ID_FILTER;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_NOT_FOUND;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_START_DATE_FILTER;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_NOT_FOUND;

@Service
public class EntryServiceImpl implements EntryService {

    @Autowired
    private EntryRepository entryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public Long createEntry(EntryDTO request) throws NotFoundException, BadRequestException {
        request.validate();

        Optional<SubcategoryEntity> optionalSubcategory = subcategoryRepository.findById(request.getSubcategoryId());

        if(optionalSubcategory.isEmpty()) {
            throw new NotFoundException(SUBCATEGORY_NOT_FOUND);
        }

        SubcategoryEntity subcategory = optionalSubcategory.get();

        return entryRepository.save(request.toEntity(subcategory)).getId();
    }

    @Override
    public void updateEntry(EntryDTO request, Long id) throws NotFoundException, BadRequestException {
        request.validate();

        Optional<EntryEntity> optionalEntry = entryRepository.findById(id);

        if(optionalEntry.isEmpty()) {
            throw new NotFoundException(ENTRY_NOT_FOUND);
        }

        EntryEntity entry = optionalEntry.get();

        Optional<SubcategoryEntity> optionalSubcategory = subcategoryRepository.findById(id);

        if(optionalSubcategory.isEmpty()) {
            throw new NotFoundException(SUBCATEGORY_NOT_FOUND);
        }

        SubcategoryEntity subcategory = optionalSubcategory.get();

        entry.setValue(request.getValue());
        entry.setDate(request.getDate());
        entry.setCommentary(request.getCommentary());
        entry.setSubcategory(subcategory);
        entry.setUpdatedAt(LocalDateTime.now());

        entryRepository.save(entry);
    }

    @Override
    public void deleteEntry(Long id) throws NotFoundException {
        if(!entryRepository.existsById(id)) {
            throw new NotFoundException(ENTRY_NOT_FOUND);
        }

        entryRepository.deleteById(id);
    }

    @Override
    public EntryDTO getEntryById(Long id) throws NotFoundException {
        Optional<EntryEntity> optionalEntry = entryRepository.findById(id);

        if(optionalEntry.isEmpty()) {
            throw new NotFoundException(ENTRY_NOT_FOUND);
        }

        EntryEntity entry = optionalEntry.get();

        return entry.toDTO();
    }

    @Override
    public List<EntryDTO> getEntries(LocalDate startDate, LocalDate endDate, Long subcategoryId) throws BadRequestException {
        this.validateFilter(startDate, endDate, subcategoryId);

        List<EntryEntity> entries;

        if(startDate != null) {
            entries = entryRepository.findAllByDateBetween(startDate, endDate);

            if(subcategoryId != null) {
                entries.removeIf(entry -> !Objects.equals(entry.getSubcategory().getId(), subcategoryId));
            }
        } else {
            entries = entryRepository.findAll(EntrySpecification.getSpecification(subcategoryId));
        }

        return entries.stream().map(EntryEntity::toDTO).toList();
    }

    @Override
    public StatementDTO getStatement(LocalDate startDate, LocalDate endDate, Long categoryId) throws BadRequestException {
        this.validateFilter(startDate, endDate, categoryId);

        List<EntryEntity> entries = entryRepository.findAllByDateBetween(startDate, endDate);

        if(categoryId != null) {
            entries.removeIf(entry -> !Objects.equals(entry.getSubcategory().getCategory().getId(), categoryId));
        }

        BigDecimal income = BigDecimal.ZERO;
        BigDecimal expense = BigDecimal.ZERO;

        for(EntryEntity entry: entries) {
            if(entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                income = income.add(entry.getValue());
            } else {
                expense = expense.add(entry.getValue().multiply(BigDecimal.valueOf(-1)));
            }
        }

        BigDecimal balance = income.subtract(expense);

        return StatementDTO.toDTO(null, income, expense, balance);
    }


    private void validateFilter(LocalDate startDate, LocalDate endDate, Long id) throws BadRequestException {
        if(startDate != null && endDate == null) {
            throw new BadRequestException(ENTRY_END_DATE_FILTER);
        }

        if(endDate != null && startDate == null) {
            throw new BadRequestException(ENTRY_START_DATE_FILTER);
        }

        if(id != null && id == 0L) {
            throw new BadRequestException(ENTRY_ID_FILTER);
        }
    }
}
