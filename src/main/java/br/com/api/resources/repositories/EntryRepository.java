package br.com.api.resources.repositories;

import br.com.api.resources.entities.EntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EntryRepository extends JpaRepository<EntryEntity, Long>, JpaSpecificationExecutor<EntryEntity> {
    List<EntryEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
