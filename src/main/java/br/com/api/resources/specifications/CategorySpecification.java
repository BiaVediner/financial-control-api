package br.com.api.resources.specifications;

import br.com.api.resources.entities.CategoryEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CategorySpecification {

    public static Specification<CategoryEntity> getSpecification(String name) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("name"), name));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}