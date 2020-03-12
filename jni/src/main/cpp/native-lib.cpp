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
JNIEXPORT jstring JNICALL Java_test_stringFromJNI
  (JNIEnv *env, jobject)
  {
     std::string hello = "Hello from C++";
         return env->NewStringUTF(hello.c_str());
  }

#ifdef __cplusplus
}
#endif
#endif