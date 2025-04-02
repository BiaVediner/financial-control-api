package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.CATEGORY_INVALID_NAME;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    private Long id;
    private String name;
    private List<SubcategoryDTO> subcategories;

    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        entity.setName(this.getName());
        return entity;
    }

    public void validate() throws BadRequestException {
        if(this.name == null || this.name.isEmpty()) {
            throw new BadRequestException(CATEGORY_INVALID_NAME);
        }
    }
}
