package com.order.predict.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.order.predict.domain.OrderData;
import com.order.predict.domain.Orders;
import com.order.predict.domain.Predict;
import com.order.predict.domain.ResultDTO;
import com.order.predict.mapper.OrderMapper;
import com.order.predict.mapper.PredictMapper;
import com.order.predict.service.MachineYieldService;
import com.order.predict.service.OrderService;
import com.order.predict.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predict")
public class PredictController {

    @Autowired
    private PredictMapper predictMapper;

    @Autowired
    private SchedulingService schedulingService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MachineYieldService machineYieldService;

    @PostMapping("/add")
    public Map<String, Object> addPredict(@RequestBody Map<String, String> request) {
        String description = request.get("description");

        Map<String, Object> result = new HashMap<>();
        if (description == null || description.trim().isEmpty()) {
            result.put("success", false);
            result.put("message", "预测描述不能为空");
            return result;
        }


        Predict predict = new Predict();
        predict.setPredictDecribe(description);
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        predict.setCreatTime(createTime);
        String dataversion = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        predict.setDataversion(dataversion);
        predictMapper.insert(predict);
        result.put("success", true);
        return result;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Predict>> listPredict() {
        List<Predict> creat_time_list = predictMapper.selectList(new QueryWrapper<Predict>().orderByDesc("dataversion"));

        return ResponseEntity.ok(creat_time_list);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importOrders(@RequestBody Map<String, Object> body) {
        Long predictId = ((Number) body.get("predictId")).longValue();
        List<Map<String, Object>> rawList = (List<Map<String, Object>>) body.get("orderList");

        if (predictId == null || rawList == null || rawList.isEmpty()) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 400);
            result.put("message", "缺少参数");
            return ResponseEntity.badRequest().body(result);
        }
        Predict predict = predictMapper.selectById(predictId);
        String dataversion = predict.getDataversion();

        List<Orders> mappedOrders = new ArrayList<>();
        for (Map<String, Object> row : rawList) {
            Orders order = new Orders();
            try {
                order.setOrderId(parseLong(row.get("订单id")));
                order.setDataversion(dataversion);
                order.setCoffeeCupNumber(parseLong(row.get("咖啡杯数量")));
                order.setNoodleBowlNumber(parseLong(row.get("泡面碗数量")));
                order.setDinnerPlateNumber(parseLong(row.get("餐盘数量")));
                order.setMugNumber(parseLong(row.get("马克杯")));
                order.setSoupBowlNumber(parseLong(row.get("汤碗数量")));
                order.setDesertPlateNumber(parseLong(row.get("甜品碟数量")));
                order.setStorageBowlNumber(parseLong(row.get("保鲜碗数量")));
                order.setSaladBowlNumber(parseLong(row.get("沙拉碗数量")));
                order.setSpoonNumber(parseLong(row.get("调羹数量")));
                order.setCeramicChopsticksNumber(parseLong(row.get("陶瓷筷数量")));
                mappedOrders.add(order);
            } catch (Exception e) {
                Map<String, Object> result = new HashMap<>();
                result.put("code", 400);
                result.put("message", "数据格式错误：" + e.getMessage());
                return ResponseEntity.badRequest().body(result);

            }
        }
        String collect = mappedOrders.stream().map(item->item.getOrderId().toString()).collect(Collectors.joining(","));
        predict.setOrderIds(collect);
        predictMapper.updateById(predict);

        System.out.println("预测ID: " + predictId + "，接收订单数量：" + mappedOrders.size());

        orderService.saveBatch(mappedOrders);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "导入成功");
        return ResponseEntity.ok(result);

    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startPrediction(@RequestBody Map<String, Object> body) {
        Map<String, Object> result = new HashMap<>();

        Object predictIdObj = body.get("predictId");
        Object dataversionObj = body.get("dataversion");
        if (predictIdObj == null) {
            result.put("code", 400);
            result.put("message", "缺少 predictId");
            return ResponseEntity.badRequest().body(result);
        }

        Long predictId = Long.parseLong(predictIdObj.toString());
        Long dataversion = Long.parseLong(dataversionObj.toString());

        //根据dataversion找订单表的
        OrderData orderData = new OrderData();
        List<Orders> ordersList=orderService.selectBydataversion(dataversion);
        Map<String, Double> yieldMap = machineYieldService.getYieldMap();

        int n = ordersList.size();
        int m = 10;
        orderData.setM(m);
        orderData.setN(n);

        double[][] p = new double[n][m+1];

        for (int i = 0; i < n; i++) {
            Orders order = ordersList.get(i);
            p[i][0] = order.getOrderId();
            p[i][1] = order.getCoffeeCupNumber()    / yieldMap.getOrDefault("coffee", 1.0);
            p[i][2] = order.getNoodleBowlNumber()   / yieldMap.getOrDefault("noodle", 1.0);
            p[i][3] = order.getDinnerPlateNumber()  / yieldMap.getOrDefault("plate", 1.0);
            p[i][4] = order.getMugNumber()          / yieldMap.getOrDefault("mug", 1.0);
            p[i][5] = order.getSoupBowlNumber()     / yieldMap.getOrDefault("soup", 1.0);
            p[i][6] = order.getDesertPlateNumber()  / yieldMap.getOrDefault("desert", 1.0);
            p[i][7] = order.getStorageBowlNumber()  / yieldMap.getOrDefault("storage", 1.0);
            p[i][8] = order.getSaladBowlNumber()    / yieldMap.getOrDefault("salad", 1.0);
            p[i][9] = order.getSpoonNumber()        / yieldMap.getOrDefault("spoon", 1.0);
            p[i][10] = order.getCeramicChopsticksNumber() / yieldMap.getOrDefault("chopstick", 1.0);
        }

        orderData.setP(p);

        double[][] d = new double[n][2];
        double[] totalTime = new double[n];
        double minTime = Double.MAX_VALUE;
        double maxTime = Double.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            // 每个订单在所有机器上的总加工时间
            totalTime[i] = Arrays.stream(p[i], 1, m + 1).sum();

            minTime = Math.min(minTime, totalTime[i]);
            maxTime = Math.max(maxTime, totalTime[i]);
        }

