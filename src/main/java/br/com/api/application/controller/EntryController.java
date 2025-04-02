package br.com.api.application.controller;

import br.com.api.application.dto.EntryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.EntryService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/entries")
public class EntryController {
    @Autowired
    private EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryDTO> createEntry(@RequestBody EntryDTO request) throws NotFoundException, BadRequestException {
        Long entry = entryService.createEntry(request);

        EntryDTO createdEntry = new EntryDTO();
        createdEntry.setId(entry);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEntry(@PathVariable Long id, @RequestBody EntryDTO request) throws NotFoundException, BadRequestException {
        entryService.updateEntry(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEntry(@PathVariable Long id) throws NotFoundException {
        entryService.deleteEntry(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryDTO> getEntryById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    @GetMapping
    public ResponseEntity<List<EntryDTO>> getSubcategories(
            @RequestParam(name = "startDate", required = false) @JsonFormat(pattern="dd/MM/yyyy") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @JsonFormat(pattern="dd/MM/yyyy") LocalDate endDate,
            @RequestParam(name = "subcategoryId", required = false) Long subcategoryId
    ) throws BadRequestException {
        List<EntryDTO> entries = entryService.getEntries(startDate, endDate, subcategoryId);

        if(entries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(entries);
    }
}
