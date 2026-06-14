plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.proyecto.novalearn"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.proyecto.novalearn"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Auth0
        manifestPlaceholders["auth0Domain"] = "dev-04eimw1dww1xo73w.us.auth0.com"
        manifestPlaceholders["auth0Scheme"] = "com.proyecto.novalearn"
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
    // Otras dependencias necesarias

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment:2.9.0")
    implementation("androidx.navigation:navigation-ui:2.9.0")

    // Auth0
    implementation("com.auth0.android:auth0:3.18.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:5.0.7")

    // CircleImageView (esta se queda igual, es la última versión)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // JWT
    implementation("com.auth0.android:jwtdecode:2.0.2")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}