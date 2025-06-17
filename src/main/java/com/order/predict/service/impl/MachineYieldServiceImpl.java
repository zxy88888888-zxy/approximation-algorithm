package com.order.predict.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.order.predict.domain.MachineYieldConfig;
import com.order.predict.mapper.MachineYieldMapper;
import com.order.predict.service.MachineYieldService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MachineYieldServiceImpl extends ServiceImpl<MachineYieldMapper, MachineYieldConfig> implements MachineYieldService {

    @Override
    public Map<String, Double> getYieldMap() {
        List<MachineYieldConfig> list = list();
        Map<String, Double> map = new HashMap<>();
        for (MachineYieldConfig config : list) {
            map.put(config.getItemCode(), config.getYieldPerHour());
        }
        return map;
    }

    @Override
    public boolean updateConfig(Long id, Double yieldPerHour, String unit) {
        MachineYieldConfig config = getById(id);
        if (config == null) {
            return false;
        }
        config.setYieldPerHour(yieldPerHour);
        config.setUnit(unit);
        return updateById(config);
    }

    @Override
    public List<MachineYieldConfig> listAll() {
        return list();
    }
}
