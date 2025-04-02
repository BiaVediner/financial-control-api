package br.com.api.domain.services;

import br.com.api.application.dto.CategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;

import java.util.List;

public interface CategoryService {
    Long createCategory(CategoryDTO categoryRequestDTO) throws BadRequestException;
    void updateCategory(CategoryDTO categoryRequestDTO, Long id) throws BadRequestException, NotFoundException;
    void deleteCategory(Long id) throws NotFoundException, BadRequestException;
    CategoryDTO getCategoryById(Long id) throws NotFoundException;
    List<CategoryDTO> getCategories(String name);
}
