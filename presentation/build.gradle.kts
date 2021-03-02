plugins {
    id(BuildPlugins.androidLibrary)

    id(BuildPlugins.kotlinAndroidPlugin)
    id(BuildPlugins.kotlinKaptPlugin)

    id(BuildPlugins.navigationSafeArgsPlugin)
}

android {
    compileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    dexOptions {
        preDexLibraries = false
        dexInProcess = false
        javaMaxHeapSize = "4g"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("LICENSE.txt")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/NOTICE")
        exclude("META-INF/LICENSE")
    }

    lintOptions {
        isQuiet = true
        isAbortOnError = false
        isIgnoreWarnings = true
        disable("InvalidPackage")            //Some libraries have issues with this.
        disable("OldTargetApi")
        //Lint gives this warning but SDK 20 would be Android L Beta.
        disable("IconDensities")             //For testing purpose. This is safe to remove.
        disable("IconMissingDensityFolder")  //For testing purpose. This is safe to remove.
    }
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":infrastructure"))

    implementation(platform("com.google.firebase:firebase-bom:26.5.0"))
    implementation(PresentationModuleDependencies.implementationLibs)
    kapt(PresentationModuleDependencies.kaptLibs)
    api(PresentationModuleDependencies.apiLibs)
    testImplementation(PresentationModuleDependencies.testLibs)
    androidTestImplementation(PresentationModuleDependencies.androidTestLibs)
}