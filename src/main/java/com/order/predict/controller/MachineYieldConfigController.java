package com.order.predict.controller;

import com.order.predict.domain.MachineYieldConfig;
import com.order.predict.service.MachineYieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/machine_yield_config")
public class MachineYieldConfigController {
    @Autowired
    private MachineYieldService service;

    @GetMapping("/list")
    public List<MachineYieldConfig> list() {
        return service.listAll();
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody MachineYieldConfig config) {
        boolean success = service.updateConfig(config.getId(), config.getYieldPerHour(), config.getUnit());
        return Collections.singletonMap("success", success);
    }
}
