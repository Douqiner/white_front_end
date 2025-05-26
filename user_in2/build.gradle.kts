//// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    // Compose插件（直接通过id引用，绕过版本目录问题）
////    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
//}
// 项目根目录 build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    // 移除通过版本目录的引用，改为直接声明
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
//    // Compose编译器插件必须与Kotlin版本匹配
//    id("org.jetbrains.kotlin.plugin.compose") version "1.9.22" apply false
}