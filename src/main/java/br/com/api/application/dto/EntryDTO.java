package br.com.api.application.dto;

import br.com.api.domain.exceptions.BadRequestException;
import br.com.api.resources.entities.EntryEntity;
import br.com.api.resources.entities.SubcategoryEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_INVALID_SUBCATEGORY_ID;
import static br.com.api.domain.exceptions.enums.ErrorMessageEnum.ENTRY_INVALID_VALUE;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EntryDTO {
    private Long id;
    private BigDecimal value;
    @JsonFormat(pattern="dd/MM/yyyy")
    private LocalDate date;
    private Long subcategoryId;
    private String commentary;

    public EntryEntity toEntity(SubcategoryEntity subcategory) {
        EntryEntity entity = new EntryEntity();
        entity.setValue(this.value);
        entity.setDate(date != null ? date : LocalDate.now());
        entity.setCommentary(this.commentary);
        entity.setSubcategory(subcategory);
        return entity;
    }

    public void validate() throws BadRequestException {
        if(this.value == null || this.value.equals(BigDecimal.ZERO)) {
            throw new BadRequestException(ENTRY_INVALID_VALUE);
        }
        if(this.subcategoryId == null || this.subcategoryId == 0L) {
            throw new BadRequestException(ENTRY_INVALID_SUBCATEGORY_ID);
        }
    }
}
