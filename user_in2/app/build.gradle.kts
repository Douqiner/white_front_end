//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//}
plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")  // 直接启用插件
//    id("org.jetbrains.kotlin.plugin.compose")  // 直接启用
}

android {
    namespace = "com.example.white_web"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.white_web"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    defaultConfig {
        // 使用非Google镜像
        manifestPlaceholders.put("usesCleartextTraffic", "true")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.10"  // 降级Compose编译器
//    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }
}

dependencies {
//    // 基础Compose依赖（修复图片中的Unresolved reference错误）
//    implementation(libs.androidx.compose.ui)
//    implementation(libs.androidx.compose.material3)
//    implementation(libs.androidx.compose.runtime)
//    implementation(libs.androidx.compose.foundation)
//
//    // Compose与Activity集成
//    implementation(libs.androidx.activity.compose)
//
//    // 预览支持
//    debugImplementation(libs.androidx.compose.ui.tooling)
//    implementation(libs.androidx.compose.ui.tooling.preview)
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
    // 基础Android依赖
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Compose核心依赖 (通过BOM管理版本)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.animation)

//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation(libs.squareup.retrofit.converter.gson)
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    // Retrofit核心库（修正拼写和符号）
    implementation(libs.squareup.retrofit)

    // 工具和扩展
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)

    // 调试工具
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // 测试依赖
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
}