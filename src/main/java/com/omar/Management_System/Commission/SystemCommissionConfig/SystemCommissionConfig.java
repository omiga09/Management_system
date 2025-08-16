package com.omar.Management_System.Commission.SystemCommissionConfig;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "system_commission_config")
public class SystemCommissionConfig {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name = "DEFAULT";

        private double rate;
    }


