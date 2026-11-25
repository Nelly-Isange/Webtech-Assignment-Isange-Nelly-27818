package auca.ac.clinicsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.domain.Patient;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patRepo;

    @Autowired
    private AppointmentRepository appRepo;

    public String savePatient(Patient patient){
        patRepo.save(patient);
        return "Patient is saved successfully";
    }

    public List<Patient> getAllPatients(){
        return patRepo.findAll();
    }

    public List<Patient> getPatientsByLastName(String lastName){
        return patRepo.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Patient> getPatientsOfDoctor(UUID doctorId){
        return patRepo.findDistinctPatientsByDoctorId(doctorId);
    }

    public List<Patient> getFrequentPatients(long min){
        return patRepo.findFrequentPatients(min);
    }

    public Optional<List<Appointment>> getUpcomingAppointments(String ssNumber){
        if(patRepo.findBySsNumber(ssNumber).isEmpty()){
            return Optional.empty();
        }

        List<AppointmentStatus> statuses = List.of(AppointmentStatus.SCHEDULED, AppointmentStatus.CONFIRMED);
        List<Appointment> appointments = appRepo.findByPatient_SsNumberAndAppointmentDateGreaterThanEqualAndStatusInOrderByAppointmentDateAsc(ssNumber, java.time.LocalDate.now(), statuses);
        return Optional.of(appointments);
    }

    public Optional<Patient> getPatientById(UUID id){
        return patRepo.findById(id);
    }

    public String updatePatient(UUID id, Patient patient){
        Optional<Patient> existingPatient = patRepo.findById(id);

        if(existingPatient.isEmpty()){
            return "Patient not found";
        }

        Patient patientToUpdate = existingPatient.get();
        patientToUpdate.setFirstName(patient.getFirstName());
        patientToUpdate.setLastName(patient.getLastName());
        patientToUpdate.setDateOfBirth(patient.getDateOfBirth());

        patRepo.save(patientToUpdate);
        return "Patient is updated successfully";
    }

    public String deletePatient(UUID id){
        if(!patRepo.existsById(id)){
            return "Patient not found";
        }

        patRepo.deleteById(id);
        return "Patient is deleted successfully";
    }
}