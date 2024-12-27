plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("kotlin-parcelize")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("C:\\Users\\ilham\\AndroidStudioProjects\\MyKeystore\\keystoreGuppyMarket.jks")
            storePassword = "1sampai8"
            keyAlias = "keyguppymarket"
            keyPassword = "1sampai8"
        }
    }
    namespace = "com.stiki.marcelloilham.guppymarketidn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stiki.marcelloilham.guppymarketidn"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx) // Sudah ada
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.android.material:material:1.9.0")

    // Adding NestedScrollView dependency
    implementation("androidx.core:core-ktx:1.10.1") // Pastikan versi terbaru

    implementation("com.google.android.gms:play-services-maps:18.0.0")


    // Additional dependencies (using AndroidX alternatives)
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.9.0")
//    implementation("com.vmadalin:easypermissions-ktx:1.0.0")
//    implementation("com.squareup.retrofit2:converter-gson:2.4.0")
//    implementation("com.squareup.retrofit2:retrofit:2.4.0")
//    implementation("com.github.bumptech.glide:glide:4.7.1")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.7.1")
//    implementation("com.vincent.filepicker:MultiTypeFilePicker:1.0.7")
}
