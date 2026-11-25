package auca.ac.clinicsystem.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import auca.ac.clinicsystem.domain.Office;
import auca.ac.clinicsystem.repository.OfficeRepository;

@Service
public class OfficeService {

    private final OfficeRepository officeRepository;

    public OfficeService(OfficeRepository officeRepository) {
        this.officeRepository = officeRepository;
    }

    public String saveOffice(Office office) {
        officeRepository.save(office);
        return "Office is saved successfully";
    }

    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }

    public List<Office> getFreeOffices() {
        return officeRepository.findFreeOffices();
    }

    public List<Object[]> getBusiestOffice() {
        List<Object[]> result = officeRepository.findBusiestOffice();
        if (result.isEmpty()) {
            return result;
        }
        return result.subList(0, 1);
    }

    public Optional<Office> getOfficeById(UUID id) {
        return officeRepository.findById(id);
    }

    public String updateOffice(UUID id, Office office) {
        Optional<Office> existingOffice = officeRepository.findById(id);

        if (existingOffice.isEmpty()) {
            return "Office not found";
        }

        Office officeToUpdate = existingOffice.get();
        officeToUpdate.setName(office.getName());
        officeToUpdate.setOfficeNumber(office.getOfficeNumber());

        officeRepository.save(officeToUpdate);
        return "Office is updated successfully";
    }

    public String deleteOffice(UUID id) {
        if (!officeRepository.existsById(id)) {
            return "Office not found";
        }
        officeRepository.deleteById(id);
        return "Office is deleted successfully";
    }

    public String deleteOfficeByNumber(int officeNumber){
    Optional<Office> office = officeRepository.findByOfficeNumber(officeNumber);

    if(office.isEmpty()){
        return "Office not found";
    }

    officeRepository.delete(office.get());
    return "Office is deleted successfully";
}
}
