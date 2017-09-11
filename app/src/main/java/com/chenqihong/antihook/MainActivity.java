package com.chenqihong.antihook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Strategy.getInstance(this).alphaChecking();
        Strategy.getInstance(this).bravoChecking();
        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String id = Strategy.getInstance(this).getSecureDeviceId();
        TextView textView = (TextView)findViewById(R.id.text);
        textView.setText("Device ID:" + manager.getDeviceId()+
                "Android ID:" + Strategy.getInstance(this).getSecureAndroidId() +
                " alpha:" + Strategy.getInstance(this).isHasAlphaXposed() +
                " bravo:" + Strategy.getInstance(this).isHasBravoExposed());
    }
}
