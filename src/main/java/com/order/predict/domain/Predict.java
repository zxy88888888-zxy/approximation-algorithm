package com.order.predict.domain;


import lombok.Data;

/**
 * 排单预测对象 predict
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Data
public class Predict
{
    private static final long serialVersionUID = 1L;

    /** 预测id */
    private Long id;

    /** 预测描述 */
    private String predictDecribe;

    /** 订单id集合 */
    private String orderIds;

    /** 排单顺序 */
    private String predictResultSequence;

    /** 预测完成结果 */
    private String resutl;

    private String creatTime;

    private String dataversion;



}
