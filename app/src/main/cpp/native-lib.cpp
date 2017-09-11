#include <jni.h>
#include <string>
#include <sys/system_properties.h>

extern "C"

JNIEXPORT jstring
SystemProperties_getSettings(JNIEnv *env, jobject clazz,
                                     jstring keyJ, jstring defJ){
    int len;
    const char* key;
    char buf[100];
    jstring rvJ = NULL;

    if (keyJ == NULL) {
        goto error;
    }
    key = env->GetStringUTFChars(keyJ, NULL);
    len = __system_property_get(key, buf);
    if ((len <= 0) && (defJ != NULL)) {
        rvJ = defJ;
    } else if (len >= 0) {
        rvJ = env->NewStringUTF(buf);
    } else {
        rvJ = env->NewStringUTF("");
    }

    env->ReleaseStringUTFChars(keyJ, key);

    error:
    return rvJ;
}

JNIEXPORT jstring JNICALL
Java_com_chenqihong_antihook_Properties_getProperty(
        JNIEnv *env,
        jobject clazz,
        jstring keyJ) {
    return SystemProperties_getSettings(env, clazz, keyJ, NULL);
}
