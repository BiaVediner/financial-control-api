package br.com.api.domain.services.impl;

import br.com.api.application.dto.CategoryDTO;
import br.com.api.application.dto.SubcategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.CategoryService;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import br.com.api.resources.repositories.CategoryRepository;
import br.com.api.resources.repositories.SubcategoryRepository;
import br.com.api.resources.specifications.CategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_NOT_FOUND;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_REGISTER_CONSTRAIN;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_DELETE_CONSTRAIN;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Override
    public Long createCategory(CategoryDTO request) throws BadRequestException {
        request.validate();

        if(categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(CATEGORY_REGISTER_CONSTRAIN);
        }

        return categoryRepository.save(request.toEntity()).getId();
    }

    @Override
    public void updateCategory(CategoryDTO request, Long id) throws BadRequestException, NotFoundException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(categoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(CATEGORY_REGISTER_CONSTRAIN);
        }

        category.setName(request.getName());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) throws NotFoundException, BadRequestException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(!category.getSubcategories().isEmpty()) {
            for (SubcategoryEntity subcategory : category.getSubcategories()) {
                if (!subcategory.isDeletable()) {
                    throw new BadRequestException(SUBCATEGORY_DELETE_CONSTRAIN);
                }
                subcategoryRepository.delete(subcategory);
            }
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws NotFoundException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        List<SubcategoryDTO> subcategories = category.getSubcategories().stream().map(SubcategoryEntity::toDto).toList();

        return category.toDto(subcategories);
    }

    @Override
    public List<CategoryDTO> getCategories(String name) {
        List<CategoryEntity> categories = categoryRepository.findAll(CategorySpecification.getSpecification(name));

        return categories.stream().map(CategoryEntity::toDto).toList();
    }
}
