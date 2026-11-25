package auca.ac.clinicsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.domain.Office;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,UUID> {

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByOfficeOfficeNumberAndIdNot(int officeNumber, UUID id);
    List<Doctor> findByFirstName(String firstName);
    List<Doctor> findByLastName(String lastName);
    Optional<Doctor> findByOffice(Office office);

    @Query("SELECT d FROM Doctor d JOIN d.specializations s WHERE LOWER(s.name) = LOWER(:name)")
    List<Doctor> doctorsBySpecialization(@Param("name") String name);

    @Query("SELECT d FROM Doctor d WHERE LOWER(d.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(d.lastName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.lastName ASC")
    List<Doctor> searchByName(@Param("name") String name);

    @Query("SELECT d.firstName, d.lastName, COUNT(a) FROM Appointment a JOIN a.doctor d WHERE a.status = :status AND a.appointmentDate BETWEEN :start AND :end GROUP BY d.id, d.firstName, d.lastName ORDER BY COUNT(a) DESC")
    List<Object[]> findDoctorWorkload(@Param("start") LocalDate start, @Param("end") LocalDate end, @Param("status") auca.ac.clinicsystem.domain.AppointmentStatus status);

    @Query("SELECT d FROM Doctor d WHERE d.office IS NULL ORDER BY d.lastName")
    List<Doctor> doctorsWithoutOffice();
}