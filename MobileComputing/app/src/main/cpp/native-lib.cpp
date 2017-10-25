#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_gauravpc_mobilecomputing_MainActivity_stringFromJNI(JNIEnv* env, jobject /* this */,jstring msg) {

    std::string hello = "Hello from C++";
//    hello.append((const char *) msg);
    const char *mmsg=env->GetStringUTFChars(msg,NULL);
    return env->NewStringUTF(mmsg);
}
