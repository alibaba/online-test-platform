package com.taobao.qa.ruleengine.mapper;

import com.taobao.qa.ruleengine.model.RuleCoverageEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface RuleCoverageMapper {

    @Insert("insert into rule_coverage(`taskid`,`realtime_cov`,`static_total_cov`,`static_cansmoke_cov`,`timestamp`) values(#{taskid},#{realtime_cov},#{static_total_cov},#{static_cansmoke_cov},#{timestamp})")
    public int add(RuleCoverageEntity entity);

    @Select("select * from rule_coverage")
    public List<RuleCoverageEntity> getAllData();

    @Select("select * from rule_coverage where timestamp >= #{startTime} and timestamp <= #{endTime}")
    public List<RuleCoverageEntity> getDataInTimeGap(@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("select * from rule_coverage where taskid=#{taskid}")
    public RuleCoverageEntity getCoverageByTaskid(@Param("taskid") Integer taskid);
}
