import java.io.FileInputStream
import java.util.*

plugins {
    id(BuildPlugins.androidApplication)

    id(BuildPlugins.kotlinAndroidPlugin)
    id(BuildPlugins.kotlinKaptPlugin)

    id(BuildPlugins.googleServicesPlugin)
    id(BuildPlugins.crashlyticsPlugin)
    id(BuildPlugins.perfMonitorPlugin)
    id(BuildPlugins.navigationSafeArgsPlugin)
}

// Create a variable called keystorePropertiesFile, and initialize it to your
// keystore.properties file, in the rootProject folder.
val keystorePropertiesFile = rootProject.file("keystore.properties")

// Initialize a new Properties() object called keystoreProperties.
val keystoreProperties = Properties()

// Load your keystore.properties file into the keystoreProperties object.
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    compileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        applicationId = "com.deledzis.messenger"
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        multiDexEnabled = true
        versionCode = project.generateVersionCode("version.properties")
        versionName = project.generateVersionName("version.properties")

        testInstrumentationRunner = AppConfig.androidTestInstrumentation
    }

    /*flavorDimensions.add("version")
    productFlavors {
        create("mock") {
            dimension = "version"
            versionNameSuffix = "-mock"
            resValue("string", "api_version", "v1")
            resValue("string", "base_url", "http://10.0.2.2:8080")
        }
        create("production") {
            dimension = "version"
            versionNameSuffix = "-prod"
            resValue("string", "api_version", "v1")
            resValue("string", "base_url", "https://spbstu-messenger.herokuapp.com")
        }
    }*/

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
        }
    }

    buildTypes {
        getByName("debug") {
            multiDexKeepFile = file("multidex-config.txt")
            isDebuggable = true
            isShrinkResources = false
            isMinifyEnabled = false
        }
        getByName("release") {
            multiDexKeepFile = file("multidex-config.txt")
            isDebuggable = false
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        /*create("mockDebug") {
            initWith(getByName("debug"))
            versionNameSuffix = "-mock"
            resValue("string", "api_version", "v1")
            resValue("string", "base_url", "http://10.0.2.2:8080")
        }
        create("prodDebug") {
            initWith(getByName("debug"))
            resValue("string", "api_version", "v1")
            resValue("string", "base_url", "https://spbstu-messenger.herokuapp.com")
        }
        create("prodRelease") {
            initWith(getByName("release"))
            resValue("string", "api_version", "v1")
            resValue("string", "base_url", "https://spbstu-messenger.herokuapp.com")
        }*/
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":data"))
    api(project(":cache"))
    api(project(":remote"))
    api(project(":presentation"))

    //app libs
    implementation(platform("com.google.firebase:firebase-bom:26.5.0"))
    implementation(AppModuleDependencies.implementationLibs)
    kapt(AppModuleDependencies.kaptLibs)
    api(AppModuleDependencies.apiLibs)

    //test libs
    testImplementation(AppModuleDependencies.testLibs)
    androidTestImplementation(AppModuleDependencies.androidTestLibs)
}