        for (int i = 0; i < n; i++) {
            d[i][0] = p[i][0]; // 订单ID

            // 归一化负载 [0, 1]
            double loadRatio = (totalTime[i] - minTime) / (maxTime - minTime + 1e-6);

            // 最小交货时间 = ceil(订单最短处理时间 / m)，防止压缩过度
            double baseTime = totalTime[i] / m;

            // 缓冲天数范围设定 [0.5天 ~ 3天]（具体可调）
            double buffer = 0.5 + loadRatio * 2.5;

            d[i][1] = Math.ceil(baseTime + buffer); // 交货时间天数，向上取整
        }
        orderData.setD(d);


        double[][] Pi = new double[n][2];

        for (int i = 0; i < n; i++) {
            Pi[i][0] = p[i][0]; // 订单ID

            // 计算总打包时间
            double totalTime2 = 0.0;
            for (int j = 1; j <= 10; j++) {
                totalTime2 += p[i][j];
            }

            // 基础惩罚项：平均打包时间
            double basePenalty = totalTime2 / 10.0;

            // 非线性复杂度惩罚
            double complexityPenalty = Math.pow(totalTime2, 1.2);

            // 可选扰动项（模拟小幅浮动）
            double randomNoise = Math.random() * 5.0;

            // 最终惩罚费用
            Pi[i][1] = basePenalty + complexityPenalty + randomNoise;
        }

        orderData.setPi(Pi);

        ResultDTO resultDTO=schedulingService.run158Algorithm(orderData);

        Set<Long> bestA=resultDTO.getBestA();
        Set<Long> bestR=resultDTO.getBestR();




        // 模拟预测结果
        Set<Long> predictionResult = bestA;
        Set<Long> predictionSequence = bestR;

        String predictResultSequence = predictionResult.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));


        Predict predict = new Predict();
        predict.setId(predictId);
        predict.setPredictResultSequence(predictResultSequence);



        predict.setResutl("预测成功");
        // 保存预测结果（实际项目应写入数据库）
         predictMapper.updateById(predict);

        result.put("code", 200);
        result.put("message", "预测成功");
        result.put("predictId", predictId);
        result.put("predictResultSequence", predictionSequence); // 排单顺序
        result.put("resutl", predictionResult); // 注意 key 是 resutl，保持一致
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getPredictDetail(@PathVariable Long id) {
        // 查询订单数据（假设 predictId 与 Orders 中的 dataversion 对应）
        Predict predict = predictMapper.selectById(id);
        if (predict == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("code", 400);
            result.put("message", "预测任务不存在");
            return ResponseEntity.badRequest().body(result);
        }

        String dataversion = predict.getDataversion();
        List<Orders> orders = orderService.lambdaQuery()
                .eq(Orders::getDataversion, dataversion)
                .list();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Orders order : orders) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("订单id", order.getOrderId());
            map.put("咖啡杯数量", order.getCoffeeCupNumber());
            map.put("泡面碗数量", order.getNoodleBowlNumber());
            map.put("餐盘数量", order.getDinnerPlateNumber());
            map.put("马克杯", order.getMugNumber());
            map.put("汤碗数量", order.getSoupBowlNumber());
            map.put("甜品碟数量", order.getDesertPlateNumber());
            map.put("保鲜碗数量", order.getStorageBowlNumber());
            map.put("沙拉碗数量", order.getSaladBowlNumber());
            map.put("调羹数量", order.getSpoonNumber());
            map.put("陶瓷筷数量", order.getCeramicChopsticksNumber());

            resultList.add(map);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", resultList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deletePredict(@PathVariable Long id) {
        Predict predict = new Predict();
        predict.setId(id);
        predictMapper.deleteById(predict);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", "删除成功");
        return ResponseEntity.ok(result);
    }



    private Long parseLong(Object obj) {
        if (obj == null) return 0L;
        if (obj instanceof Number) return ((Number) obj).longValue();
        return Long.parseLong(obj.toString());
    }

}
