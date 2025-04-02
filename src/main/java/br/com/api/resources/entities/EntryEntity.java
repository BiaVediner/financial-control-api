package br.com.api.resources.entities;

import br.com.api.application.dto.EntryDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "entry")
@Getter
@Setter
public class EntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal value;
    private LocalDate date;
    private String commentary;
    @ManyToOne
    private SubcategoryEntity subcategory;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public EntryDTO toDTO() {
        EntryDTO dto = new EntryDTO();
        dto.setId(this.id);
        dto.setValue(this.value);
        dto.setDate(this.date);
        dto.setCommentary(this.commentary);
        dto.setSubcategoryId(subcategory.getId());
        return dto;
    }
}
