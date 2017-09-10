package com.chenqihong.antihook;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by abby on 2017/9/9.
 */

public class SecureSettings {
    public static int getTransactionId(Object proxy,
                                       String name) throws
            RemoteException,
            NoSuchFieldException,
            IllegalAccessException {

        int transactionId = 0;
        Class outclass = proxy.getClass().getEnclosingClass();
        Field idField = outclass.getDeclaredField(name);
        idField.setAccessible(true);
        transactionId = (int) idField.get(proxy);
        return transactionId;
    }

    public static String getInterfaceDescriptor(Object proxy) throws
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {

        Method getInterfaceDescriptor = proxy.getClass().getDeclaredMethod("getInterfaceDescriptor");
        return (String) getInterfaceDescriptor.invoke(proxy);
    }

    public static String getDeviceIdLevel0(Context context) {
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        try {
            Method method = TelephonyManager.class.getDeclaredMethod("getITelephony");
            method.setAccessible(true);
            Object binderProxy = method.invoke(telephonyManager);
            Class proxyClass = binderProxy.getClass();
            Method asBinder = proxyClass.getDeclaredMethod("asBinder");
            asBinder.setAccessible(true);
            try {
                Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                if (getDeviceId != null) {
                    return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                }
            } catch (Exception e) {

            }
            Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId");
            if (getDeviceId != null) {
                return (String) getDeviceId.invoke(binderProxy);
            }
        } catch (Exception e) {
            //();
        }
        return "";
    }

    public static String getDeviceIdLevel1(Context context) {
        try {
            Class ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getDeclaredMethod("getService", String.class);
            getService.setAccessible(true);
            IBinder binder = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
            Class Stub = Class.forName("com.android.internal.telephony.ITelephony$Stub");
            Method asInterface = Stub.getDeclaredMethod("asInterface", IBinder.class);
            asInterface.setAccessible(true);
            Object binderProxy = asInterface.invoke(null, binder);
            try {
                Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                if (getDeviceId != null) {
                    return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                }
            } catch (Exception e) {

            }
            Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId");
            if (getDeviceId != null) {
                return (String) getDeviceId.invoke(binderProxy);
            }
        } catch (Exception e) {
            //();
        }
        return "";
    }

    public static String getDeviceIdLevel2(Context context) {

        String deviceId = "";
        try {
            Class ServiceManager = Class.forName("android.os.ServiceManager");
            Method getService = ServiceManager.getDeclaredMethod("getService", String.class);
            getService.setAccessible(true);
            IBinder binder = (IBinder) getService.invoke(null, Context.TELEPHONY_SERVICE);
            Class Stub = Class.forName("com.android.internal.telephony.ITelephony$Stub");
            Method asInterface = Stub.getDeclaredMethod("asInterface", IBinder.class);
            asInterface.setAccessible(true);
            Object binderProxy = asInterface.invoke(null, binder);
            try {
                Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                if (getDeviceId != null) {
                    deviceId = binderGetHardwareInfo(context.getPackageName(),
                            binder, getInterfaceDescriptor(binderProxy),
                            getTransactionId(binderProxy, "TRANSACTION_getDeviceId"));
                }
            } catch (Exception e) {

            }

            Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId");
            if (getDeviceId != null) {
                deviceId = binderGetHardwareInfo("",
                        binder, getInterfaceDescriptor(binderProxy),
                        getTransactionId(binderProxy, "TRANSACTION_getDeviceId"));
            }

            Strategy.getInstance(context).stackTraceCatching();
        } catch (Exception e) {
        }
        return deviceId;
    }

    private static String binderGetHardwareInfo(String callingPackage,
                                                IBinder remote,
                                                String DESCRIPTOR,
                                                int tid) throws RemoteException {

        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        String _result;
        try {
            _data.writeInterfaceToken(DESCRIPTOR);
            if (!TextUtils.isEmpty(callingPackage)) {
                _data.writeString(callingPackage);
            }
            remote.transact(tid, _data, _reply, 0);
            _reply.readException();
            _result = _reply.readString();
        } finally {
            _reply.recycle();
            _data.recycle();
        }
        return _result;
    }
}
