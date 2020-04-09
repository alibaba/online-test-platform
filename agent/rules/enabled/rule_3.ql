[name]
请求中的id与返回中cityid值一致

[level]
1

[category]
字段取值验证

[type]
steed_data

[when]
返回结果jsonArr.size() > 0;

[verify]
temp = 原始查询串.split("/");

temp1 = temp[temp.length-1];

req_cityid = temp1.split(".html")[0];


res_cityid = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("cityid");
if (req_cityid != res_cityid) {
    记录错误("返回中的cityid与请求中不一致");
    return false;
}

return true;