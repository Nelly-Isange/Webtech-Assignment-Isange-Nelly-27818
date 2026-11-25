package auca.ac.clinicsystem.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.DoctorRepository;
import auca.ac.clinicsystem.repository.PatientRepository;
import auca.ac.clinicsystem.repository.PrescriptionRepository;

@Service
public class DashboardService {

    @Autowired
    private PatientRepository patientRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private PrescriptionRepository prescriptionRepo;

    public Map<String, Long> getSummary(){
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("patients", patientRepo.count());
        summary.put("doctors", doctorRepo.count());
        summary.put("freeOffices", (long) officeService.getFreeOffices().size());
        summary.put("appointmentsToday", appointmentRepo.countByAppointmentDate(LocalDate.now()));
        summary.put("scheduled", appointmentRepo.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepo.count());
        return summary;
    }
}