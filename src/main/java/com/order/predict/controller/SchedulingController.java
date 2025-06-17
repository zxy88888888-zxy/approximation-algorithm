package com.order.predict.controller;


import com.order.predict.domain.OrderData;
import com.order.predict.domain.ResultDTO;
import com.order.predict.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedule")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    @PostMapping("/compute")
    public ResultDTO compute(@RequestBody OrderData data) {
        return schedulingService.run158Algorithm(data);
    }
}

