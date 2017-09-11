package com.chenqihong.antihook;

import android.content.ContentResolver;
import android.content.Context;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

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
                    Strategy.getInstance(context).stackTraceCatching();
                    return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                }
            } catch (Exception e) {
                method = TelephonyManager.class.getDeclaredMethod("getSubscriberInfo");
                method.setAccessible(true);
                binderProxy = method.invoke(telephonyManager);
                try {
                    Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                    if (getDeviceId != null) {
                        Strategy.getInstance(context).stackTraceCatching();
                        return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                    }
                } catch (Exception e1) {

                }

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
                    Strategy.getInstance(context).stackTraceCatching();
                    return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                }
            } catch (Exception e) {
                binder = (IBinder) getService.invoke(null, "iphonesubinfo");
                Stub = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub");
                asInterface = Stub.getDeclaredMethod("asInterface", IBinder.class);
                asInterface.setAccessible(true);
                binderProxy = asInterface.invoke(null, binder);
                try{
                    Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                    if (getDeviceId != null) {
                        return (String) getDeviceId.invoke(binderProxy, context.getPackageName());
                    }
                }catch (Exception e1){

                }
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
                binder = (IBinder) getService.invoke(null, "iphonesubinfo");
                Stub = Class.forName("com.android.internal.telephony.IPhoneSubInfo$Stub");
                asInterface = Stub.getDeclaredMethod("asInterface", IBinder.class);
                asInterface.setAccessible(true);
                binderProxy = asInterface.invoke(null, binder);
                try{
                    Method getDeviceId = binderProxy.getClass().getDeclaredMethod("getDeviceId", String.class);
                    if (getDeviceId != null) {
                        deviceId = binderGetHardwareInfo(context.getPackageName(),
                                binder, getInterfaceDescriptor(binderProxy),
                                getTransactionId(binderProxy, "TRANSACTION_getDeviceId"));
                    }
                }catch (Exception e1){

                }
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

    public static String getAndroidProperty(Context context, String name) {
        try {
            Class mUserHandle = Class.forName("android.os.UserHandle");
            Method getUserId = mUserHandle.getDeclaredMethod("getUserId", int.class);
            getUserId.setAccessible(true);
            int uid = (int) getUserId.invoke(null, Process.myUid());
            Class mSecure = Class.forName("android.provider.Settings$Secure");
            Method getString = mSecure.getDeclaredMethod("getStringForUser", ContentResolver.class, String.class, int.class);
            getString.setAccessible(true);
            return (String) getString.invoke(null, context.getContentResolver(), name, uid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAndroidPropertyLevel1(Context context, String name) {

        ContentResolver resolver = context.getContentResolver();
        try {
            Class mUserHandle = Class.forName("android.os.UserHandle");
            Method getUserId = mUserHandle.getDeclaredMethod("getUserId", int.class);
            getUserId.setAccessible(true);
            int uid = (int) getUserId.invoke(null, Process.myUid());

            HashSet<String> MOVED_TO_SECURE = new HashSet<>();
            HashSet<String> MOVED_TO_LOCK_SETTINGS = new HashSet<>();
            HashSet<String> MOVED_TO_GLOBAL = new HashSet<>();
            try {
                Class Global = Class.forName("android.provider.Settings$Global");
                Field field = Global.getDeclaredField("MOVED_TO_SECURE");
                field.setAccessible(true);
                MOVED_TO_SECURE = (HashSet<String>) field.get(Global);
            } catch (Exception e) {
            }
            try {
                Class Secure = Class.forName("android.provider.Settings$Secure");
                Field field = Secure.getDeclaredField("MOVED_TO_LOCK_SETTINGS");
                field.setAccessible(true);
                MOVED_TO_LOCK_SETTINGS = (HashSet<String>) field.get(Secure);
            } catch (Exception e) {
            }
            try {
                Class Secure = Class.forName("android.provider.Settings$Secure");
                Field field = Secure.getDeclaredField("MOVED_TO_GLOBAL");
                field.setAccessible(true);
                MOVED_TO_GLOBAL = (HashSet<String>) field.get(Secure);
            } catch (Exception e) {

            }

            if (MOVED_TO_SECURE.contains(name)) {

            } else if (MOVED_TO_GLOBAL.contains(name)) {
                Class mSecure = Class.forName("android.provider.Global");
                Method getStringForUser = mSecure.getDeclaredMethod("getStringForUser", ContentResolver.class, String.class, int.class);
                getStringForUser.setAccessible(true);
                return (String) getStringForUser.invoke(null, resolver, name, uid);
            } else if ((MOVED_TO_LOCK_SETTINGS.contains(name))) {
                Class ServiceManager = Class.forName("android.os.ServiceManager");
                Method getService = ServiceManager.getDeclaredMethod("getService");
                getService.setAccessible(true);
                IBinder binder = (IBinder) getService.invoke(null, "lock_settings");
                Class Stub = Class.forName("com.android.internal.widget.ILockSettings$Stub");
                Method asInterface = Stub.getDeclaredMethod("asInterface", IBinder.class);
                asInterface.setAccessible(true);
                Object binderProxy = asInterface.invoke(null, binder);
                boolean sIsSystemProcess = Process.myUid() == Process.SYSTEM_UID;
                if (MOVED_TO_LOCK_SETTINGS.contains(name)) {
                    if (binderProxy != null && !sIsSystemProcess) {
                        Class proxy = binderProxy.getClass();
                        Method getString = proxy.getDeclaredMethod("getString", String.class, String.class, int.class);
                        return (String) getString.invoke(name, "0", uid);
                    }
                }
            }
            Class Secure = Class.forName("android.provider.Settings$Secure");
            Field field = Secure.getDeclaredField("sNameValueCache");
            field.setAccessible(true);
            Object sNameValueCache = field.get(null);
            Class NameValueCache = sNameValueCache.getClass();
            Method getStringForUser = NameValueCache.getDeclaredMethod("getStringForUser", ContentResolver.class, String.class, int.class);
            return (String) getStringForUser.invoke(sNameValueCache, resolver, name, uid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
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
