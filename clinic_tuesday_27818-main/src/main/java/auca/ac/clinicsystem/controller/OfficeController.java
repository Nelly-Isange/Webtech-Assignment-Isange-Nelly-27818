package auca.ac.clinicsystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auca.ac.clinicsystem.domain.Office;
import auca.ac.clinicsystem.service.OfficeService;

@RestController
@RequestMapping (value="/api/offices")
@CrossOrigin ("*")
public class OfficeController {

    @Autowired
    private OfficeService offServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveOffice(@RequestBody Office office){

       String returnedMessage =  offServe.saveOffice(office);

       if(returnedMessage.equals("Office is saved successfully")){
        return new ResponseEntity<>(returnedMessage,HttpStatus.OK);
       }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
       }

    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllOffices(){
        List<Office> offices = offServe.getAllOffices();
        return new ResponseEntity<>(offices, HttpStatus.OK);
    }

    @GetMapping(value = "/free")
    public ResponseEntity<?> getFreeOffices(){
        List<Office> offices = offServe.getFreeOffices();
        return new ResponseEntity<>(offices, HttpStatus.OK);
    }

    @GetMapping(value = "/busiest")
    public ResponseEntity<?> getBusiestOffice(){
        List<Object[]> busiestOffice = offServe.getBusiestOffice();
        if(busiestOffice.isEmpty()){
            return new ResponseEntity<>("No appointments yet", HttpStatus.OK);
        }
        return new ResponseEntity<>(busiestOffice, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOfficeById(@PathVariable UUID id){
        Optional<Office> office = offServe.getOfficeById(id);

        if(office.isPresent()){
            return new ResponseEntity<>(office.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Office not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateOffice(@PathVariable UUID id, @RequestBody Office office){
        String returnedMessage = offServe.updateOffice(id, office);

        if(returnedMessage.equals("Office is updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteOffice(@PathVariable UUID id){
        String returnedMessage = offServe.deleteOffice(id);

        if(returnedMessage.equals("Office is deleted successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/deleteByNumber/{officeNumber}")
public ResponseEntity<?> deleteOfficeByNumber(@PathVariable int officeNumber){
    String returnedMessage = offServe.deleteOfficeByNumber(officeNumber);

    if(returnedMessage.equals("Office is deleted successfully")){
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
    }
    
}
}