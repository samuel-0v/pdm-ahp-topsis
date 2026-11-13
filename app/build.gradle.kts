plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "br.edu.ufu.pdm.mcdm"
    compileSdk = 35

    defaultConfig {
        applicationId = "br.edu.ufu.pdm.mcdm"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Projeto Firebase "demo-" do Local Emulator Suite: não exige google-services.json.
        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"demo-pdm-ahp-topsis\"")
        buildConfigField("String", "FIREBASE_APPLICATION_ID", "\"1:000000000000:android:0000000000000000\"")
        buildConfigField("String", "FIREBASE_API_KEY", "\"demo-api-key\"")
    }

    buildTypes {
        debug {
            // 10.0.2.2 é o host da máquina vista pelo emulador Android. Ver firebase/README.md.
            buildConfigField("String", "FIRESTORE_EMULATOR_HOST", "\"${providers.gradleProperty("firestoreHost").getOrElse("10.0.2.2")}\"")
            buildConfigField("int", "FIRESTORE_EMULATOR_PORT", providers.gradleProperty("firestorePort").getOrElse("8085"))
        }
        release {
            isMinifyEnabled = false
            // Sem projeto Firebase real nesta versão: o release também aponta para o emulador
            // até que um google-services.json seja fornecido pela equipe.
            buildConfigField("String", "FIRESTORE_EMULATOR_HOST", "\"10.0.2.2\"")
            buildConfigField("int", "FIRESTORE_EMULATOR_PORT", "8085")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
