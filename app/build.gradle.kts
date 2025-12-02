plugins {
    alias(libs.plugins.android.application)
//    id("com.google.gms.google-services")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.astrantiabooks"
    compileSdk = 36 // UBAH DARI 34 KE 36

    defaultConfig {
        applicationId = "com.asrantiabooks"
        minSdk = 24
        targetSdk = 36 // Disarankan disamakan dengan compileSdk (Ubah ke 36)
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    val lifecycle_version = "2.8.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    implementation("com.google.android.material:material:1.12.0")
    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(libs.appcompat)
    implementation(libs.activity) // Ini yang menyebabkan error jika SDK < 36
    implementation(libs.constraintlayout)

    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))

    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.firebase:firebase-database")

    // WAJIB DITAMBAHKAN agar bisa import com.google.firebase.auth.*
    implementation("com.google.firebase:firebase-auth")

    // WAJIB DITAMBAHKAN agar bisa import com.google.firebase.firestore.*
    // implementation("com.google.firebase:firebase-firestore")

    // Opsional: Jika nanti butuh upload gambar
    implementation("com.google.firebase:firebase-storage")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}