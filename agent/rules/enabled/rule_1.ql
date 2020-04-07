[name]
中国气象局查询天气服务结果统计空结果率正常

[level]
1

[category]
其他验证

[type]
steed_data

[when]
(统计结果.get("total_query") != null) and (统计结果.get("total_query") >= 1)

[verify]
total = 统计结果.get("total_query");
emptys = 统计结果.get("empty_return");
if(emptys == null){
     return true;
}
if( total== null || total == 0){
     记录错误(format("统计数据异常，total_query为0或者不存在"));
     return false;
}
emptys = emptys * 100;
ratio = emptys/total;
if(ratio > 80){
     记录错误(format("空结果率为 %s，不在[0,80]范围内",ratio));
     return false;
}
return true;