plugins {
    id(BuildPlugins.androidLibrary)

    id(BuildPlugins.kotlinAndroidPlugin)
    id(BuildPlugins.kotlinKaptPlugin)
}

android {
    compileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
            }
        }
    }

    dexOptions {
        javaMaxHeapSize = "4g"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":data"))

    //app libs
    implementation(platform("com.google.firebase:firebase-bom:26.5.0"))
    implementation(CacheModuleDependencies.implementationLibs)
    kapt(CacheModuleDependencies.kaptLibs)
    api(CacheModuleDependencies.apiLibs)

    //test libs
    androidTestImplementation(CacheModuleDependencies.androidTestLibs)
}