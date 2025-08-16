package com.omar.Management_System.Reservation.ReservationMapper;

import com.omar.Management_System.Reservation.Reservation.Reservation;
import com.omar.Management_System.Reservation.ReservationDto.ReservationDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationMapper {
        public static ReservationDto toDto(Reservation reservation) {
            ReservationDto dto = new ReservationDto();
            dto.setReservationId(reservation.getReservationId());
            dto.setReservationCode(reservation.getReservationCode());
            dto.setPropertyId(reservation.getProperty().getPropertyId());
            dto.setBuyerId(reservation.getBuyer().getId());
            return dto;
        }
    }


