package auca.ac.clinicsystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.clinicsystem.domain.Prescription;
import auca.ac.clinicsystem.service.PrescriptionService;

@RestController
@RequestMapping(value = "/api/prescriptions")
public class PrescriptionController {

    @Autowired
    private PrescriptionService prescriptionService;

    @PostMapping(value = "/save")
    public ResponseEntity<?> savePrescription(@RequestBody Prescription prescription, @RequestParam UUID appointmentId){
        String returnedMessage = prescriptionService.savePrescription(prescription, appointmentId);

        if(returnedMessage.equals("Prescription saved successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CREATED);
        }else if(returnedMessage.equals("The appointment with that id does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/appointment/{appointmentId}")
    public ResponseEntity<?> getPrescriptionsByAppointment(@PathVariable UUID appointmentId){
        Optional<List<Prescription>> prescriptions = prescriptionService.getPrescriptionsByAppointment(appointmentId);
        if(prescriptions.isEmpty()){
            return new ResponseEntity<>("The appointment with that id does not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(prescriptions.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/patient/{ssNumber}")
    public ResponseEntity<?> getPrescriptionsByPatient(@PathVariable String ssNumber){
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByPatient(ssNumber);
        return new ResponseEntity<>(prescriptions, HttpStatus.OK);
    }

    @GetMapping(value = "/stats/top-medicines")
    public ResponseEntity<?> getTopMedicines(){
        List<Object[]> topMedicines = prescriptionService.getTopMedicines();
        return new ResponseEntity<>(topMedicines, HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updatePrescription(@PathVariable UUID id, @RequestBody Prescription prescription){
        String returnedMessage = prescriptionService.updatePrescription(id, prescription);

        if(returnedMessage.equals("Prescription updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePrescription(@PathVariable UUID id){
        String returnedMessage = prescriptionService.deletePrescription(id);

        if(returnedMessage.equals("Prescription deleted successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }
        return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
    }
}