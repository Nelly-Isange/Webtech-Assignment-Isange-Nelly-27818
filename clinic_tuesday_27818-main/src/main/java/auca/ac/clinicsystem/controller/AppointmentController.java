package auca.ac.clinicsystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.service.AppointmentService;

@RestController
@RequestMapping (value="/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appServe;

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment appointment, @RequestParam UUID patientId, @RequestParam UUID doctorId){

       String returnedMessage =  appServe.saveAppointment(appointment, patientId, doctorId);

       if(returnedMessage.equals("Appointment is saved successfully")){
        return new ResponseEntity<>(returnedMessage,HttpStatus.OK);
       }else if(returnedMessage.equals("Patient not found") || returnedMessage.equals("Doctor not found")){
        return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
    }else if(returnedMessage.equals("Appointment date cannot be in the past")){
     return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
       }else{
        return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
       }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAppointments(){
        List<Appointment> appointments = appServe.getAllAppointments();
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/page")
    public ResponseEntity<Page<Appointment>> getAppointmentsPage(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "appointmentDate,desc") String sort){
        String[] sortParts = sort.split(",", 2);
        String sortProperty = sortParts[0];
        Sort.Direction sortDirection = sortParts.length > 1 ? Sort.Direction.fromString(sortParts[1]) : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty));
        Page<Appointment> appointments = appServe.getAppointmentsPage(pageable);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/by-status")
    public ResponseEntity<?> getAppointmentsByStatus(@RequestParam AppointmentStatus status){
        List<Appointment> appointments = appServe.getAppointmentsByStatus(status);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping(value = "/between")
    public ResponseEntity<?> getAppointmentsBetween(@RequestParam String start, @RequestParam String end){
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        List<Appointment> appointments = appServe.getAppointmentsBetween(startDate, endDate);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}/status")
    public ResponseEntity<?> updateAppointmentStatus(@PathVariable UUID id, @RequestParam AppointmentStatus status){
        String returnedMessage = appServe.updateAppointmentStatus(id, status);

        if(returnedMessage.equals("Appointment status updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("The appointment with that id does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(value = "/{id}/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@PathVariable UUID id, @RequestParam String newDate){
        LocalDate appointmentDate = LocalDate.parse(newDate);
        String returnedMessage = appServe.rescheduleAppointment(id, appointmentDate);

        if(returnedMessage.equals("Appointment rescheduled successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("The appointment with that id does not exist")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }else if(returnedMessage.equals("Doctor is already booked on that date")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/stats/by-status")
    public ResponseEntity<?> getAppointmentsByStatusStats(){
        List<Object[]> stats = appServe.getAppointmentsByStatusStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PatchMapping(value = "/cancel-day")
    public ResponseEntity<?> cancelAppointmentsForDoctorOnDate(@RequestParam UUID doctorId, @RequestParam String date){
        LocalDate appointmentDate = LocalDate.parse(date);
        String returnedMessage = appServe.cancelAppointmentsForDoctorOnDate(doctorId, appointmentDate);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cancelled-before")
    public ResponseEntity<?> deleteCancelledAppointmentsBefore(@RequestParam String date){
        LocalDate appointmentDate = LocalDate.parse(date);
        String returnedMessage = appServe.deleteCancelledAppointmentsBefore(appointmentDate);
        return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable UUID id){
        Optional<Appointment> appointment = appServe.getAppointmentById(id);

        if(appointment.isPresent()){
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Appointment not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable UUID id, @RequestBody Appointment appointment){
        String returnedMessage = appServe.updateAppointment(id, appointment);

        if(returnedMessage.equals("Appointment is updated successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable UUID id){
        String returnedMessage = appServe.deleteAppointment(id);

        if(returnedMessage.equals("Appointment is deleted successfully")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.OK);
        }else if(returnedMessage.equals("That appointment has prescriptions, delete them first")){
            return new ResponseEntity<>(returnedMessage, HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(returnedMessage, HttpStatus.NOT_FOUND);
        }
    }
}