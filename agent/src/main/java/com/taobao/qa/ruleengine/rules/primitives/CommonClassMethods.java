package com.taobao.qa.ruleengine.rules.primitives;

import com.alibaba.fastjson.JSON;
//import org.json.JSONArray;
import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.io.UnsupportedEncodingException;

public class CommonClassMethods {

    /**
     * 比较两个字符串是否相等，对应自定义函数"字符串比较"，例如 if(字符串比较(expectedRefPid, actualRefPid) != 0)
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 如果str1=str2，则返回0，否则返回非0
     */
    public static int strCompare(String str1,String str2) {
        return str1.compareTo(str2);
    }

    /**
     * 将整数转换为字符串，对应自定义函数"整数转为字符串"，例如 exptpos = 整数转为字符串(j+1);
     * @param val 被转换的整数
     * @return val对应的字符串
     */
    public static String intToString(Integer val){
         return Integer.toString(val);
    }

    /**
     * 获取字符数组中指定位置的值，对应自定义函数"getByIndex"，例如 minVal = getByIndex(arr,0)
     * @param arr 字符数组
     * @param index 位置
     * @return arr[index]值
     */
    public static String getByIndex(String[] arr,Integer index){
        String val = null;
        if(arr.length > index){
            val = arr[index];
        }
        return val;
    }

    /**
     * 字符串按照指定的分隔符做切分，对应自定义函数"字符串切分"，例如 exptArr = 字符串切分(titleStr,",")
     * @param str 被切分字符串
     * @param splits 切分字符
     * @return 切分后的字符串数组
     */
    public static String[] strSplit(String str,String splits){
        String[] arr = str.split(splits);
        return arr;
    }

    /**
     * 去除字符串左侧指定的字符，对应自定义函数"ltrim"，例如 pid = ltrim(pid,",")
     * @param str 被处理字符串
     * @param substr 被删除字符串
     * @return 删除str左侧substr后的新字符串
     */
    public static String ltrim(String str,String substr){
        int i=0;
        for(;i<str.length();i++){
            if(substr.indexOf(str.charAt(i))==-1){
                break;
            }
        }
        return str.substring(i);
    }

    /**
     * 去除字符串右侧指定的字符，对应自定义函数"rtrim"，例如 pid = rtrim(pid,",")
     * @param str 被处理字符串
     * @param substr 被删除字符串
     * @return 删除str右侧substr后的新字符串
     */
    public static String rtrim(String str,String substr){
        int j=str.length()-1;
        for(;j>-1;j--){
            if(substr.indexOf(str.charAt(j))==-1){
                break;
            }
        }
        return str.substring(0, j+1);
    }

    /**
     * 等同于java URLDecoder.decode，对应自定义函数"urldecode"，例如 ac = urldecode(ac,"GBK");
     * @param str 被处理字符串
     * @param code 编码方式，例如utf-8
     * @return 解码后的值
     */
    public static String urldecode(String str,String code){
        String res = "";
        try {
            res = URLDecoder.decode(str,code);
        }catch (UnsupportedEncodingException e){
            System.out.print(e.getCause());
        }
        return res;
    }

    /**
     * 等同于URLEncoder.encode，对应自定义函数"urlencode"，例如 ac = urleecode(ac,"GBK");
     * @param str 被处理字符串
     * @param code 编码方式，例如utf-8
     * @return 编码后的值
     */
    public static String urlencode(String str,String code){
        String res = "";
        try {
            res = URLEncoder.encode(str,code);
        }catch (UnsupportedEncodingException e){
            System.out.print(e.getCause());
        }
        return res;
    }

    /**
     * 获取json串指定key的值，目前只能获取一层json的key，对应自定义函数"json串key获取"，例如 ratio = json串key获取(mtStr,"101");
     * @param jsonString json字符串，取值类似 ：{"0":1508,"1":1687,"ratio":8}
     * @param k 需要获取值的key
     * @return k在jsonString中对应的取值
     */
    public static String executeJson(String jsonString, String k){
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator iterator = jsonObject.keys();
        while(iterator.hasNext()){
            String key = (String) iterator.next();
            Object value = (Object)jsonObject.get(key);
            if(key.equals(k)) {
                return value.toString();
            }
        }
        return "";
    }

    /**
     * 验证字符数组是否包含指定的元素，对应自定义函数"数组A是否包含元素B"
     * @param strArr 字符数组
     * @param val  字符元素
     * @return  strArr为空，则返回false；否则strArr包含val元素，则返回true；否则返回false
     */
    public static boolean contains(String[] strArr,String val){
        if(strArr.length == 0){
            return false;
        }
        for(String one:strArr){
            if(one.equals(val)){
                return true;
            }
        }
        return false;
    }

