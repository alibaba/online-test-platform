package com.taobao.qa.ruleengine.biz;

import com.taobao.qa.ruleengine.mapper.RuleCoverageMapper;
import com.taobao.qa.ruleengine.model.RuleCoverageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RuleCoverageService {
    @Autowired
    public RuleCoverageMapper ruleMapper;

    public int add(RuleCoverageEntity entity){
        return ruleMapper.add(entity);
    }

    public List<RuleCoverageEntity> getAllData(){
        return ruleMapper.getAllData();
    }

    public List<RuleCoverageEntity> getDataInTimeGap(String startTime,String endTime){
        return ruleMapper.getDataInTimeGap(startTime,endTime);
    }

    public RuleCoverageEntity getCoverageByTaskid(Integer taskid){
        return ruleMapper.getCoverageByTaskid(taskid);
    }
}
