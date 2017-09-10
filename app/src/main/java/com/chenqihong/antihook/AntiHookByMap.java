package com.chenqihong.antihook;

import android.content.Context;
import android.provider.Telephony;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenqihong on 2017/9/7.
 */

public class AntiHookByMap {
    private Context mContext;
    private boolean isExposedByXposed;
    private boolean isExposedBySubstrate;
    public boolean isExposed(){
        try {
            Set<String> libraries = new HashSet();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            for (String library : libraries) {
                if(library.contains("com.saurik.substrate")) {
                    isExposedByXposed = true;
                    return true;
                }
                if(library.contains("XposedBridge.jar")) {
                    isExposedBySubstrate = true;
                    return true;
                }
            }
            reader.close();
        }
        catch (Exception e) {

        }

        TelephonyManager telephony = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.getDeviceId();

        return false;
    }

    public boolean isExposedByXposed() {
        return isExposedByXposed;
    }

    public boolean isExposedBySubstrate() {
        return isExposedBySubstrate;
    }
}
