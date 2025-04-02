package br.com.api.resources.repositories;

import br.com.api.resources.entities.SubcategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, Long>, JpaSpecificationExecutor<SubcategoryEntity> {
    boolean existsByName(String name);
}
