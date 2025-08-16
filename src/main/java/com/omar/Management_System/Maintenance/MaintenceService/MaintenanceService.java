package com.omar.Management_System.Maintenance.MaintenceService;

import com.omar.Management_System.Maintenance.Maintenance.Maintenance;
import com.omar.Management_System.Maintenance.MaintenanceDto.MaintenanceDto;
import com.omar.Management_System.Maintenance.MaintenanceRepository.MaintenanceRepository;
import com.omar.Management_System.Property.Property.Property;
import com.omar.Management_System.Property.PropertyRepository.PropertyRepository;
import com.omar.Management_System.enums.MaintenanceStatus.MaintenanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private PropertyRepository propertyRepository;

    public Maintenance createMaintenance(MaintenanceDto dto) {
        Maintenance maintenance = new Maintenance();
        maintenance.setDescription(dto.getDescription());
        maintenance.setCost(dto.getCost());
        maintenance.setContractorName(dto.getContractorName());
        maintenance.setRequestDate(dto.getRequestDate() != null ? dto.getRequestDate() : LocalDate.now());

        // Fetch property from DB
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));
        maintenance.setProperty(property);

        maintenance.setStatus(MaintenanceStatus.PENDING);

        return maintenanceRepository.save(maintenance);
    }

    public List<MaintenanceDto> getMaintenanceByProperty(Long propertyId) {
        List<Maintenance> maintenances = maintenanceRepository.findByProperty_PropertyId(propertyId);
        return maintenances.stream()
                .map(m -> new MaintenanceDto(
                        m.getMaintenanceId(),
                        m.getDescription(),
                        m.getCost(),
                        m.getContractorName(),
                        m.getRequestDate(),
                        m.getCompletionDate(),
                        m.getStatus().name(),
                        m.getProperty().getPropertyId()
                ))
                .collect(Collectors.toList());
    }

    public Maintenance updateStatus(Long id, String newStatus) {
        Maintenance maintenance = maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        MaintenanceStatus statusEnum = MaintenanceStatus.valueOf(newStatus.toUpperCase());
        maintenance.setStatus(statusEnum);

        if (statusEnum == MaintenanceStatus.COMPLETED) {
            maintenance.setCompletionDate(LocalDate.now());
        }

        return maintenanceRepository.save(maintenance);
    }

    public void deleteMaintenance(Long id) {
        maintenanceRepository.deleteById(id);
    }
}
