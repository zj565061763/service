plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sd.demo.service.sample_common"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }
}

kotlin {
    jvmToolchain(8)
}

ksp {
    arg("FSERVICE_MODULE_NAME", project.name)
}

dependencies {
    api(project(":service"))
    ksp(project(":service-compiler"))
}

