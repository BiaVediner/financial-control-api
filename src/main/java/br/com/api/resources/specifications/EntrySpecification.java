package br.com.api.resources.specifications;

import br.com.api.resources.entities.EntryEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EntrySpecification {

    public static Specification<EntryEntity> getSpecification(Long subcategoryId) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(subcategoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("subcategory").get("id"), subcategoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}