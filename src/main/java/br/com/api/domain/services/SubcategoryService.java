package br.com.api.domain.services;

import br.com.api.application.dto.SubcategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;

import java.util.List;

public interface SubcategoryService {
    Long createSubcategory(SubcategoryDTO request) throws BadRequestException, NotFoundException;
    void updateSubcategory(SubcategoryDTO request, Long id) throws BadRequestException, NotFoundException;
    void deleteSubcategory(Long id) throws BadRequestException, NotFoundException;
    SubcategoryDTO getSubcategoryById(Long id) throws NotFoundException;
    List<SubcategoryDTO> getSubcategories(String name, Long categoryId);
}
