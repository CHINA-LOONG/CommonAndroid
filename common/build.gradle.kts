plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}

//
//group = 'com.gitee.loong-gitee'
//version = '1.0.4'

android {
    namespace = "com.loong.common"
    compileSdk = 33

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    defaultConfig {
        ndk {
            // 设置支持的SO库架构
            abiFilters+=listOf("armeabi" , "x86", "armeabi-v7a", "x86_64","arm64-v8a")
        }
    }
}

dependencies {
//    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")
    // 实现下拉刷新的界面模式。要添加 SwipeRefreshLayout 的依赖项，您必须将 Google Maven 代码库添加到项目中。
    api ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")




    // 为"单Activity ＋ 多Fragment","多模块Activity + 多Fragment"架构而生，简化开发，轻松解决动画、嵌套、事务相关等问题。
    api ("me.yokeyword:fragmentationx:1.0.2")

    // SmartRefreshLayout以打造一个强大，稳定，成熟的下拉刷新框架为目标，并集成各种的炫酷、多样、实用、美观的Header和Footer。https://github.com/scwang90/SmartRefreshLayout
    api ("com.scwang.smartrefresh:SmartRefreshLayout:1.1.2")  //1.0.5及以前版本的老用户升级需谨慎，API改动过大
    api ("com.scwang.smartrefresh:SmartRefreshHeader:1.1.2")  //没有使用特殊Header，可以不加这行

    // 一个强大并且灵活的RecyclerViewAdapter
    api ("com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4")


    // Retrofit是Square公司开发的一款针对Android网络请求的框架，Retrofit2底层基于OkHttp实现的
    // RxJava是一个在 Java VM 上使用可观测的序列来组成异步的、基于事件的程序的库

    api ("com.squareup.retrofit2:retrofit:2.9.0")
    // 模拟web服务器，用于模拟来自服务器的HTTP响应，并模拟网络行为
    // A mock web server for mocking HTTP responses from a server, and simulating network behaviour
    api ("com.squareup.retrofit2:retrofit-mock:2.9.0")
    // 一个使用Gson进行JSON序列化的转换器   可以传递给GsonConverterFactory
    // A Converter which uses Gson for serialization to and from JSON
    api ("com.squareup.retrofit2:converter-gson:2.9.0")
    // 一个转换器，支持将字符串和原语及其装箱类型转换为文本/普通体。
    // A Converter which supports converting strings and both primitives and their boxed types to text/plain bodies
    api ("com.squareup.retrofit2:converter-scalars:2.9.0")
    // 适配RxJava 2.x类型的适配器
    // An Adapter for adapting RxJava 2.x types
    api ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")

    api ("com.squareup.okhttp3:logging-interceptor:3.14.8")


    api ("io.reactivex.rxjava2:rxjava:2.1.0")
    api ("io.reactivex.rxjava2:rxandroid:2.0.1")
    api ("com.jakewharton.rxbinding2:rxbinding:2.1.1")


    // Bugly崩溃日志收集插件 4.1.9
    api ("com.tencent.bugly:crashreport:4.1.9")

    // 功能强大，UI简洁，交互优雅的通用弹窗！可以替代Dialog，PopupWindow，PopupMenu，BottomSheet，DrawerLayout，Spinner等组件，自带十几种效果良好的动画， 支持完全的UI和动画自定义！
    //xpopup通用弹窗
    api ("com.lxj:xpopup:2.2.4")
    api ("com.lxj:xpopup-ext:0.0.3")

    //只做一个可以自定义的轮播容器，不侵入UI ———— Banner 2.0
    api ("io.github.youth5201314:banner:2.2.2")

//    一款针对Android平台下的图片选择器，支持从相册获取图片、视频、音频&拍照，支持裁剪(单图or多图裁剪)、压缩、主题自定义配置等功能，支持动态获取权限&适配Android 5.0+系统的开源图片选择框架。
//    api 'com.github.LuckSiege.PictureSelector:picture_library:v2.5.8' // 图片选择
//    版本升级3.10.6
    // PictureSelector basic (Necessary)
    api ("io.github.lucksiege:pictureselector:v3.10.6")
    // image compress library (Not necessary)
    api ("io.github.lucksiege:compress:v3.10.6")
    // uCrop library (Not necessary)
    api ("io.github.lucksiege:ucrop:v3.10.6")
    // simple camerax library (Not necessary)
    api ("io.github.lucksiege:camerax:v3.10.6")


    api ("com.google.android:flexbox:1.0.0")

    //今日头条屏幕适配方案
    api ("me.jessyan:autosize:1.2.1")

    //Glide是一个快速高效的Android图片加载库，注重于平滑的滚动。Glide提供了易用的API，高性能、可扩展的图片解码管道（decode pipeline），以及自动的资源池技术。
    api ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // 泛型生成库TypeBuilder
    api ("com.github.ikidou:TypeBuilder:1.0")

    //EventBus是用于Android和Java的发布/订阅事件总线。https://github.com/greenrobot/EventBus
    api ("org.greenrobot:eventbus:3.2.0")

    // 文字转拼音库
    api ("com.github.promeg:tinypinyin:1.0.0") // ~80KB
    // 使用RXJava实现的权限请求
//    api 'com.github.tbruyelle:rxpermissions:0.12'
    api ("com.github.tbruyelle:rxpermissions:v0.11")

    // 沉浸式状态栏
    // 基础依赖包，必须要依赖
    api ("com.gyf.immersionbar:immersionbar:3.0.0")
    // fragment快速实现（可选）
    api ("com.gyf.immersionbar:immersionbar-components:3.0.0")

    // log输出库
    api ("com.jakewharton.timber:timber:4.7.1")
    //logger打印
    api ("com.orhanobut:logger:2.1.1")

    // 一个Android TabLayout库,目前有3个TabLayout
    // https://github.com/H07000223/FlycoTabLayout
    api ("com.lzp:FlycoTabLayoutZ:1.3.3")

    // 在Okhttp（Retrofit和Glide）中收听下载和上传的进度。https://github.com/JessYanCoding/ProgressManager/blob/master/README-zh.md
    implementation ("me.jessyan:progressmanager:1.5.0")

    //一个强大且易于使用的 Android 图表库 https://github.com/PhilJay/MPAndroidChart
    api ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // 阿里云OSS上传
    api ("com.aliyun.dpa:oss-android-sdk:2.9.5")




    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3")
    // or Material Design 2
    implementation("androidx.compose.material:material")
    // or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
    // or only import the main APIs for the underlying toolkit systems,
    // such as input and measurement/layout
    implementation("androidx.compose.ui:ui")

    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Optional - Included automatically by material, only add when you need
    // the icons but not the material library (e.g. when using Material3 or a
    // custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
    // Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")

    // Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.6.1")
    // Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    // Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")

}



//afterEvaluate {
//    publishing {
//        publications {
//            // Creates a Maven publication called "release".
//            release(MavenPublication) {
//                from components.release
//                groupId = 'com.gitee.loong-gitee'
//                artifactId = 'common-android'
//                version = '1.0.4'
//            }
//        }
//    }
//}
