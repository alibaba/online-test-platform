[name]
返回结果中的时间满足正则

[level]
1

[category]
功能逻辑验证

[type]
steed_data

[when]
返回结果jsonArr.size() > 0;

[verify]
res_time = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("time");

pattern = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";

flag = matchPattern(res_time,pattern);

if( flag == false){
    记录错误("返回时间格式异常");
    return false;
}

return true;