package com.chenqihong.antihook;

import android.content.Context;

/**
 * Created by chenqihong on 2017/9/7.
 */

public class AntiHookByMap {
    private Context mContext;
    private boolean isExposedByXposed;
    private boolean isExposedBySubstrate;
    public boolean isExposed(){
        try {
            Set libraries = new HashSet();
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
    }
}
