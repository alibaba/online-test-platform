package com.alibaba.test.steed.utils;

import java.io.*;

/**
 * Created by liyang on 2019/8/28.
 */
public class Common {
    public static int getFileLineNum(File f) {
        int count = 0;
        try {
            String req;
            InputStream input = new FileInputStream( f );
            BufferedReader b = new BufferedReader( new InputStreamReader( input ) );
            while ((req = b.readLine()) != null) {
                count++;
            }
            return count;
        } catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }
}
