import java.time.LocalDate
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.loong.template"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.loong.template"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "1.0.2"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // 基础配置
        buildConfigField("String", "BASE_URL", "\"http://www.baidu.com/修改根地址/\"")
        // OOS配置
        buildConfigField("String", "OSS_ACCESS_KEY_ID", "\"XXXX\"")
        buildConfigField("String", "OSS_ACCESS_KEY_SECRET", "\"XXXXXXX\"")
        buildConfigField("String", "endpoint", "\"\"")
        buildConfigField("String", "bucketName", "\"\"")
        buildConfigField("String", "headUrl", "\"\"")
        vectorDrawables {
            useSupportLibrary = true
        }

        manifestPlaceholders += mapOf("UPDATE_URL" to "更新的域名地址", "UPDATE_APPID" to "更新使用的APPID")
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    signingConfigs {
        /**
         * 设置你的keystore相关
         * demo中只是一个示例，使用时请根据实际情况进行配置
         */
        create("sbGoogle") {
            keyAlias = "psdims"
            keyPassword = "policedog"
            storeFile = file("../psdims.jks")
            storePassword = "policdog"
        }
        create("release") {
            keyAlias = "psdims"
            keyPassword = "policedog"
            storeFile = file("../psdims.jks")
            storePassword = "policdog"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            lint.abortOnError = false
        }
        debug {
            signingConfig = signingConfigs.getByName("sbGoogle")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions += listOf("version")
    productFlavors {
        create("publish") {
            dimension = "version"
//             默认地址直接配置发布的地址，所以这里不需要在配置
        }
        create("local") {
            dimension = "version"
            buildConfigField("String", "BASE_URL", "\"http://www.baidu.com/接口基础域名/\"")
            manifestPlaceholders += mapOf("UPDATE_URL" to "更新的域名地址")
            manifestPlaceholders += mapOf("UPDATE_APPID" to "更新使用的APPID")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    // 输出类型
    applicationVariants.all {
        // 编译类型
        val buildType = this.buildType.name
        outputs.all {
            // 判断是否是输出 apk 类型
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "模板项目${defaultConfig.versionName}_${buildType}_${
                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                }.apk"
            }
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation(project(":common"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")


    
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}