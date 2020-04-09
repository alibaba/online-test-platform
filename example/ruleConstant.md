## 现有规则常量说明

- 小于
    ```
    含义：java标准运算符 <
    示例：返回字段数量小于5
    ```
    
- 大于
    ```
    含义：java标准运算符 >
    示例：返回字段数量大于10
    ```
    
- 最小值约束
    ```
    含义：最小值判断，parameter >= min,则返回true，否则返回false
    示例：!最小值约束(parameter,0)
    ```

- 最大值约束
    ```
    含义：最大值判断，parameter <= max,则返回true，否则返回false
    示例：!最大值约束(parameter,10)
    ```
   
- 取值范围约束
    ```
    含义：取值范围判断。parameter在[min,max]范围内，返回true，否则返回false
    示例：!取值范围约束(parameter,0,20)
    ```
    
- 取值集合约束
    ```
    含义：取值是否属于特定集合。parameter取值在set集合内，则返回true，否则返回false
    示例：boolean flag = 取值范围约束(parameter,"0","1","2")
    ```
    
- isInteger
    ```
    含义：是否是整数。是返回true，否则返回false
    示例：if( isInteger(parameter) )
    ```
    
- 为非负整数
    ```
    含义：是否是非负整数。是，返回true，否则返回false
    示例：String str = "-123"; boolean flag = 为非负整数(str)
    ```
    
- isFloat
    ```
    含义：是否是浮点数。是，则返回true，否则返回false
    示例：if( isFloat(str) )
    ```
    
- isNumeric
    ```
    含义：是否是数值。是，则返回true，否则返回false
    示例：String str = "te347192734";boolen flag = isNumberic(str);
    ```
     
- matchPattern
    ```
    含义：判断是否符合正则表达式。符合则返回true，否则返回false
    示例：if(!matchPattern(real,"\\d+.\\d+%"))
    ```
    
- 为空
    ```
    含义：是否为空值。是，则返回true，否则返回false
    示例：if(!为空(parameter))
    ```
    
- 字符串比较
    ```
    含义：比较两个字符串是否相等。相等，则返回0，否则返回非0
    示例：if(字符串比较(expectedParameter,actualParameter)!=0)
    ```
    
- 字符串切分
    ```
    含义：字符串切分，同java标准split（）函数
    示例：Sring[] exptArr = 字符串切分（"hello,word!",","）
    ```
    
- 整除
    ```
    含义：除法，相当于java中的算数运算符/
    示例：num = 整除(offset,cnt) + 1
    ```
    
- 相乘
    ```
    含义：乘法,相当于java中的算数运算符*
    示例：price = 相乘(srcPrice,"0.8")
    ```
    
- 字符串无序是否相等比较
    ```
    含义：对比两个无序字符数组是否相等
    示例：if(字符串无序是否相等比较(exptArr,realArr) == false)
    ```
    
- 第一个字符数组中存在第二个字符数组中不存在的字段
    ```
    含义：获取在第一个数组中存在第二个数组中不存在的字段
    示例：lessreturn = 第一个字符数组中存在第二个字符数组中不存在的字段(arr1,arr2)
    ```

- ltrim
    ```
    含义：去除字符串左侧指定字符
    示例：parameter = ltrim(parameter,",")
    ```
    
- rtrim
    ```
    含义：去除字符串右侧指定字符
    示例：parameter = rtrim(parameter,",")
    ```
    
- urldecode
    ```
    含义：字符串解码，等同于java URLDecoder.decode
    示例：ac = urldecode(ac,"GBK")
    ```
    
- urlencode
    ```
    含义：字符串编码，等同于javaURLEncoder.encode
    示例：parameter = urlencode(parameter,"GBK")
    ```
    
- json串key获取
    ```
    含义：获取json字符串指定key的值，只能获取一维json的key
    示例：ratio = json串key获取（jsonStr,"101"）
    ```
    
- 数组A是否包含元素B
    ```
    含义：判断数组中是否包含某一元素，若包含返回true，若不包含返回false
    示例：数组A是否包含元素B(strArr, "a")
    ```

- 数组A是否不包含数组B的任何元素
    ```
    含义：判断数组A是否不包含B的任何元素，若不包含返回true，若包含返回false
    示例：数组A是否不包含数组B的任何元素(strArr1, strArr2)
    ```
    
- 数组A和B是否有交集
    ```
    含义：判断数组A和数组B是否存在交集，若存在则返回true，若不存在则返回false
    示例：数组A和B是否有交集(strArr1, strArr2)
    ```
    
- 数组B是否是数组A的子集
    ```
    含义：判断数组B是否是数组A的子集，若是则返回true，若否则返回false
    示例：数组B是否是数组A的子集（A, B）
    ```
    
- 获取json对应指定key值
    ```
    含义：递归查找json中指定的key，并返回value
    示例：获取json对应指定key值（jsonArr, "key1, key2"）
    ```
    
- json数组中是否存在指定值
    ```
    含义：jsonarray是否存在指定的值，若存在则返回true，若不存在则返回false
    示例：json数组中是否存在指定值（jsonArr, "test"）
    ```
    
- 整数字符串比较
    ```
    含义：两个整数字符串对比，str1>str2时返回1，str1==str2时返回0，str1<str2时返回-1
    示例：整数字符串比较("123","456")
    ```
    
- 字符串是否为正整数
    ```
    含义：判断字符串是否为正整数，若是返回true，若不是返回false
    示例：字符串是否为正整数("acbc")
    ```
  
## 自定义规则常量
进入online-test-platform/agent/src/main/java/com/taobao/qa/ruleengine/rules/primitives目录，目录下的ConstraintsClassMethods和CommonClassMethods两个类中有所有规则常量的校验函数。

- 在类中添加新的校验函数
```
/**
     * 递归查找srcobj中keyStr字段
     * @param srcObj  原始待查找jonsarry
     * @param keyStr 待查找递归字符串，例如app,deals,id的含义是查找arr中app.deals.id字段
     * @return
     */
    public static com.alibaba.fastjson.JSONArray getMultiFieldFromJson(JSONArray srcObj, String keyStr){
        JSONArray arr = new JSONArray();
        String res = "";
        // 保证查找对原始数据无影响
        String midStr = srcObj.toJSONString();
        JSONArray midArr = JSONArray.parseArray(midStr);
        while(res != null){
            res = getFieldFromJsonSingle(midArr,keyStr,0);
            if(res != null){
                arr.add(res);
            }
        }
        return arr;
    }
```

- 进入online-test-platform/agent/src/main/java/com/taobao/qa/ruleengine/rules，在RuleRunner中添加规则常量关联校验函数
```
runner.addFunctionOfClassMethod("获取json对应指定key值",CommonClassMethods.class.getName(),
                "getMultiFieldFromJson",new String[]{JSONArray.class.getName(),String.class.getName()},null);
```

- 编写规则时即可使用"获取json对应指定key值"