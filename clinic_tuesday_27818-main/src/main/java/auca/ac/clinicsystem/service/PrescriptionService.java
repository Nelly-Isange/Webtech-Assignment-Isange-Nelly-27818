package auca.ac.clinicsystem.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.domain.Prescription;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.PrescriptionRepository;

@Service
public class PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    public String savePrescription(Prescription prescription, UUID appointmentId){
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(appointment.isEmpty()){
            return "The appointment with that id does not exist";
        }

        if(appointment.get().getStatus() != AppointmentStatus.COMPLETED){
            return "Prescriptions are only for COMPLETED appointments";
        }

        prescription.setAppointment(appointment.get());
        prescription.setIssuedDate(LocalDate.now());
        prescriptionRepo.save(prescription);
        return "Prescription saved successfully";
    }

    public Optional<List<Prescription>> getPrescriptionsByAppointment(UUID appointmentId){
        if(appointmentRepo.findById(appointmentId).isEmpty()){
            return Optional.empty();
        }
        return Optional.of(prescriptionRepo.findByAppointment_Id(appointmentId));
    }

    public List<Prescription> getPrescriptionsByPatient(String ssNumber){
        return prescriptionRepo.findByPatientSsNumber(ssNumber);
    }

    public List<Object[]> getTopMedicines(){
        return prescriptionRepo.findTopMedicines();
    }

    public String updatePrescription(UUID id, Prescription prescription){
        Optional<Prescription> existingPrescription = prescriptionRepo.findById(id);
        if(existingPrescription.isEmpty()){
            return "The prescription with that id does not exist";
        }

        Prescription prescriptionToUpdate = existingPrescription.get();
        prescriptionToUpdate.setMedicineName(prescription.getMedicineName());
        prescriptionToUpdate.setDosage(prescription.getDosage());
        prescriptionToUpdate.setDurationDays(prescription.getDurationDays());
        prescriptionRepo.save(prescriptionToUpdate);
        return "Prescription updated successfully";
    }

    public String deletePrescription(UUID id){
        if(!prescriptionRepo.existsById(id)){
            return "The prescription with that id does not exist";
        }

        prescriptionRepo.deleteById(id);
        return "Prescription deleted successfully";
    }
}