    /**
     * 验证数组A不包含数组B中的任何元素,对应自定义函数"数组A是否不包含数组B的任何元素"
     * @param A 数组A
     * @param B 数组B
     * @return A或B为空，返回false；否则A包含B的任何元素，返回false；否则返回true
     */
    public static boolean containsNon(String[] A,String[] B){
        if(A.length == 0 || B.length == 0){
            return false;
        }
        for(String oneB:B){
            for(String oneA:A){
                if(oneB.equals(oneA)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 验证两个数组的交集，对应自定义函数"数组A和B是否有交集"
     * @param A 字符串数组
     * @param B 字符串数组
     * @return A&B都为空，则返回false；否则如果两个数组交集非空，则返回true，否则返回false
     */
    public static boolean containsAny(String[] A,String[] B){
        if(A.length == 0 || B.length == 0){
            return false;
        }
       for(String oneA:A){
           for(String oneB:B){
               if(oneA.equals(oneB)){
                   return true;
               }
           }
       }
       return false;
    }

    /**
     * 验证数组A是否包含数组B中的所有元素，即数组B是否是数组A的子集，对应自定义函数"数组B是否是数组A的子集"
     * @param A 字符数组A
     * @param B 字符数组B
     * @return A&B都为空，返回false；否则A包含B的所有元素，则返回true；否则返回false
     */
    public static boolean containsAll(String[] A,String[] B){
        if(A.length == 0 || B.length == 0){
            return false;
        }
        for(String oneB:B){
            for(String oneA:A){
                if(!oneA.equals(oneB)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 获取指定jsonArr或jsonObj中指定的key。该查找方式是有损查找，被找到的叶子节点数据会被删除
     * @param srcObj  待查找jsonArr或jsonObj
     * @param keyStr  查找字段的路径key，多级key用逗号连接
     * @param keypos  keyStr中的第几个key，keypos初始值为0
     * @return 查找的结果，若找到则返回对应的string，否则返回null
     */
    private static String getFieldFromJsonSingle(Object srcObj, String keyStr,Integer keypos){
        String res = null;
        String[] keyArr = keyStr.split(",");
        if( keypos >= keyArr.length){
            return null;
        }
        String currKey = keyArr[keypos];
        if(srcObj instanceof com.alibaba.fastjson.JSONArray){
            for(int i = 0; i < ((com.alibaba.fastjson.JSONArray) srcObj).size(); i++){
                Object subobj = ((com.alibaba.fastjson.JSONArray) srcObj).get(i);
                res = getFieldFromJsonSingle(subobj,keyStr,keypos);
                if( res != null){
                    return res;
                }
            }
        }
        else if( srcObj instanceof com.alibaba.fastjson.JSONObject){
            Set<String> keySet = ((com.alibaba.fastjson.JSONObject) srcObj).keySet();
            if(keySet.contains(currKey)){
                if((keypos +1) == keyArr.length){ // 待查询字段的最后一个字段，查找结束，返回查询结果
                    Object obj =  ((com.alibaba.fastjson.JSONObject) srcObj).get(currKey);
                    if(obj instanceof com.alibaba.fastjson.JSONObject){
                        res =  JSONObject.valueToString(obj);
                    }
                    res = ((com.alibaba.fastjson.JSONObject) srcObj).getString(currKey);
                    ((com.alibaba.fastjson.JSONObject) srcObj).remove(currKey);// 删除已找到的key
                    return res;
                }else{
                    Object subobj = ((com.alibaba.fastjson.JSONObject) srcObj).get(currKey);
                    res = getFieldFromJsonSingle(subobj,keyStr,keypos+1);
                    if(res != null){
                        return res;
                    }
                }
            }
        }
        return res;
    }

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
    
    /**
     * jsonarray中是否存在指定的值
     * @param A
     * @param value
     * @return
     */
    public static boolean containsValue(com.alibaba.fastjson.JSONArray A,String value){
    	if(A==null||A.size()<1){
    		return false;
    	}
    	
    	for(Object a :A){
    		if(a.toString().equals(value)){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 两个整数字符串对比，str1>str2时返回1，str1==str2时返回0，str1<str2时返回-1
     * @param str1
     * @param str2
     * @return
     */
    public static int stringIntCompare(String str1,String str2){
        if(str1.equals(str2)){
            return 0;
        }

        if(str1.length()>str2.length()){
            return 1;
        }

        if(isNumeric(str1)&&isNumeric(str2)){
            int int1 = Integer.parseInt(str1);
            int int2 = Integer.parseInt(str2);
            if(int1>int2){
                return 1;
            }else if(int1==int2){
                return 0;
            }else{
                return -1;
            }
        }


        return str1.compareTo(str2);
    }

    /**
     * 判断字符串是否为正整数
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
//		   System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public static String getProductFromTable(String tableName) {
        String product = "";
        switch (tableName){
            case "t_rule":{
                product = "steed_data";
                break;
            }
        }
        return product;
    }
}
