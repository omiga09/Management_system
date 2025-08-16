package com.omar.Management_System.Report.ReportController;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyPerformanceDTO {
    private Long propertyId;
    private String title;
    private int views;
    private int inquiries;
    private int sales;
}
