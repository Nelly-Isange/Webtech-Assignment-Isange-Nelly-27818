package auca.ac.clinicsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.domain.Patient;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.DoctorRepository;
import auca.ac.clinicsystem.repository.PatientRepository;
import auca.ac.clinicsystem.repository.PrescriptionRepository;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appRepo;

    @Autowired
    private PatientRepository patRepo;

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    public String saveAppointment(Appointment appointment, UUID patientId, UUID doctorId){
        if(appointment.getAppointmentDate().isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }

        Optional<Patient> patient = patRepo.findById(patientId);

        if(patient.isEmpty()){
            return "Patient not found";
        }

        Optional<Doctor> doctor = docRepo.findById(doctorId);

        if(doctor.isEmpty()){
            return "Doctor not found";
        }

        if(appRepo.existsByDoctorAndAppointmentDateAndStatusNot(doctor.get(), appointment.getAppointmentDate(), AppointmentStatus.CANCELLED)){
            return "Doctor is already booked on that date";
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setPatient(patient.get());
        appointment.setDoctor(doctor.get());
        appRepo.save(appointment);
        return "Appointment is saved successfully";
    }

    public List<Appointment> getAllAppointments(){
        return appRepo.findAll();
    }

    public Page<Appointment> getAppointmentsPage(Pageable pageable){
        return appRepo.findAll(pageable);
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status){
        return appRepo.findByStatusOrderByAppointmentDateAsc(status);
    }

    public List<Appointment> getAppointmentsBetween(LocalDate start, LocalDate end){
        return appRepo.findByAppointmentDateBetweenOrderByAppointmentDateAsc(start, end);
    }

    public List<Object[]> getAppointmentsByStatusStats(){
        return appRepo.countAppointmentsByStatus();
    }

    @Transactional
    public String cancelAppointmentsForDoctorOnDate(UUID doctorId, LocalDate date){
        int updated = appRepo.cancelAppointmentsForDoctorOnDate(doctorId, date, AppointmentStatus.CANCELLED, AppointmentStatus.COMPLETED);
        return updated + " appointments cancelled";
    }

    @Transactional
    public String deleteCancelledAppointmentsBefore(LocalDate date){
        int deleted = appRepo.deleteCancelledAppointmentsBefore(date, AppointmentStatus.CANCELLED);
        return deleted + " appointments deleted";
    }

    public Optional<Appointment> getAppointmentById(UUID id){
        return appRepo.findById(id);
    }

    public String updateAppointment(UUID id, Appointment appointment){
        Optional<Appointment> existingAppointment = appRepo.findById(id);

        if(existingAppointment.isEmpty()){
            return "Appointment not found";
        }

        Appointment appointmentToUpdate = existingAppointment.get();
        appointmentToUpdate.setAppointmentDate(appointment.getAppointmentDate());
        appointmentToUpdate.setReason(appointment.getReason());

        appRepo.save(appointmentToUpdate);
        return "Appointment is updated successfully";
    }

    public String updateAppointmentStatus(UUID id, AppointmentStatus status){
        Optional<Appointment> existingAppointment = appRepo.findById(id);
        if(existingAppointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();
        AppointmentStatus currentStatus = appointment.getStatus();
        boolean allowed = (currentStatus == AppointmentStatus.SCHEDULED && (status == AppointmentStatus.CONFIRMED || status == AppointmentStatus.CANCELLED))
                || (currentStatus == AppointmentStatus.CONFIRMED && (status == AppointmentStatus.COMPLETED || status == AppointmentStatus.CANCELLED));

        if(!allowed){
            return "Cannot change status from " + currentStatus + " to " + status;
        }

        appointment.setStatus(status);
        appRepo.save(appointment);
        return "Appointment status updated successfully";
    }

    public String rescheduleAppointment(UUID id, LocalDate newDate){
        Optional<Appointment> existingAppointment = appRepo.findById(id);
        if(existingAppointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        Appointment appointment = existingAppointment.get();
        if(appointment.getStatus() == AppointmentStatus.COMPLETED || appointment.getStatus() == AppointmentStatus.CANCELLED){
            return "A " + appointment.getStatus() + " appointment cannot be rescheduled";
        }

        if(newDate.isBefore(LocalDate.now())){
            return "Appointment date cannot be in the past";
        }

        if(appRepo.existsByDoctorAndAppointmentDateAndStatusNotAndIdNot(appointment.getDoctor(), newDate, AppointmentStatus.CANCELLED, id)){
            return "Doctor is already booked on that date";
        }

        appointment.setAppointmentDate(newDate);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appRepo.save(appointment);
        return "Appointment rescheduled successfully";
    }

    public String deleteAppointment(UUID id){
        if(prescriptionRepo.existsByAppointment_Id(id)){
            return "That appointment has prescriptions, delete them first";
        }

        if(!appRepo.existsById(id)){
            return "Appointment not found";
        }

        appRepo.deleteById(id);
        return "Appointment is deleted successfully";
    }
}