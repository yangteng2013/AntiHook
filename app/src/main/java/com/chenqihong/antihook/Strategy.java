package com.chenqihong.antihook;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abby on 2017/9/9.
 */

public class Strategy {
    private volatile static Strategy mInstance;
    private Context mContext;
    private boolean hasAlphaXposed;
    private boolean hasAlphaSubstrated;
    private boolean hasBravoExposed;
    private AntiHookByMap antiHookByMap;
    private AntiHookByStackTrace antiHookByStackTrace;
    private AntiHookByProcessor antiHookByProcessor;
    public static Strategy getInstance(Context context){
        if(null == mInstance){
            synchronized (Strategy.class){
                if(null == mInstance){
                    mInstance = new Strategy(context);
                }
            }
        }

        return mInstance;
    }

    private Strategy(Context context){
        mContext = context;
        antiHookByMap = new AntiHookByMap();
        antiHookByProcessor = new AntiHookByProcessor(context);
        antiHookByStackTrace = new AntiHookByStackTrace();
    }

    public void alphaChecking(){
        antiHookByProcessor.isExposed();
        antiHookByMap.isExposed();
        hasAlphaXposed = antiHookByMap.isExposedByXposed() |
                antiHookByProcessor.isExposedByXposed() |
                antiHookByStackTrace.isExposedByXposed();

        hasAlphaSubstrated = antiHookByMap.isExposedBySubstrate() |
                antiHookByProcessor.isExposedBySubstrate() |
                antiHookByStackTrace.isExposedBySubstrate();
    }

    public void bravoChecking(){
        List<ApplicationInfo> processorList = antiHookByProcessor.getApplicationInfo();
        Iterator<ApplicationInfo> iterator = processorList.iterator();
        while(iterator.hasNext()){
            ApplicationInfo applicationInfo = iterator.next();
            hasBravoExposed = antiHookByStackTrace.hasPackageNameInStack(applicationInfo.className);
        }
    }

    public void stackTraceCatching(){
        antiHookByStackTrace.isExposed();
    }

    public boolean isHasAlphaXposed() {
        return hasAlphaXposed;
    }

    public boolean isHasAlphaSubstrated() {
        return hasAlphaSubstrated;
    }

    public boolean isHasBravoExposed() {
        return hasBravoExposed;
    }

    public String getSecureDeviceId(){
        String device0 = SecureSettings.getDeviceIdLevel0(mContext);
        String device1 = SecureSettings.getDeviceIdLevel1(mContext);

        /**
         * 该方法最安全
         */
        String device2 = SecureSettings.getDeviceIdLevel2(mContext);
        if(device0.equals(device1) && device0.equals(device2) && device1.equals(device2)){
            hasBravoExposed = true;
        }

        return device2;
    }
}
