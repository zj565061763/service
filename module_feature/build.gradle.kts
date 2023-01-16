plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.sd.demo.service.app_module_a"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

ksp {
    arg("FSERVICE_MODULE_NAME", project.name)
}

dependencies {
    implementation(project(":module_common"))
    ksp(libs.sd.serviceCompiler)
}