package com.order.predict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.order.predict.domain.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
    @Select("SELECT * FROM orders WHERE dataversion = #{dataversion}")
    List<Orders> selectBydataversion(Long dataversion);
}
