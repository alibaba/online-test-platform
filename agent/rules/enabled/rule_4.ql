[name]
失败demo: 返回结果中isRadar不为1时失败

[level]
1

[category]
字段取值验证

[type]
steed_data

[when]
type == "steed_data";

[verify]
isRadar = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("isRadar");

if (isRadar != "1"){
  记录错误("返回结果中的isRadar不为1");
  return false;
}
return true;