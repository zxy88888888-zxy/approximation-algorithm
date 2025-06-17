package com.order.predict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.order.predict.domain.Predict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 排单预测Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Mapper
public interface PredictMapper extends BaseMapper<Predict>
{
    List<Predict> selectAllPredictOrderByCreateTime();
}
