# 概述

<img src="https://github.com/alibaba/online-test-platform/raw/master/logo.png" width="30%" height="30%"></img>

MagicOTP (Online Test Platform)是一个开源的线上测试平台，思想是通过回放大批量线上真实请求，并结合规则验证的形式对服务返回的结果进行校验。相对于传统测试用例设计中固定的输入和输出数据方式，更能提高数据的覆盖度，对系统稳定性监测提供有力的保障。请求数据来源于线上服务真实请求的收集，验证规则又不依赖于具体请求数据，大大节省了开发和维护的成本。依托此线上测试平台，您只需要根据业务需求梳理添加功能点的校验规则，即可完成接入，快速完成对线上服务的业务逻辑验证工作。

# 主要功能

- 线上请求回放

    线上测试 采集线上请求后通过平台回放请求的方式进行功能逻辑的验证。支持多应用的接入，分别配置不同的请求文件和校验规则。提交任务时指定测试地址、选取测试数据后任务即可执行，可以通过WEBUI查看历史测试任务的结果和详细信息。

- 规则引擎
    
    规则校验 通过脚本对业务功能逻辑进行规则编写，根据不同的应用名称选取相应规则在运行测试任务时进行规则校验，实现验证功能点和数据的匹配。规则还有准入条件的筛选流程，满足预设条件的数据才进行规则验证。通过脚本的方式实时修改免编译发布，编写方便维护成本低。

