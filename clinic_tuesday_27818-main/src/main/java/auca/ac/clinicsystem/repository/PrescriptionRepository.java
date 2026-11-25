package auca.ac.clinicsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.clinicsystem.domain.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,UUID> {

    List<Prescription> findByAppointment_Id(UUID appointmentId);
    boolean existsByAppointment_Id(UUID appointmentId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber ORDER BY p.issuedDate DESC")
    List<Prescription> findByPatientSsNumber(@Param("ssNumber") String ssNumber);

    @Query("SELECT p.medicineName, COUNT(p) FROM Prescription p GROUP BY p.medicineName ORDER BY COUNT(p) DESC")
    List<Object[]> findTopMedicines();
}