package com.order.predict.domain;


import lombok.Data;

/**
 * 订单对象 orders
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Data
public class Orders
{
    private static final long serialVersionUID = 1L;

    /** 订单id */
    private Long orderId;

    private  String dataversion;

    /** 客户id */
//    private Long customerId;

    /** 咖啡杯数量 */
    private Long coffeeCupNumber;

    /** 泡面碗数量 */
    private Long noodleBowlNumber;

    /** 餐盘数量 */
    private Long dinnerPlateNumber;

    /** 马克杯 */
    private Long mugNumber;

    /** 汤碗数量 */
    private Long soupBowlNumber;

    /** 甜品碟数量 */
    private Long desertPlateNumber;

    /** 保鲜碗数量 */
    private Long storageBowlNumber;

    /** 沙拉碗数量 */
    private Long saladBowlNumber;

    /** 调羹数量 */
    private Long spoonNumber;

    /** 陶瓷筷数量 */
    private Long ceramicChopsticksNumber;


}
