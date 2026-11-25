package auca.ac.clinicsystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.service.DoctorService;

@RestController
@RequestMapping (value="/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService docServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveDoctor(@RequestBody Doctor doctor, @RequestParam(required = false) UUID officeId){

       String returnedMessage =  docServe.saveDoctor(doctor, officeId);

       if(returnedMessage.equals("Doctor is saved successfully")){
        return new ResponseEntity<>(returnedMessage,HttpStatus.OK);
       }else if(returnedMessage.equals("Office not found")){
        return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
       }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
       }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllDoctors(){
        List<Doctor> doctors = docServe.getAllDoctors();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/by-specialization")
    public ResponseEntity<?> getDoctorsBySpecialization(@RequestParam String name){
        List<Doctor> doctors = docServe.getDoctorsBySpecialization(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/without-office")
    public ResponseEntity<?> getDoctorsWithoutOffice(){
        List<Doctor> doctors = docServe.getDoctorsWithoutOffice();
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchDoctors(@RequestParam String name){
        List<Doctor> doctors = docServe.searchDoctors(name);
        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping(value = "/{doctorId}/agenda")
    public ResponseEntity<?> getDoctorAgenda(@PathVariable UUID doctorId, @RequestParam String date){
        if(docServe.getDoctorById(doctorId).isEmpty()){
            return new ResponseEntity<>("The doctor with that id does not exist", HttpStatus.NOT_FOUND);
        }

        List<Appointment> appointments = docServe.getDoctorAgenda(doctorId, LocalDate.parse(date));
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/workload")
    public ResponseEntity<?> getDoctorWorkload(@RequestParam String start, @RequestParam String end){
        List<Object[]> workload = docServe.getDoctorWorkload(LocalDate.parse(start), LocalDate.parse(end));
        return new ResponseEntity<>(workload, HttpStatus.OK);
    }

    @PutMapping(value = "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> addSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId){
        String returnedMessage = docServe.addSpecialization(doctorId, specializationId);

        if(returnedMessage.equals("Specialization added to the doctor")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("The doctor with that id does not exist") || returnedMessage.equals("That specialization does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/{doctorId}/specializations/{specializationId}")
    public ResponseEntity<?> removeSpecialization(@PathVariable UUID doctorId, @PathVariable UUID specializationId){
        String returnedMessage = docServe.removeSpecialization(doctorId, specializationId);

        if(returnedMessage.equals("Specialization removed from the doctor")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("The doctor with that id does not exist") || returnedMessage.equals("That specialization does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @PatchMapping(value = "/{doctorId}/office")
    public ResponseEntity<?> changeOffice(@PathVariable UUID doctorId, @RequestParam int officeNumber){
        String returnedMessage = docServe.changeOffice(doctorId, officeNumber);

        if(returnedMessage.equals("Office changed successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("The doctor with that id does not exist") || returnedMessage.equals("The office with that office number does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id){
        Optional<Doctor> doctor = docServe.getDoctorById(id);

        if(doctor.isPresent()){
            return new ResponseEntity<>(doctor.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable UUID id, @RequestBody Doctor doctor){
        String returnedMessage = docServe.updateDoctor(id, doctor);

        if(returnedMessage.equals("Doctor is updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id){
        String returnedMessage = docServe.deleteDoctor(id);

        if(returnedMessage.equals("Doctor is deleted successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }
}