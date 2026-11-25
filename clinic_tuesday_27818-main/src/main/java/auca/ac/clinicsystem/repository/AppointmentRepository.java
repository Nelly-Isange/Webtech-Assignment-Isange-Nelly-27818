package auca.ac.clinicsystem.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.domain.Doctor;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,UUID> {

    List<Appointment> findByStatusOrderByAppointmentDateAsc(AppointmentStatus status);
    List<Appointment> findByAppointmentDateBetweenOrderByAppointmentDateAsc(LocalDate start, LocalDate end);
    boolean existsByDoctorAndAppointmentDateAndStatusNot(Doctor doctor, LocalDate appointmentDate, AppointmentStatus status);
    boolean existsByDoctorAndAppointmentDateAndStatusNotAndIdNot(Doctor doctor, LocalDate appointmentDate, AppointmentStatus status, UUID id);
    List<Appointment> findByDoctor_IdAndAppointmentDateAndStatusNot(UUID doctorId, LocalDate appointmentDate, AppointmentStatus status);
    List<Appointment> findByPatient_SsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(String ssNumber, LocalDate appointmentDate, List<AppointmentStatus> statuses);
    long countByAppointmentDate(LocalDate appointmentDate);
    long countByStatus(AppointmentStatus status);

    @Query("SELECT a.status, COUNT(a) FROM Appointment a GROUP BY a.status")
    List<Object[]> countAppointmentsByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE Appointment a SET a.status = :cancelledStatus WHERE a.doctor.id = :doctorId AND a.appointmentDate = :date AND (a.status <> :completedStatus OR a.status IS NULL)")
    int cancelAppointmentsForDoctorOnDate(@Param("doctorId") UUID doctorId, @Param("date") LocalDate date, @Param("cancelledStatus") AppointmentStatus cancelledStatus, @Param("completedStatus") AppointmentStatus completedStatus);

    @Modifying
    @Query("DELETE FROM Appointment a WHERE a.status = :status AND a.appointmentDate < :date")
    int deleteCancelledAppointmentsBefore(@Param("date") LocalDate date, @Param("status") AppointmentStatus status);
}