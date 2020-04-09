package com.taobao.qa.ruleengine.mapper;

import com.taobao.qa.ruleengine.model.RuleEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RuleMapper {

    @Insert("insert into t_rule_all (`name`,`tags`,`category`,`level`,`when`,`verify`,`then`,`enabled`,`retry`,`logic_id`,`author`,`smoketype`,`debugtype`,`product`,`isautogen`,`query`,`extrinfo`) " +
            "values(#{name},#{tags},#{category},#{level},#{when},#{verify},#{then},#{enabled},#{retry},#{logic_id},#{author},#{smoketype},#{debugtype},#{product},#{isautogen},#{query},#{extrinfo})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int addRule(RuleEntity ruleEntity);

    @Select("select * from ${table} where id=${id}")
    public RuleEntity getRuleEntitiesById(@Param("table")String table,@Param("id") int id);

    @Select("select * from ${table}")
    public List<RuleEntity> getAllRulesByTable(@Param("table")String table);

    @Select("select * from ${table} where product='${product}'")
    public List<RuleEntity> getAllRulesByProduct(@Param("table")String table,@Param("product")String product);

    @Select("select * from ${table} where product='${product}' and smoketype='${smoketype}'")
    public List<RuleEntity> getAllRulesBySubproduct(@Param("table")String table,@Param("smoketype")String smoketype);

    @Delete("delete from ${table} where id=${id}")
    public int deleteRuleById(@Param("table") String table,@Param("id") int id);

    public int enableRulesByIds(List<Integer> ids);

    public int disableRulesByIds(List<Integer> ids);

    public int update(RuleEntity ruleEntity);

    @Update("update ${table} set enabled=1 where id=${id}")
    public int enableRuleById(@Param("table") String table, @Param("id") int id);

    @Update("update ${table} set enabled=0 where id=${id}")
    public int disableRuleById(@Param("table") String table, @Param("id") int id);
}
