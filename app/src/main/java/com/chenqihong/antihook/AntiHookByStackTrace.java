package com.chenqihong.antihook;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by chenqihong on 2017/9/6.
 */

public class AntiHookByStackTrace {
    private boolean isExposedByXposed;
    public boolean isExposed(){
        try{
            throw new AntiHookException("catch here");
        }catch (AntiHookException e){
            for(StackTraceElement stackTraceElement : e.getStackTrace()){
                if(stackTraceElement.getClassName().contains(".xposed")){
                    isExposedByXposed = true;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasPackageNameInStack(String name){
        try{
            throw new AntiHookException("catch here");
        }catch (AntiHookException e){
            for(StackTraceElement stackTraceElement : e.getStackTrace()){
                if(stackTraceElement.getClassName().contains(name)){
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isExposedByXposed(){
        return isExposedByXposed;
    }
}
