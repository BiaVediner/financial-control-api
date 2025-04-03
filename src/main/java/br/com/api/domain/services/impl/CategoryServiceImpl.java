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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_NOT_FOUND;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_REGISTER_CONSTRAIN;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_DELETE_CONSTRAIN;

@Slf4j
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
            log.error("Create Category Error - Repeated creation attempt");
            throw new BadRequestException(CATEGORY_REGISTER_CONSTRAIN);
        }

        Long categoryId = categoryRepository.save(request.toEntity()).getId();

        log.debug("Create Category Successful");
        return categoryId;
    }

    @Override
    public void updateCategory(CategoryDTO request, Long id) throws BadRequestException, NotFoundException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            log.error("Update Category Error - attempt with not found category");
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(categoryRepository.existsByName(request.getName())) {
            log.error("Update Category Error - Repeated creation attempt");
            throw new BadRequestException(CATEGORY_REGISTER_CONSTRAIN);
        }

        category.setName(request.getName());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepository.save(category);

        log.debug("Update Category Successful");
    }

    @Override
    public void deleteCategory(Long id) throws NotFoundException, BadRequestException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            log.error("Delete Category Error - attempt with not found category");
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(!category.getSubcategories().isEmpty()) {
            for (SubcategoryEntity subcategory : category.getSubcategories()) {
                if (!subcategory.isDeletable()) {
                    log.error("Delete Category Error - attempt with subcategory with entries");
                    throw new BadRequestException(SUBCATEGORY_DELETE_CONSTRAIN);
                }
                subcategoryRepository.delete(subcategory);
            }
        }

        categoryRepository.deleteById(id);

        log.debug("Delete Category Successful");
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws NotFoundException {
        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(id);

        if(optionalCategory.isEmpty()) {
            log.error("Get Category by Id Error - attempt with not found category");
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        List<SubcategoryDTO> subcategories = category.getSubcategories().stream().map(SubcategoryEntity::toDto).toList();

        log.debug("Get Category by Id Successful");
        return category.toDto(subcategories);
    }

    @Override
    public List<CategoryDTO> getCategories(String name) {
        List<CategoryEntity> categories = categoryRepository.findAll(CategorySpecification.getSpecification(name));

        log.debug("Get Categories Successful");
        return categories.stream().map(CategoryEntity::toDto).toList();
    }
}
