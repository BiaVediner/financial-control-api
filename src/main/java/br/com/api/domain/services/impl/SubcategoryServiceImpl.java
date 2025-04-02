package br.com.api.domain.services.impl;

import br.com.api.application.dto.SubcategoryDTO;
import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.domain.exceptions.NotFoundException;
import br.com.api.domain.services.SubcategoryService;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import br.com.api.resources.repositories.CategoryRepository;
import br.com.api.resources.repositories.SubcategoryRepository;
import br.com.api.resources.specifications.SubcategorySpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_NOT_FOUND;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_DELETE_CONSTRAIN;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_NOT_FOUND;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_REGISTER_CONSTRAIN;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Long createSubcategory(SubcategoryDTO request) throws BadRequestException, NotFoundException {
        request.validate();

        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(request.getCategoryId());

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(subcategoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(SUBCATEGORY_REGISTER_CONSTRAIN);
        }

        return subcategoryRepository.save(request.toEntity(category)).getId();
    }

    @Override
    public void updateSubcategory(SubcategoryDTO request, Long id) throws BadRequestException, NotFoundException {
        request.validate();

        Optional<SubcategoryEntity> optionalSubcategory = subcategoryRepository.findById(id);

        if(optionalSubcategory.isEmpty()) {
            throw new NotFoundException(SUBCATEGORY_NOT_FOUND);
        }

        SubcategoryEntity subcategory = optionalSubcategory.get();

        Optional<CategoryEntity> optionalCategory = categoryRepository.findById(request.getCategoryId());

        if(optionalCategory.isEmpty()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND);
        }

        CategoryEntity category = optionalCategory.get();

        if(subcategoryRepository.existsByName(request.getName())) {
            throw new BadRequestException(SUBCATEGORY_REGISTER_CONSTRAIN);
        }

        subcategory.setName(request.getName());
        subcategory.setCategory(category);
        subcategory.setUpdatedAt(LocalDateTime.now());

        subcategoryRepository.save(subcategory);
    }

    @Override
    public void deleteSubcategory(Long id) throws BadRequestException, NotFoundException {
        Optional<SubcategoryEntity> optionalSubcategory = subcategoryRepository.findById(id);

        if(optionalSubcategory.isEmpty()) {
            throw new NotFoundException(SUBCATEGORY_NOT_FOUND);
        }

        SubcategoryEntity subcategory = optionalSubcategory.get();
        if(!subcategory.isDeletable()) {
            throw new BadRequestException(SUBCATEGORY_DELETE_CONSTRAIN);
        }

        subcategoryRepository.deleteById(id);
    }

    @Override
    public SubcategoryDTO getSubcategoryById(Long id) throws NotFoundException {
        Optional<SubcategoryEntity> optionalSubcategory = subcategoryRepository.findById(id);

        if(optionalSubcategory.isEmpty()) {
            throw new NotFoundException(SUBCATEGORY_NOT_FOUND);
        }

        SubcategoryEntity subcategory = optionalSubcategory.get();

        return subcategory.toDto();
    }

    @Override
    public List<SubcategoryDTO> getSubcategories(String name, Long categoryId) {
        List<SubcategoryEntity> subcategories = subcategoryRepository.findAll(SubcategorySpecification.getSpecification(name, categoryId));

        return subcategories.stream().map(SubcategoryEntity::toDto).toList();
    }
}
