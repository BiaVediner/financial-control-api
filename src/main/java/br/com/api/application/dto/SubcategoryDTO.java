package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.CategoryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_INVALID_CATEGORY_ID;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.SUBCATEGORY_INVALID_NAME;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubcategoryDTO {
    private Long id;
    @NotBlank(message = "Name must be valid")
    private String name;
    @Positive(message = "categoryId must be a valid id")
    private Long categoryId;

    public SubcategoryEntity toEntity(CategoryEntity category) {
        SubcategoryEntity entity = new SubcategoryEntity();
        entity.setName(this.name);
        entity.setCategory(category);
        return entity;
    }

    public void validate() throws BadRequestException {
        if(this.name == null || this.name.isEmpty()) {
            throw new BadRequestException(SUBCATEGORY_INVALID_NAME);
        }
        if(this.categoryId == null || this.categoryId == 0L) {
            throw new BadRequestException(SUBCATEGORY_INVALID_CATEGORY_ID);
        }
    }
}
