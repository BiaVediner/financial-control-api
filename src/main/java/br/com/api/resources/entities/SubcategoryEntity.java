package br.com.api.resources.entities;

import br.com.api.application.dto.SubcategoryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "subcategory")
@Getter
@Setter
public class SubcategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    private CategoryEntity category;
    @OneToMany(mappedBy = "subcategory")
    private List<EntryEntity> entries;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public SubcategoryDTO toDto() {
        SubcategoryDTO dto = new SubcategoryDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setCategoryId(this.category.getId());
        return dto;
    }

    public boolean isDeletable() {
        return this.entries.isEmpty();
    }
}
