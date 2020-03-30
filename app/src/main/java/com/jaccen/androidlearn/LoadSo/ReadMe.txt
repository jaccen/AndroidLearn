关于Load so、aar、jar

so依赖

1、先建本地仓库，指向so放置的目录
------libs
    |
    |----aar
    |----jar
    |----jniLibs


//建立本地仓库
sourceSets {
    main {
        jniLibs.srcDirs = ['libs/jniLibs']
    }
}

2、so加载
  System.loadLibrary（so名称）;


3、jar依赖

通过jar的全路径去依赖
implementation files('libs/jar/okio.jar')

4、aar依赖


通过aar的全路径去依赖
implementation files('libs/aar/test-debug.aar')


注意：
上面三种方式支持app和aar的打包，但是需要注意：
　　1、aar中打包的aar、jar、android包，app宿主是没办法直接引用的，如果aar和宿主需要使用相同的jar、aar、android包，可以通过gralde配置，
     把公共的aar和jar放置在工程的某个位置，通过gradle配置本地仓库，aar打包时不包含公用的jar和aar，
     在宿主中进行打包(重点建议：1，aar中不要打包jar、aar、android包，全部交给宿主去打包；
     2，如果实在需要aar中打包aar、jar、android包，也需要告知宿主对应的包，宿主需要剔除或者依赖，以免出现未知问题)
　　2、app宿主和aar包中的so对应的cpu类型要一致，不然容易出现so找不到的问题



5、aar打包方法：
         Android库项目的二进制归档文件，包含所有资源，class以及res资源文件全部包含。

         将aar解压（后缀改为.zip，再解压文件）打开后，可以看到每个aar解压后的内容可能不完全一样，但是都会包含AndroidManifest.xml，classes.jar，res，R.txt。

   先把想要打包成sdk的项目做成Android libraries B，不要建成Android project ；

   然后建立一个新的Android project A 去调用写好的libraries B；

   运行后，studio就自动把我们的librarys B自动打包成aar包了，这就是我们想要的sdk了（路径：module 下，build/outputs/aar/）

6、so打包方法


javah配置
Name:javah

Description:javah

Program:$JDKPath$\bin\javah.exe

Parameters:-classpath . -jni -d $ModuleFileDir$\src\main\jni $FileClass$

Working directory:$ModuleFileDir$\src\main\Java


参考：https://my.oschina.net/u/2002921/blog/1800183


CMakeLists.txt
# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             my-native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/hello.cpp )

# Specifies a path to native header files.
include_directories(src/main/cpp/include/)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       my-native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )


//
（1）cmake的最低版本要求:

cmake_minimum_required(VERSION 3.4.1)
（2）使用 add_library() 的 CMake指令构建脚本添加源文件或库，为了确保 CMake 可以在编译时定位您的标头文件，您需要将 include_directories() 命令添加到 CMake 构建脚本中并指定标头的路径：

demo脚本：

add_library( # Sets the name of the library.
             my-native-lib     #library的名称

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/native-lib.cpp
             src/main/cpp/hello.cpp )

# Specifies a path to native header files.
include_directories(src/main/cpp/include/)
（3）使用find_library添加 NDK库

Android NDK 提供了一套实用的原生 API 和库。通过将 NDK库包含到项目的 CMakeLists.txt 脚本文件中，您可以使用这些 API 中的任意一种。预构建的 NDK 库已经存在于 Android 平台上，因此，您无需再构建或将其封装到 APK 中。由于 NDK 库已经是 CMake 搜索路径的一部分，您甚至不需要在您的本地 NDK 安装中指定库的位置 - 只需要向 CMake 提供您希望使用的库的名称，并将其关联到您自己的原生库。

demo脚本：

find_library( # Sets the name of the path variable.
              log-lib   #例如：添加ndk中log-lib库

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
（4）为了确保创建的原生库可以使用log 库中的函数，需要使用 CMake 构建脚本中的 target_link_libraries() 命令关联库。

demo脚本：

target_link_libraries( # Specifies the target library.
                       my-native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

(5)以下命令可以指示 CMake 构建 android_native_app_glue.c，后者会将 NativeActivity 生命周期事件和触摸输入置于静态库中并将静态库关联到库中：

   demo脚本：

   add_library( app-glue
                STATIC
                ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

   # You need to link static libraries against your shared native library.
   target_link_libraries( my-native-lib app-glue ${log-lib} )

   (6）
   添加预构建库与为 CMake 指定要构建的另一个原生库类似。不过，由于库已经预先构建，您需要使用 IMPORTED 标志告知 CMake 您只希望将库导入到项目中：

   add_library( imported-lib
                SHARED
                IMPORTED )
   然后，您需要使用 set_target_properties() 命令指定库的路径

   set_target_properties( # Specifies the target library.
                          imported-lib

                          # Specifies the parameter you want to define.
                          PROPERTIES IMPORTED_LOCATION

                          # Provides the path to the library you want to import.
                          imported-lib/src/${ANDROID_ABI}/libimported-lib.so )
（7）为了确保 CMake 可以在编译时定位您的标头文件，您需要使用 include_directories() 命令，并包含标头文件的路径：

   include_directories( imported-lib/include/ )

 （8）如果您希望封装一个并不是构建时依赖项的预构建库（例如在添加属于 imported-lib 依赖项的预构建库时），则不需要执行以下说明来关联库。

   要将预构建库关联到您自己的原生库，请将其添加到 CMake 构建脚本的 target_link_libraries() 命令中：

   target_link_libraries( my-native-lib imported-lib app-glue ${log-lib} )
   在您构建应用时，Gradle 会自动将导入的库封装到 APK 中。您可以使用 APK 分析器验证 Gradle 将哪些库封装到您的 APK 中。如需了解有关 CMake 命令的详细信息，请参阅 CMake 文档。


完整的demo的gradle文件内容如下：
apply plugin: 'com.android.application'

android {
    ...
    defaultConfig {
        ...
        externalNativeBuild {
            cmake {
                cppFlags ""   //配置c++的版本库，其中""表示使用默认的，如 cppFlags "-std=c++14" 为c++14版本
                abiFilters "armeabi", "armeabi-v7a", "x86","x86_64" // 输出指定abi体系结构下的so库
            }
        }
    }
    ...
//配置引用的CMakeLists.txt文件
    externalNativeBuild {
        cmake {
            path "CMakeLists.txt"
        }
    }
}

dependencies {
    ...
}

引用.so文件
方法一：
将不同CPU架构的so文件拷贝到libs目录下，然后在gradle文件的android节点下添加：

	sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
1
2
3
4
5
defaultConfig节点下添加：

	ndk {
            //设置支持的SO库架构（开发者可以根据需要，选择一个或多个平台的so）
            abiFilters "armeabi", "armeabi-v7a", "arm64-v8a", "x86", "arm64-v8a", "x86_64"
        }
1
2
3
4

sync一下gradle，引入完成。

方法二：
  在 src/main/ 目录下创建文件夹 jniLibs ，然后将so文件复制到这个目录下即可，工程会自动加载src/main/jniLibs目录下的so动态库。
1
如图：



