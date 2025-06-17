package com.order.predict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.order.predict.domain.Orders;

import java.util.List;

public interface OrderService extends IService<Orders> {
    List<Orders> selectBydataversion(Long dataversion);
}
