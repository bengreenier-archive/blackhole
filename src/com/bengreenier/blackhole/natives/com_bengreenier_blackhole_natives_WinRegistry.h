/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_bengreenier_blackhole_natives_WinRegistry */

#ifndef _Included_com_bengreenier_blackhole_natives_WinRegistry
#define _Included_com_bengreenier_blackhole_natives_WinRegistry
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_bengreenier_blackhole_natives_WinRegistry
 * Method:    writeKey
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_writeKey
  (JNIEnv *, jclass, jstring);

/*
 * Class:     com_bengreenier_blackhole_natives_WinRegistry
 * Method:    writeValue
 * Signature: (Ljava/lang/String;Ljava/lang/String;I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_writeValue
  (JNIEnv *, jclass, jstring, jstring, jint);

/*
 * Class:     com_bengreenier_blackhole_natives_WinRegistry
 * Method:    getValue
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_getValue
  (JNIEnv *, jclass, jstring);

#ifdef __cplusplus
}
#endif
#endif
