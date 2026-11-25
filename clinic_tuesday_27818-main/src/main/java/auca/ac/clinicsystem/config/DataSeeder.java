package auca.ac.clinicsystem.config;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import auca.ac.clinicsystem.domain.Appointment;
import auca.ac.clinicsystem.domain.AppointmentStatus;
import auca.ac.clinicsystem.domain.Doctor;
import auca.ac.clinicsystem.domain.Office;
import auca.ac.clinicsystem.domain.Patient;
import auca.ac.clinicsystem.domain.Specialization;
import auca.ac.clinicsystem.repository.AppointmentRepository;
import auca.ac.clinicsystem.repository.DoctorRepository;
import auca.ac.clinicsystem.repository.OfficeRepository;
import auca.ac.clinicsystem.repository.PatientRepository;
import auca.ac.clinicsystem.repository.SpecializationRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SpecializationRepository specializationRepository;
    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    public DataSeeder(SpecializationRepository specializationRepository, OfficeRepository officeRepository,
            DoctorRepository doctorRepository, PatientRepository patientRepository,
            AppointmentRepository appointmentRepository) {
        this.specializationRepository = specializationRepository;
        this.officeRepository = officeRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        if (specializationRepository.count() > 0) {
            prepareQuiz2Data();
            return;
        }

        Specialization cardiology = new Specialization();
        cardiology.setName("Cardiology");
        Specialization pediatrics = new Specialization();
        pediatrics.setName("Pediatrics");
        Specialization dermatology = new Specialization();
        dermatology.setName("Dermatology");
        Specialization neurology = new Specialization();
        neurology.setName("Neurology");

        cardiology = specializationRepository.save(cardiology);
        pediatrics = specializationRepository.save(pediatrics);
        dermatology = specializationRepository.save(dermatology);
        neurology = specializationRepository.save(neurology);

        Office office101 = new Office();
        office101.setName("Clinic 101");
        office101.setOfficeNumber(101);
        Office office102 = new Office();
        office102.setName("Clinic 102");
        office102.setOfficeNumber(102);
        Office office103 = new Office();
        office103.setName("Clinic 103");
        office103.setOfficeNumber(103);

        office101 = officeRepository.save(office101);
        office102 = officeRepository.save(office102);
        office103 = officeRepository.save(office103);

        Doctor mugabo = new Doctor();
        mugabo.setFirstName("Jean");
        mugabo.setLastName("Mugabo");
        mugabo.setDateOfBirth(LocalDate.of(1985, 5, 10));
        mugabo.setOffice(office101);
        Set<Specialization> mugaboSpecializations = new HashSet<>();
        mugaboSpecializations.add(cardiology);
        mugaboSpecializations.add(pediatrics);
        mugabo.setSpecializations(mugaboSpecializations);
        mugabo = doctorRepository.save(mugabo);

        Doctor ingabire = new Doctor();
        ingabire.setFirstName("Aline");
        ingabire.setLastName("Ingabire");
        ingabire.setDateOfBirth(LocalDate.of(1990, 8, 14));
        ingabire.setOffice(office102);
        Set<Specialization> ingabireSpecializations = new HashSet<>();
        ingabireSpecializations.add(dermatology);
        ingabire.setSpecializations(ingabireSpecializations);
        ingabire = doctorRepository.save(ingabire);

        Doctor habimana = new Doctor();
        habimana.setFirstName("Eric");
        habimana.setLastName("Habimana");
        habimana.setDateOfBirth(LocalDate.of(1988, 2, 22));
        Set<Specialization> habimanaSpecializations = new HashSet<>();
        habimanaSpecializations.add(pediatrics);
        habimana.setSpecializations(habimanaSpecializations);
        habimana = doctorRepository.save(habimana);

        Patient alice = new Patient();
        alice.setFirstName("Alice");
        alice.setLastName("Uwase");
        alice.setDateOfBirth(LocalDate.of(1995, 1, 12));
        alice = patientRepository.save(alice);

        Patient brian = new Patient();
        brian.setFirstName("Brian");
        brian.setLastName("Uwase");
        brian.setDateOfBirth(LocalDate.of(1992, 7, 18));
        brian = patientRepository.save(brian);

        Patient claudine = new Patient();
        claudine.setFirstName("Claudine");
        claudine.setLastName("Mukamana");
        claudine.setDateOfBirth(LocalDate.of(1998, 9, 3));
        claudine = patientRepository.save(claudine);

        Patient david = new Patient();
        david.setFirstName("David");
        david.setLastName("Nkurunziza");
        david.setDateOfBirth(LocalDate.of(1987, 11, 25));
        david = patientRepository.save(david);

        Patient esther = new Patient();
        esther.setFirstName("Esther");
        esther.setLastName("Iradukunda");
        esther.setDateOfBirth(LocalDate.of(2000, 3, 8));
        esther = patientRepository.save(esther);

        Appointment appointment1 = new Appointment();
        appointment1.setAppointmentDate(LocalDate.of(2026, 10, 5));
        appointment1.setReason("Heart checkup");
        appointment1.setStatus(AppointmentStatus.COMPLETED);
        appointment1.setPatient(alice);
        appointment1.setDoctor(mugabo);
        appointmentRepository.save(appointment1);

        Appointment appointment2 = new Appointment();
        appointment2.setAppointmentDate(LocalDate.of(2026, 10, 20));
        appointment2.setReason("Blood pressure follow-up");
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setPatient(alice);
        appointment2.setDoctor(mugabo);
        appointmentRepository.save(appointment2);

        Appointment appointment3 = new Appointment();
        appointment3.setAppointmentDate(LocalDate.of(2026, 11, 3));
        appointment3.setReason("Skin rash");
        appointment3.setStatus(AppointmentStatus.CONFIRMED);
        appointment3.setPatient(alice);
        appointment3.setDoctor(ingabire);
        appointmentRepository.save(appointment3);

        Appointment appointment4 = new Appointment();
        appointment4.setAppointmentDate(LocalDate.of(2026, 10, 20));
        appointment4.setReason("Chest pain");
        appointment4.setStatus(AppointmentStatus.CONFIRMED);
        appointment4.setPatient(brian);
        appointment4.setDoctor(mugabo);
        appointmentRepository.save(appointment4);

        Appointment appointment5 = new Appointment();
        appointment5.setAppointmentDate(LocalDate.of(2026, 10, 12));
        appointment5.setReason("Child vaccination");
        appointment5.setStatus(AppointmentStatus.CANCELLED);
        appointment5.setPatient(brian);
        appointment5.setDoctor(habimana);
        appointmentRepository.save(appointment5);

        Appointment appointment6 = new Appointment();
        appointment6.setAppointmentDate(LocalDate.of(2026, 10, 25));
        appointment6.setReason("Acne treatment");
        appointment6.setStatus(AppointmentStatus.SCHEDULED);
        appointment6.setPatient(claudine);
        appointment6.setDoctor(ingabire);
        appointmentRepository.save(appointment6);

        Appointment appointment7 = new Appointment();
        appointment7.setAppointmentDate(LocalDate.of(2026, 10, 20));
        appointment7.setReason("ECG review");
        appointment7.setStatus(AppointmentStatus.COMPLETED);
        appointment7.setPatient(claudine);
        appointment7.setDoctor(mugabo);
        appointmentRepository.save(appointment7);

        Appointment appointment8 = new Appointment();
        appointment8.setAppointmentDate(LocalDate.of(2026, 11, 10));
        appointment8.setReason("Fever");
        appointment8.setStatus(AppointmentStatus.SCHEDULED);
        appointment8.setPatient(david);
        appointment8.setDoctor(habimana);
        appointmentRepository.save(appointment8);

        Appointment appointment9 = new Appointment();
        appointment9.setAppointmentDate(LocalDate.of(2026, 11, 15));
        appointment9.setReason("Allergy test");
        appointment9.setStatus(AppointmentStatus.COMPLETED);
        appointment9.setPatient(esther);
        appointment9.setDoctor(ingabire);
        appointmentRepository.save(appointment9);

        Appointment appointment10 = new Appointment();
        appointment10.setAppointmentDate(LocalDate.of(2026, 10, 8));
        appointment10.setReason("Follow-up visit");
        appointment10.setStatus(AppointmentStatus.CANCELLED);
        appointment10.setPatient(david);
        appointment10.setDoctor(habimana);
        appointmentRepository.save(appointment10);

        prepareQuiz2Data();
    }

    private void prepareQuiz2Data() {
        LocalDate today = LocalDate.now();

        Office office103 = findOffice(103);
        if (office103 == null) {
            office103 = new Office();
            office103.setName("Clinic 103");
            office103.setOfficeNumber(103);
            office103 = officeRepository.save(office103);
        }

        if (findOffice(104) == null) {
            Office office104 = new Office();
            office104.setName("Clinic 104");
            office104.setOfficeNumber(104);
            officeRepository.save(office104);
        }

        Specialization cardiology = findSpecialization("Cardiology");
        Specialization pediatrics = findSpecialization("Pediatrics");
        Specialization dermatology = findSpecialization("Dermatology");

        Doctor mugabo = findDoctor("Jean", "Mugabo");
        if (mugabo != null && cardiology != null && pediatrics != null) {
            Set<Specialization> specializations = new HashSet<>();
            specializations.add(cardiology);
            specializations.add(pediatrics);
            mugabo.setSpecializations(specializations);
            doctorRepository.save(mugabo);
        }

        Doctor ingabire = findDoctor("Aline", "Ingabire");
        if (ingabire != null && dermatology != null) {
            Set<Specialization> specializations = new HashSet<>();
            specializations.add(dermatology);
            ingabire.setSpecializations(specializations);
            doctorRepository.save(ingabire);
        }

        Doctor habimana = findDoctor("Eric", "Habimana");
        if (habimana != null && pediatrics != null) {
            Set<Specialization> specializations = new HashSet<>();
            specializations.add(pediatrics);
            habimana.setOffice(office103);
            habimana.setSpecializations(specializations);
            doctorRepository.save(habimana);
        }

        Patient alice = null;
        for (Patient patient : patientRepository.findAll()) {
            if ("Alice".equals(patient.getFirstName()) && "Uwase".equals(patient.getLastName())) {
                patient.setSsNumber("SS-1001");
                alice = patient;
            } else if ("Brian".equals(patient.getFirstName()) && "Uwase".equals(patient.getLastName())) {
                patient.setSsNumber("SS-1002");
            } else if ("Claudine".equals(patient.getFirstName()) && "Mukamana".equals(patient.getLastName())) {
                patient.setSsNumber("SS-1003");
            } else if ("David".equals(patient.getFirstName()) && "Nkurunziza".equals(patient.getLastName())) {
                patient.setSsNumber("SS-1004");
            } else if ("Esther".equals(patient.getFirstName()) && "Iradukunda".equals(patient.getLastName())) {
                patient.setSsNumber("SS-1005");
            }
            patientRepository.save(patient);
        }

        boolean hasThirdFutureAppointment = false;
        for (Appointment appointment : appointmentRepository.findAll()) {
            if ("Blood pressure follow-up".equals(appointment.getReason())) {
                appointment.setAppointmentDate(today.plusDays(6));
                appointmentRepository.save(appointment);
            } else if ("Skin rash".equals(appointment.getReason())) {
                appointment.setAppointmentDate(today.plusDays(8));
                appointmentRepository.save(appointment);
            } else if ("Quiz 2 future appointment".equals(appointment.getReason())) {
                hasThirdFutureAppointment = true;
            }
        }

        if (alice != null && habimana != null && !hasThirdFutureAppointment) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(today.plusDays(16));
            appointment.setReason("Quiz 2 future appointment");
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setPatient(alice);
            appointment.setDoctor(habimana);
            appointmentRepository.save(appointment);
        }
    }

    private Office findOffice(int officeNumber) {
        for (Office office : officeRepository.findAll()) {
            if (office.getOfficeNumber() == officeNumber) {
                return office;
            }
        }
        return null;
    }

    private Specialization findSpecialization(String name) {
        for (Specialization specialization : specializationRepository.findAll()) {
            if (name.equals(specialization.getName())) {
                return specialization;
            }
        }
        return null;
    }

    private Doctor findDoctor(String firstName, String lastName) {
        for (Doctor doctor : doctorRepository.findByFirstName(firstName)) {
            if (lastName.equals(doctor.getLastName())) {
                return doctor;
            }
        }
        return null;
    }
}
