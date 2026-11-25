package auca.ac.clinicsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.domain.Office;
import auca.ac.clinicsystem.domain.Specialization;
import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.DoctorRepository;
import auca.ac.clinicsystem.repository.OfficeRepository;
import auca.ac.clinicsystem.repository.SpecializationRepository;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository docRepo;

    @Autowired
    private OfficeRepository offRepo;

    @Autowired
    private SpecializationRepository specRepo;

    @Autowired
    private AppointmentRepository appRepo;

    public String saveDoctor(Doctor doctor, UUID officeId){
        if(docRepo.existsByFirstNameAndLastName(doctor.getFirstName(), doctor.getLastName())){
            return "A doctor with this first name and last name already exists";
        }

        if(officeId == null){
            docRepo.save(doctor);
            return "Doctor is saved successfully";
        }

        Optional<Office> office = offRepo.findById(officeId);

        if(office.isEmpty()){
            return "Office not found";
        }

        if(docRepo.findByOffice(office.get()).isPresent()){
            return "Office is already assigned to another doctor";
        }

        doctor.setOffice(office.get());
        office.get().setDoctor(doctor);
        docRepo.save(doctor);
        return "Doctor is saved successfully";
    }

    public List<Doctor> getAllDoctors(){
        return docRepo.findAll();
    }

    public List<Doctor> getDoctorsBySpecialization(String name){
        return docRepo.doctorsBySpecialization(name);
    }

    public List<Doctor> getDoctorsWithoutOffice(){
        return docRepo.doctorsWithoutOffice();
    }

    public List<Appointment> getDoctorAgenda(UUID doctorId, java.time.LocalDate date){
        return appRepo.findByDoctor_IdAndAppointmentDateAndStatusNot(doctorId, date, AppointmentStatus.CANCELLED);
    }

    public List<Doctor> searchDoctors(String name){
        return docRepo.searchByName(name);
    }

    public List<Object[]> getDoctorWorkload(LocalDate start, LocalDate end){
        return docRepo.findDoctorWorkload(start, end, AppointmentStatus.COMPLETED);
    }

    @Transactional
    public String addSpecialization(UUID doctorId, UUID specializationId){
        Optional<Doctor> doctor = docRepo.findById(doctorId);
        if(doctor.isEmpty()){
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specialization = specRepo.findById(specializationId);
        if(specialization.isEmpty()){
            return "That specialization does not exist";
        }

        if(doctor.get().getSpecializations().contains(specialization.get())){
            return "The doctor already has that specialization";
        }

        doctor.get().getSpecializations().add(specialization.get());
        docRepo.save(doctor.get());
        return "Specialization added to the doctor";
    }

    @Transactional
    public String removeSpecialization(UUID doctorId, UUID specializationId){
        Optional<Doctor> doctor = docRepo.findById(doctorId);
        if(doctor.isEmpty()){
            return "The doctor with that id does not exist";
        }

        Optional<Specialization> specialization = specRepo.findById(specializationId);
        if(specialization.isEmpty()){
            return "That specialization does not exist";
        }

        if(!doctor.get().getSpecializations().contains(specialization.get())){
            return "The doctor does not have that specialization";
        }

        doctor.get().getSpecializations().remove(specialization.get());
        docRepo.save(doctor.get());
        return "Specialization removed from the doctor";
    }

    @Transactional
    public String changeOffice(UUID doctorId, int officeNumber){
        Optional<Doctor> doctor = docRepo.findById(doctorId);
        if(doctor.isEmpty()){
            return "The doctor with that id does not exist";
        }

        Optional<Office> office = offRepo.findByOfficeNumber(officeNumber);
        if(office.isEmpty()){
            return "The office with that office number does not exist";
        }

        if(docRepo.existsByOfficeOfficeNumberAndIdNot(officeNumber, doctorId)){
            return "That office is taken by other doctor";
        }

        Office oldOffice = doctor.get().getOffice();
        doctor.get().setOffice(office.get());
        if(oldOffice != null){
            oldOffice.setDoctor(null);
        }
        office.get().setDoctor(doctor.get());
        docRepo.save(doctor.get());
        return "Office changed successfully";
    }

    public Optional<Doctor> getDoctorById(UUID id){
        return docRepo.findById(id);
    }

    public String updateDoctor(UUID id, Doctor doctor){
        Optional<Doctor> existingDoctor = docRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "Doctor not found";
        }

        Doctor doctorToUpdate = existingDoctor.get();
        doctorToUpdate.setFirstName(doctor.getFirstName());
        doctorToUpdate.setLastName(doctor.getLastName());
        doctorToUpdate.setDateOfBirth(doctor.getDateOfBirth());

        docRepo.save(doctorToUpdate);
        return "Doctor is updated successfully";
    }

    @Transactional
    public String deleteDoctor(UUID id){
        Optional<Doctor> existingDoctor = docRepo.findById(id);

        if(existingDoctor.isEmpty()){
            return "Doctor not found";
        }

        Doctor doctorToDelete = existingDoctor.get();
        Office office = doctorToDelete.getOffice();

        doctorToDelete.setOffice(null);
        if(office != null){
            office.setDoctor(null);
        }

        docRepo.delete(doctorToDelete);
        return "Doctor is deleted successfully";
    }
}