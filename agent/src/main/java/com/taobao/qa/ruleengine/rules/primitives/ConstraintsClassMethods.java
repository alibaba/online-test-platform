package com.taobao.qa.ruleengine.rules.primitives;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstraintsClassMethods {

    /**
     * 输入值最小值判断，对应自定义函数"最小值约束"，例如 !最小值约束(ratio,0) or !最大值约束(ratio,10)
     * @param input 被判断的值
     * @param min 允许的最小值
     * @return 如果input大于等于min，则返回true；否则返回false
     */
    public static boolean min(String input, Double min) {
        return Double.parseDouble(input) >= min;
    }

    /**
     * 输入值最大值判断，对应自定义函数"最大值约束"，例如 !最小值约束(ratio,0) or !最大值约束(ratio,10)
     * @param input 被判断的值
     * @param max 允许的最大值
     * @return 如果input小于等于max，则返回true；否则返回false
     */
    public static boolean max(String input, Double max) {
        return Double.parseDouble(input) <= max;
    }

    /**
     * 输入值是否在指定范围内，对应自定义函数"取值范围约束"，例如 !取值范围约束("15",0,20)
     * @param input 被判断的输入值
     * @param min 允许的最大值
     * @param max 允许的最小值
     * @return 如果input在[min,max]范围内，则返回true，否则返回false
     */
    public static boolean inRange(String input, Double min, Double max) {
        Double inputDouble = Double.parseDouble(input);
        return inputDouble >= min && inputDouble <= max;
    }

    /**
     * 判断输入值是否在指定的取值集合内，对应自定义函数"取值集合约束"，例如 !取值范围约束(val,"0","1"，"2")
     * @param input 被判断值
     * @param set 取值集合
     * @return 如果input取值在指定set集合内，则返回true，否则返回false
     */
    public static boolean inSet(String input, String...set) {
        for(String item:set) {
            if(input.equals(item.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断输入值是否是整数，对应自定义函数"isInterger"。例如 if( isInterger("123") or isFloat("123"))
     * @param input 被判断值
     * @return input是整数，则返回true，否则返回false
     */
    public static boolean isInteger(String input) {
        try {
            Long.parseLong(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断输入值是否是浮点数,对应自定义函数"isFloat"。例如 if( isInterger("123") or isFloat("123"))
     * @param input 被判断值
     * @return input是浮点数，则返回true，否则返回false
     */
    public static boolean isFloat(String input) {
        if(!input.contains(".")) {
            return false;
        }

        try {
            Double.parseDouble(input);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断输入值是否是数字，对应自定义函数"isNumberic"。例如 !isNumberic("te1249813")
     * @param input
     * @return
     */
    public static boolean isNumeric(String input) {
        return isInteger(input) || isFloat(input);
    }

    /**
     * 判断输入值是非负整数，对应自定义函数"为非负整数"，例如 为非负整数("-123")
     * @param input 被判断值
     * @return input是非负整数，返回true，否则返回false
     */
    public static boolean isNonNegativeInteger(String input) {
        return isInteger(input) && min(input,0d);
    }

    /**
     * 判断输入值是否符合给定的正则表达式，对应自定义函数"matchPattern"，例如 if(!matchPattern(real,"\\d+.\\d+%"))
     * @param input 被判断的值
     * @param regexpr 正则表达式
     * @return input符合正则regexpr，则返回true，否则返回false
     */
    public static boolean matchPattern(String input, String regexpr) {
        Pattern pattern = Pattern.compile(regexpr);
        Matcher m = pattern.matcher(input);
        return m.matches();
    }

    /**
     * 除法，相当于java中的算数运算符/,对应自定义函数"整除",例如  pageno = 整除(offset,cnt) + 1
     * @param divdend 被除数
     * @param divs 除数
     * @return 整数类型结果值
     */
    public static Integer diver(String divdend,String divs){
        int val = Integer.parseInt(divdend)/Integer.parseInt(divs);
        return val;
    }

    /**
     * 乘法，相当于java中的标准算数运算符*，对应自定义函数"相乘"，例如 price = 相乘（"1"，"5"）
     * @param fir 乘数1
     * @param sec 乘数2
     * @return fir和sec的乘积，=fir*sec
     */
    public static String multiply(String fir,String sec){
        Double val =  Double.parseDouble(fir) * Double.parseDouble(sec);
        int tmp = val.intValue();
        return String.valueOf(tmp);
    }

    /**
     * 判断两个字符数组是否相等，对应自定义函数"字符串无序是否相等比较"，例如 if(字符串无序是否相等比较(exptArr,titleArr) == false)
     * @param arr1
     * @param arr2
     * @return
     */
    public static boolean strArrEqualNoOrder(String[] arr1,String[] arr2){
        if(arr1.length != arr2.length){ return false; }
        int j;
        for(int i=0;i<arr1.length;i++) {
            String val = arr1[i];

            for (j = 0; j < arr2.length; j++) {
                if (arr2[j].compareTo(arr1[i]) == 0) {
                    break;
                }
            }
            if (j >= arr2.length) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在第一个字符数组中存在在第二个字符数组中不存在字符串，对应自定义函数"第一个字符数组中存在第二个字符数组中不存在的字段"，例如 lessreturn = 第一个字符数组中存在第二个字符数组中不存在的字段(exptArr,titleArr);
     * @param arr1 字符数组1
     * @param arr2 字符数组2
     * @return 在arr1中存在但在arr2中不存在的字符用,拼接组成的全新字符串
     */
    public static String strArrsubtract(String[] arr1,String[] arr2){
        String str = "";
        for(int i=0;i<arr1.length;i++){
            String val = arr1[i];
            boolean flag = false;
            for(int j=0;j<arr2.length;j++){
               if(arr2[j].compareTo(val) == 0){
                   flag = true;
                   break;
               }
            }
            if(flag == false){
                if(str.isEmpty()){
                    str = val;
                }else {
                    str = str + "," + val;
                }
            }
        }
        return str;
    }


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str.isEmpty();
    }
    public static void main(String[] args) {
        System.out.println("Enter "+ ConstraintsClassMethods.class.getName());
        String input = "9999.00";
        String pattern = "\\d+\\.\\d\\d";

        System.out.format("matchPattern: %s\n",matchPattern(input,pattern));
    }
}

