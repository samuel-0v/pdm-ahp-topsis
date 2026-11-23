import groovy.json.JsonSlurper

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

// O modo real usa os identificadores do app Android registrado no Firebase.
// O JSON é lido pelo Gradle; o app continua inicializando o SDK manualmente.
val firebaseMode = providers.gradleProperty("firebaseMode").getOrElse("emulator")
require(firebaseMode in setOf("emulator", "real")) {
    "firebaseMode deve ser 'emulator' ou 'real'."
}

val firebaseConfig = if (firebaseMode == "real") {
    val configFile = file("google-services.json")
    require(configFile.isFile) {
        "Para -PfirebaseMode=real, baixe google-services.json do Firebase e salve em app/google-services.json."
    }
    val root = JsonSlurper().parse(configFile) as Map<*, *>
    val projectId = (root["project_info"] as? Map<*, *>)?.get("project_id") as? String
    val client = (root["client"] as? List<*>)
        ?.filterIsInstance<Map<*, *>>()
        ?.firstOrNull { entry ->
            val info = entry["client_info"] as? Map<*, *>
            val android = info?.get("android_client_info") as? Map<*, *>
            android?.get("package_name") == "br.edu.ufu.pdm.mcdm"
        }
    val appId = (client?.get("client_info") as? Map<*, *>)?.get("mobilesdk_app_id") as? String
    val apiKey = (client?.get("api_key") as? List<*>)
        ?.filterIsInstance<Map<*, *>>()?.firstOrNull()?.get("current_key") as? String
    require(!projectId.isNullOrBlank() && !appId.isNullOrBlank() && !apiKey.isNullOrBlank()) {
        "google-services.json deve conter project_id e um app Android br.edu.ufu.pdm.mcdm com mobilesdk_app_id e current_key."
    }
    Triple(projectId, appId, apiKey)
} else null

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

        buildConfigField("String", "FIREBASE_PROJECT_ID", "\"${firebaseConfig?.first ?: "demo-pdm-ahp-topsis"}\"")
        buildConfigField("String", "FIREBASE_APPLICATION_ID", "\"${firebaseConfig?.second ?: "1:000000000000:android:0000000000000000"}\"")
        buildConfigField("String", "FIREBASE_API_KEY", "\"${firebaseConfig?.third ?: "demo-api-key"}\"")
        buildConfigField("boolean", "USE_FIRESTORE_EMULATOR", (firebaseMode == "emulator").toString())
    }

    buildTypes {
        debug {
            // 10.0.2.2 é o host da máquina vista pelo emulador Android. Ver firebase/README.md.
            buildConfigField("String", "FIRESTORE_EMULATOR_HOST", "\"${providers.gradleProperty("firestoreHost").getOrElse("10.0.2.2")}\"")
            buildConfigField("int", "FIRESTORE_EMULATOR_PORT", providers.gradleProperty("firestorePort").getOrElse("8085"))
        }
        release {
            isMinifyEnabled = false
            // O acesso real requer -PfirebaseMode=real e app/google-services.json.
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
