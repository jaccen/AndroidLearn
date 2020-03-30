//
// Created by jaccen on 2020-03-12.
//
#include <string>
#include <jni.h>
/* Header for class test */

#ifndef _Included_test
#define _Included_test
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     test
 * Method:    stringFromJNI
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_jaccen_jni_test_stringFromJNI(JNIEnv *env, jobject thiz) {

     std::string hello = "Hello from C++";
         return env->NewStringUTF(hello.c_str());
  }

#ifdef __cplusplus
}
#endif
#endif
extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_nativeProcessFrameMat(JNIEnv *env, jobject thiz,
                                                                    jlong mat_addr_gr,
                                                                    jlong mat_addr_rgba,
                                                                    jintArray status_buf) {
    // TODO: implement nativeProcessFrameMat()
}extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_detect(JNIEnv *env, jobject thiz,
                                                     jintArray status_buf) {
    // TODO: implement detect()
}extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_initSLAM(JNIEnv *env, jobject thiz, jstring path) {
    // TODO: implement initSLAM()
}extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_getM(JNIEnv *env, jobject thiz, jfloatArray model_m) {
    // TODO: implement getM()
}extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_getV(JNIEnv *env, jobject thiz, jfloatArray view_m) {
    // TODO: implement getV()
}extern "C"
JNIEXPORT void JNICALL
Java_com_jaccen_androidlearn_VIO_NativeHelper_getP(JNIEnv *env, jobject thiz, jint image_width,
                                                   jint image_height, jfloatArray projection_m) {
    // TODO: implement getP()
}