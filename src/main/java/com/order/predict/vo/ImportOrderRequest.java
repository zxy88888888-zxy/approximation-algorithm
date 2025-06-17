package com.order.predict.vo;

import com.order.predict.domain.Orders;
import lombok.Data;

import java.util.List;

@Data
public class ImportOrderRequest {
    private Long predictId;
    private List<Orders> orderList;
}
