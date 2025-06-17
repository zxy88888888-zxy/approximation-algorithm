package com.order.predict.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.predict.domain.Orders;
import com.order.predict.mapper.OrderMapper;
import com.order.predict.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: TODO
 * @version: v1.0.0
 * @author: lidelin
 * @date: 2025/6/9 16:06
 */
@Service("orderServiceImpl")
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Override
    public List<Orders> selectBydataversion(Long dataversion) {
        return baseMapper.selectBydataversion(dataversion);
    }
}

