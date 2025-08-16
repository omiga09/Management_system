package com.omar.Management_System.Maintenance.MaintenanceDto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class MaintenanceDto {
        private Long maintenanceId;
        private String description;
        private Double cost;
        private String contractorName;
        private LocalDate requestDate;
        private LocalDate completionDate;
        private String status;
        private Long propertyId;

        public MaintenanceDto() {

        }

        public MaintenanceDto(Long maintenanceId, String description, Double cost, String contractorName,
                              LocalDate requestDate, LocalDate completionDate, String status, Long propertyId) {
                this.maintenanceId = maintenanceId;
                this.description = description;
                this.cost = cost;
                this.contractorName = contractorName;
                this.requestDate = requestDate;
                this.completionDate = completionDate;
                this.status = status;
                this.propertyId = propertyId;
        }
}
