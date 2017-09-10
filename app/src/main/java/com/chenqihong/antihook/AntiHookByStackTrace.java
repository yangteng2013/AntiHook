package com.chenqihong.antihook;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by chenqihong on 2017/9/6.
 */

public class AntiHookByStackTrace {
    private boolean isExposedByXposed;
    private boolean isExposedBySubstrate;
    public boolean isExposed(){
        try{
            throw new AntiHookException();
        }catch (AntiHookException e){
            int zygoteInitCallCount = 0;
            for(StackTraceElement stackTraceElement : e.getStackTrace()){
                if(stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if(zygoteInitCallCount == 2) {
                        isExposedBySubstrate = true;
                        return true;
                    }
                }

                if(stackTraceElement.getClassName().contains(".Xposed")){
                    isExposedByXposed = true;
                    return true;
                }else if(stackTraceElement.getClassName().contains(".substrate")){
                    isExposedBySubstrate = true;
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasPackageNameInStack(String name){
        try{
            throw new AntiHookException();
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

    public boolean isExposedBySubstrate(){
        return isExposedBySubstrate;
    }
}
