package com.taobao.qa.ruleengine.utils;

import java.net.InetAddress;

public class IpUtil {
    public static String getLocalIp() {
        String ip = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
            System.out.println(ip);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ip;
    }
}
