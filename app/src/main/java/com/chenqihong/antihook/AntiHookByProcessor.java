package com.chenqihong.antihook;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by chenqihong on 2017/9/6.
 */

public class AntiHookByProcessor {
    private List<ApplicationInfo> mApplicationInfoList;
    private Context mContext;
    private boolean isExposedByXposed;

    public AntiHookByProcessor(Context context){
        mContext = context;
    }

    public List<ApplicationInfo> getApplicationInfo(){
        return mApplicationInfoList = mContext.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);
    }

    public boolean isExposed(){
        mApplicationInfoList = mContext.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for(ApplicationInfo applicationInfo: mApplicationInfoList){
            if(applicationInfo.packageName.contains(".xposed")){
                isExposedByXposed = true;
                return true;
            }
        }

        return false;
    }

    public boolean isExposedByXposed(){
        return isExposedByXposed;
    }
}
