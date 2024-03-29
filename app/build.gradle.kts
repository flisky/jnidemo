plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.jnidemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.jnidemo"
        minSdk = 24
        targetSdk = 34
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
    implementation("net.java.dev.jna:jna:5.13.0@aar")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("rustls:rustls-platform-verifier:0.1.0")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

afterEvaluate {
    val task = tasks.register<Exec>("uniffi") {
        workingDir = project.rootDir
        commandLine("cargo", "build")
        commandLine("cargo", "run", "--bin", "uniffi-bindgen", "--features",  "uniffi/cli", "generate", "--library", "target/debug/libjnidemo.dylib", "--language", "kotlin", "--out-dir", "app/src/main/java/")
        commandLine("cargo", "ndk", "-t", "arm64-v8a", "-o", "app/src/main/jniLibs/", "--", "build")
    }
    android.applicationVariants.forEach { variant ->
        variant.javaCompileProvider.get().finalizedBy(task)
    }
}
