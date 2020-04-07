package com.taobao.qa.ruleengine.utils;

import org.apache.commons.lang.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 */
public class StringUtil {

    /**
     * csv to List
     * @param csvStr
     */
    public static List<String> csvStringToList(String csvStr) {
         if(null == csvStr) {
            return null;
        }

        List<String> output = new Vector<String>();
        for(String item: csvStr.split(",")) {
            item = item.trim();
            if(!item.isEmpty()) {
                output.add(item);
            }
        }
        return output;
    }

     /**
     * csv to HashSet
     * @param csvStr
     */
    public static HashSet<String> csvStringToHashSet(String csvStr) {
         if(null == csvStr) {
            return null;
        }

        HashSet<String> output = new HashSet<String>();
        for(String item: csvStr.split(",")) {
            item = item.trim();
            if(!item.isEmpty()) {
                output.add(item);
            }
        }
        return output;
    }

    /**
     * truncate string with maxLength
     * @param inputStr
     * @param maxLength
     * @return
     */
    public static String truncateString(String inputStr, int maxLength) {
        if(inputStr.length() <= maxLength) {
            return inputStr;
        }
        String output = inputStr.subSequence(0,maxLength)+"...truncated.";
        return output;
    }

    /**
     *
     * @param inputStr
     * @return
     */
    public static String singleSlashToDoubleSlash(String inputStr) {
        Pattern p = Pattern.compile("([^\\\\])(\\\\)([^\\\\])");
        Matcher m = p.matcher(inputStr);

        String output = inputStr;
        if(m.find()) {
            output = m.replaceAll("$1\\\\\\\\$3");
        }

        return output;
    }

    public static String convertBytesToUTF8(String text) {
        String result = null;
        List<Byte> bytes = new ArrayList<Byte>();
        char[] textChar = text.toCharArray();
        int j = 0;
        for(int i = 0; i < textChar.length;) {
            if(textChar[i] == '\\') {
                StringBuffer tmpBuffer = new StringBuffer();
                tmpBuffer.append(textChar[i+1]).append(textChar[i+2]).append(textChar[i+3]);
                bytes.add((byte)Integer.valueOf(tmpBuffer.toString(), 8).intValue());
                i+=4;
            } else {
                bytes.add((byte)textChar[i]);
                i++;
            }
            j++;
        }

        byte[] bytesArray = new byte[bytes.size()];
        for(int i = 0; i < bytes.size(); i++) {
            bytesArray[i] = bytes.get(i);
        }


        try {
            result = new String(bytesArray, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(result);
        return result;
    }

    /**
     * @param srcstr
     * @param firstsplit
     * @param secondesplit
     * @return
     */
    public static HashMap<String,String> str2Split(String srcstr,String firstsplit,String secondesplit) {
        HashMap<String,String> resMap = new HashMap<>();
        String[] strArr = srcstr.split(firstsplit,-1);
        for (String key:strArr) {
            String[] tempArr = key.split(secondesplit,-1);
            if(tempArr.length == 2) {
                resMap.put(tempArr[0], tempArr[1]);
            }
        }
        return resMap;
    }
}


