plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.android.art.glow.drawing"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.android.art.glow.drawing"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1"

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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures { viewBinding = true }

    bundle {
        language {
            enableSplit = false
        }
    }
    base{
        archivesName.set("Magic Doodle_${defaultConfig.versionName}")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(project(":colorPicker"))

    implementation("com.github.skydoves:colorpickerview:2.3.0")

    //Animation
    implementation( "com.airbnb.android:lottie:6.6.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    //advertisement
    implementation("com.github.thienlp201097:DktechLib:2.0.2")
    implementation("com.facebook.android:facebook-android-sdk:18.0.2")
    implementation("com.google.android.gms:play-services-ads:24.1.0")
    implementation("com.github.thienlp201097:smart-app-rate:1.0.7")

    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-config")




}