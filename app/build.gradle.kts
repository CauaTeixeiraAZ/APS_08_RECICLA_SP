plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.unip.CC8P33.PontosDeColeta"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.unip.CC8P33.PontosDeColeta"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.firebase.firestore)

    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    implementation(libs.material)

    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)

    implementation(libs.viewpager2)
    implementation(libs.fragment)

    // SwipeRefreshLayout
    implementation(libs.swiperefreshlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}