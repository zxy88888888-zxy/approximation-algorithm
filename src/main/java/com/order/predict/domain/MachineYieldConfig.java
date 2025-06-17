package com.order.predict.domain;

import lombok.Data;

@Data
public class MachineYieldConfig {
    private Long id;
    private String itemName;
    private String itemCode;
    private Double yieldPerHour;
    private String unit;
}
