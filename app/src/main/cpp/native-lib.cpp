#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_ndkdemo_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    std::string randnum = std::to_string(rand() % 1000);
    hello.append(randnum);

    return env->NewStringUTF(hello.c_str());
}