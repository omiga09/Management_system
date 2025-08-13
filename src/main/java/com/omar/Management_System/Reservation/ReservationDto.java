package com.omar.Management_System.Reservation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
        private Long reservationId;
        private LocalDate reservationDate;
        private String status;
        private Long propertyId;
        private Long buyerId;
    }


