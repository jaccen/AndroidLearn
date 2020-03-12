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