package com.omar.Management_System.Reservation.ReservationDto;

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
        private String reservationCode;
        private LocalDate reservationDate;
        private String status;
        private Long propertyId;
        private Long buyerId;

        public ReservationDto(Long reservationId,
                              LocalDate reservationDate,
                              String status,
                              Long propertyId,
                              Long buyerId) {
                this.reservationId = reservationId;
                this.reservationDate = reservationDate;
                this.status = status;
                this.propertyId = propertyId;
                this.buyerId = buyerId;
        }
}
