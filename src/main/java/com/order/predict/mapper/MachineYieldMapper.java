package com.order.predict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.order.predict.domain.MachineYieldConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MachineYieldMapper extends BaseMapper<MachineYieldConfig> {
    @Select("SELECT * FROM machine_yield_config")
    List<MachineYieldConfig> selectAll();
//
//    @Update("UPDATE machine_yield_config SET yield_per_hour = #{yieldPerHour}, unit = #{unit} WHERE id = #{id}")
//    int updateConfig(@Param("id") Long id, @Param("yieldPerHour") Double yieldPerHour, @Param("unit") String unit);
}