![center|image](https://github.com/alibaba/online-test-platform/raw/master/platform.png)
    
# 教程
- [基础教程](https://github.com/alibaba/online-test-platform/blob/master/example/README.md)
    

# 快速开始

创建数据库

`create database steed`

`source deployment/database.sql`

修改配置文件中的ip

```
   1. platform/src/main/resources/application.properties
      spring.datasource.url=jdbc:mysql://ip:3306/steed?useSSL=false
      spring.datasource.username=****
      spring.datasource.password=******
      #规则引擎地址
      rule.address=http://ip:9191
```

```
   2. agent/src/main/resources/application.properties
      spring.redis.host=ip
```

```
   3. agent/src/main/resources/applicationContext.xml
      <property name="url" value="jdbc:mysql://ip:3306/steed"/>
      <property name="username" value="****"/>
      <property name="password" value="******"/>
```

进入agent目录，启动agent

`mvn clean package`

`java -jar target/RuleEngine.jar`

进入platform，启动platform，完成后即可通过配置的端口号访问（默认访问地址为: ip:9100）

`mvn clean package`

`java -jar target/MagicOTP-1.0-SNAPSHOT.jar`

若需要进行前端页面的修改，进入xuanwu-op/platform/frontend编辑后，执行以下命令重新部署即可。

`tnpm install`

`tnpm start`

`tnpm run build`

在编写统计类规则时，数据存储在redis中，需要安装部署redis数据库。


# 案例演示
   MagicOTP以中国气象局查询天气服务为例，提供了一个通过平台进行线上测试的任务样例。首先在本地创建需要回放的请求串列表文件，平台回放文件中的请求到指定的服务器，对返回的数据通过规则的形式进行功能验证，任务结束后可以查看测试任务执行的结果及详情信息。

- #### 测试数据文件准备

    测试数据文件准备。案例中使用测试数据文件(deployment/steed_data)，需拷贝该文件到配置的数据读取路径下(platform/src/main/resources/application.properties)。
    
    请求地址：
    
    http://www.weather.com.cn
    
    文件中不同的id代表不同的城市，通过GET请求会返回一个JSON格式的数据，包含该城市的具体天气信息：
    
    http://www.weather.com.cn/data/sk/101190101.html
    
    ```
        {
            "weatherinfo":{
                "city":"南京",
                "cityid":"101190101",
                "temp":"21.9",
                "WD":"东风",
                "WS":"小于3级",
                "SD":"96%",
                "AP":"1000.9hPa",
                "njd":"暂无实况",
                "WSE":"<3",
                "time":"17:55",
                "sm":"3.2",
                "isRadar":"1",
                "Radar":"JC_RADAR_AZ9250_JB"
            }
        }
    ```
        
- #### 规则示例
    MagicOTP通过获取每条请求返回的不同城市的天气数据，通过预先编写的规则来验证功能是否正确。案例中提供了五条参考规则进行验证。

  - 功能逻辑规则
   
    ```
        应用名称：steed_data
        规则名称：请求中的id与返回中cityid值一致
        规则分类：功能逻辑验证
        规则等级：高
        是否启用：是
        准入条件：返回结果jsonArr.size() > 0;
        规则内容：
            temp = 原始查询串.split("/");
            
            temp1 = temp[temp.length-1];
            
            req_cityid = temp1.split(".html")[0];
            
            res_cityid = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("cityid");
            if (req_cityid != res_cityid) {
                记录错误("返回中的cityid与请求中不一致");
                return false;
            }
            
            return true;
    ```
    
    ```
        应用名称：steed_data
        规则名称：返回结果中的时间满足正则
        规则分类：功能逻辑验证
        规则等级：高
        是否启用：是
        准入条件：返回结果jsonArr.size() > 0;
        规则内容：
            res_time = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("time");
            
            pattern = "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])(:([0-5]?[0-9]))?$";
            
            flag = matchPattern(res_time,pattern);
            
            if( flag == false){
                记录错误("返回时间格式异常");
                return false;
            }
            
            return true;
    ```
    
  - 统计类规则
  
    ```
        应用名称：steed_data
        规则名称：中国气象局查询天气服务结果统计空结果率正常
        规则分类：其他验证
        规则等级：高
        是否启用：是
        准入条件：(统计结果.get("total_query") != null) and (统计结果.get("total_query") >= 1)
        规则内容：
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
    ```

  - 失败规则示例
  
    ```
          应用名称：steed_data
          规则名称：失败demo: 返回结果中isRadar不为1时失败
          规则分类：其他验证
          规则等级：高
          是否启用：是
          准入条件：type == "steed_data";
          规则内容：
              isRadar = 返回结果jsonArr.getJSONObject(0).getJSONObject("weatherinfo").get("isRadar");
              
              if (isRadar != "1"){
                  记录错误("返回结果中的isRadar不为1");
                  return false;
              }
              return true;
    ```
  
  - 未命中规则示例
  
    ```
          应用名称：steed_data;
          规则名称：未命中demo:准入条件不满足
          规则分类：其他验证
          规则等级：高
          是否启用：是
          准入条件：type == "steed"
          规则内容：
              return true;
    ```
  
- #### 任务创建
    进入"任务管理"页面，点击新建，填写任务的基本信息如下，点击确定
    
    ```
    任务名称：中国气象局查询天气服务
    请求地址： http://www.weather.com.cn
    测试数据文件：steed_data
    ```

- #### 任务结果查看
    任务列表中即可查看任务详细信息，包含执行状态、执行结果等。

# 常见问题

- agent出现“java.lang.NoClassDefFoundError”错误。
   
   原因在于未修改规则引擎的数据库用户名、密码相关配置，修改后重新部署即可。
   
- MagicOTP前端页面中文乱码如何解决？
   
   数据库编码问题，请修改编码为utf-8.

# 后续开源计划

MagicOTP目前提供了基础的通过线上请求回放的方式进行功能逻辑的验证，后续开源计划如下:
1. 开源更加完善的线上测试解决方案；
2. 开源利用算法技术的规则智能化推荐生成。

# 联系我们

MagicOTP由阿里集团-新零售智能引擎事业群-广告产品技术事业部-技术质量-引擎&基础测试及平台团队荣誉出品，欢迎通过邮件组magicotp-opensource@list.alibaba-inc.com和github issue联系和反馈。


<img src="https://github.com/alibaba/online-test-platform/raw/master/dingtalk.png" width="30%" height="30%"></img>
