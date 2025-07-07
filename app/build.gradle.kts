plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    kotlin("android")
    kotlin("plugin.compose")
}

android {
    namespace = "com.bahadirkaya.surumkontrol"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bahadirkaya.surumkontrol"
        minSdk = 21
        targetSdk = 36
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file("surumkontrol-key.jks")
            storePassword = "87888788"
            keyAlias = "surumkontrolkey"
            keyPassword = "87888788"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release") // 🔑 EKLENDİ
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
        buildConfig = true
    }

    applicationVariants.all {
        outputs.all {
            val outputImpl = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val appName = "surumkontrol"
            val version = versionName
            val buildType = buildType.name
            outputImpl.outputFileName = "${appName}_v${version}_${buildType}.apk"
        }
    }


}

firebaseAppDistribution {
    appId = "1:134197709933:android:c703c4856fe3dde0b34364"
    serviceCredentialsFile = "./.credentials/surumkontrol-service-account.json"
    releaseNotes = "Sürüm 1.2 yayınlandı"
    testers = "ebaykaya@gmail.com"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.firebase:firebase-analytics-ktx:21.6.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
