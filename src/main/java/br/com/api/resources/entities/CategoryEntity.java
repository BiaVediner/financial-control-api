package br.com.api.resources.entities;

import br.com.api.application.dto.CategoryDTO;
import br.com.api.application.dto.SubcategoryDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "category")
@Getter
@Setter
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "category")
    private List<SubcategoryEntity> subcategories;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public CategoryDTO toDto() {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(this.id);
        dto.setName(this.name);

        return dto;
    }

    public CategoryDTO toDto(List<SubcategoryDTO> subcategories) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setSubcategories(subcategories);

        return dto;
    }
}
