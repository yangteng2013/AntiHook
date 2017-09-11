package com.chenqihong.antihook;

/**
 * Created by abby on 2017/9/11.
 */

public class Properties {
    static {
        System.loadLibrary("native-lib");
    }

    private static native String getProperty(String key);
    public static String getString(String key){
        return getProperty(key);
    }
}
