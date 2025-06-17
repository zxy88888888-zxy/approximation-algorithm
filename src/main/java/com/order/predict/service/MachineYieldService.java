package com.order.predict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.order.predict.domain.MachineYieldConfig;

import java.util.List;
import java.util.Map;

public interface MachineYieldService extends IService<MachineYieldConfig> {
    Map<String, Double> getYieldMap();

    boolean updateConfig(Long id, Double yieldPerHour, String unit);

    List<MachineYieldConfig> listAll();
}
