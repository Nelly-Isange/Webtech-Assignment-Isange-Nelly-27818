package auca.ac.clinicsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import auca.ac.clinicsystem.domain.Specialization;

@Repository
public interface SpecializationRepository extends JpaRepository<Specialization,UUID> {

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT s FROM Specialization s WHERE s.doctors IS EMPTY")
    List<Specialization> findUnusedSpecializations();
}