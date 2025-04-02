package br.com.api.application.controller;

import br.com.api.application.dto.SubcategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.SubcategoryService;
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

import java.util.List;

@RestController
@RequestMapping("/subcategories")
public class SubcategoryController {
    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping
    public ResponseEntity<SubcategoryDTO> createSubcategory(@RequestBody SubcategoryDTO request) throws BadRequestException, NotFoundException {
        Long subcategoryId = subcategoryService.createSubcategory(request);

        SubcategoryDTO createdSubcategory = new SubcategoryDTO();
        createdSubcategory.setId(subcategoryId);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubcategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSubcategory(@PathVariable Long id, @RequestBody SubcategoryDTO request) throws BadRequestException, NotFoundException {
        subcategoryService.updateSubcategory(request, id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubcategory(@PathVariable Long id) throws BadRequestException, NotFoundException {
        subcategoryService.deleteSubcategory(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoryDTO> getSubcategoryById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(subcategoryService.getSubcategoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<SubcategoryDTO>> getSubcategories(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        List<SubcategoryDTO> subcategories = subcategoryService.getSubcategories(name, categoryId);

        if(subcategories.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(subcategories);
    }
}
