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
    private boolean isExposedBySubstrate;

    public AntiHookByProcessor(Context context){
        mContext = context;
    }

    public List<ApplicationInfo> getApplicationInfo(){
        return mApplicationInfoList = mContext.getPackageManager()
                .getInstalledApplications(PackageManager.GET_META_DATA);
    }

    public boolean isExposed(){
        for(ApplicationInfo applicationInfo: mApplicationInfoList){
            if(applicationInfo.packageName.contains(".xposed")){
                isExposedByXposed = true;
                return true;
            }

            if(applicationInfo.packageName.contains(".substrate")){
                isExposedBySubstrate = true;
                return true;
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
