#include "com_bengreenier_blackhole_natives_WinRegistry.h"
#include <windows.h>

JNIEXPORT jboolean JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_writeKey(JNIEnv *, jclass, jstring) {

}

JNIEXPORT jboolean JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_writeValue(JNIEnv *, jclass, jstring, jstring, jint) {

}

JNIEXPORT jstring JNICALL Java_com_bengreenier_blackhole_natives_WinRegistry_getValue(JNIEnv *, jclass, jstring) {
	RegOpenKeyEx();
}