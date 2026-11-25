package auca.ac.clinicsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.domain.Specialization;
import auca.ac.clinicsystem.repository.DoctorRepository;
import auca.ac.clinicsystem.repository.SpecializationRepository;

@Service
public class SpecializationService {

    @Autowired
    private SpecializationRepository specRepo;

    @Autowired
    private DoctorRepository docRepo;

    public String saveSpecialization(Specialization specialization){
        if(specRepo.existsByNameIgnoreCase(specialization.getName())){
            return "Specialization already exists";
        }

        specRepo.save(specialization);
        return "Specialization is saved successfully";
    }

    public List<Specialization> getAllSpecializations(){
        return specRepo.findAll();
    }

    public List<Specialization> getUnusedSpecializations(){
        return specRepo.findUnusedSpecializations();
    }

    public Optional<Specialization> getSpecializationById(UUID id){
        return specRepo.findById(id);
    }

    @Transactional
    public String addDoctorToSpecialization(UUID id, UUID doctorId){
        Optional<Specialization> specialization = specRepo.findById(id);

        if(specialization.isEmpty()){
            return "Specialization not found";
        }

        Optional<Doctor> doctor = docRepo.findById(doctorId);

        if(doctor.isEmpty()){
            return "Doctor not found";
        }

        if(specialization.get().getDoctors().contains(doctor.get())){
            return "Doctor already has this specialization";
        }

        specialization.get().getDoctors().add(doctor.get());
        specRepo.save(specialization.get());
        return "Doctor is added to the specialization successfully";
    }

    public String updateSpecialization(UUID id, Specialization specialization){
        Optional<Specialization> existingSpecialization = specRepo.findById(id);

        if(existingSpecialization.isEmpty()){
            return "Specialization not found";
        }

        Specialization specializationToUpdate = existingSpecialization.get();
        specializationToUpdate.setName(specialization.getName());

        specRepo.save(specializationToUpdate);
        return "Specialization is updated successfully";
    }

    public String deleteSpecialization(UUID id){
        if(!specRepo.existsById(id)){
            return "Specialization not found";
        }

        specRepo.deleteById(id);
        return "Specialization is deleted successfully";
    }
}