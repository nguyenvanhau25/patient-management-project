package com.pm.pharmacyservice.application.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MedicineResponseDTO {
    private String id;
    private String name;
    private String manufacturer;
    private BigDecimal price;
    private Integer quantity;
}
