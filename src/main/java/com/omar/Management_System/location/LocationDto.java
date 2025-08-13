package com.omar.Management_System.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
        private String country;
        private String city;
        private String address;
        private double lat;
        private double lng;

        public LocationDto(String country, String city, String address, double lat, double lng) {
            this.country = country;
            this.city = city;
            this.address = address;
            this.lat = lat;
            this.lng = lng;
        }
    }


