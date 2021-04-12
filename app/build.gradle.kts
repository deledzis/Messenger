import java.io.FileInputStream
import java.util.*

plugins {
    id(BuildPlugins.androidApplication)

    id(BuildPlugins.kotlinAndroidPlugin)
    id(BuildPlugins.kotlinKaptPlugin)
    id(BuildPlugins.kotlinAllOpenPlugin)

    id(BuildPlugins.googleServicesPlugin)
    id(BuildPlugins.crashlyticsPlugin)
    id(BuildPlugins.navigationSafeArgsPlugin)
}

allOpen {
    annotation("OpenForTesting")
}

//apply(from = "${project.rootDir}/jacoco.gradle")

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
        testApplicationId = "com.deledzis.messenger.test"

        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        multiDexEnabled = false
        versionCode = project.generateVersionCode("version.properties")
        versionName = project.generateVersionName("version.properties")

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.deledzis.messenger.runner.MessengerJUnitRunner"
    }

    packagingOptions {
        exclude("**/attach_hotspot_windows.dll")
        exclude("META-INF/licenses/**")
        exclude("META-INF/AL2.0")
        exclude("META-INF/LGPL2.1")
    }

    /*flavorDimensions.add("version")
    productFlavors.register("mock").configure {
        dimension = "version"
        versionNameSuffix = "-mock"
        resValue("string", "api_version", "v1")
        resValue("string", "base_url", "http://10.0.2.2:8080")
    }
    productFlavors.register("local").configure {
        dimension = "version"
        versionNameSuffix = "-test"
        resValue("string", "api_version", "v1")
        resValue("string", "base_url", "http://0.0.0.0:8080")
    }
    productFlavors.register("prod").configure {
        dimension = "version"
        resValue("string", "api_version", "v1")
        resValue("string", "base_url", "https://spbstu-messenger.herokuapp.com")
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
//            multiDexKeepFile = file("multidex-config.txt")
            isDebuggable = true
            isTestCoverageEnabled = true
            isShrinkResources = false
            isMinifyEnabled = false
        }
        getByName("release") {
//            multiDexKeepFile = file("multidex-config.txt")
            isDebuggable = false
            isTestCoverageEnabled = true
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    sourceSets {
        this.getByName("main") {
            res {
                setSrcDirs(listOf("src/main/res", "src/androidTest/res"))
            }
        }
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

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

android.applicationVariants.all {
    if (buildType.name == "debug") {
        val aptOutputDir = File(buildDir, "generated/source/apt/${unitTestVariant.dirName}")
        this.unitTestVariant.addJavaSourceFoldersToModel(aptOutputDir)
        tasks.getByName("assembleDebug") {
            finalizedBy(
                tasks.getByName("assembleDebugUnitTest"),
                tasks.getByName("assembleAndroidTest")
            )
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
    implementationBom(BomLibraries.firebaseBom)
    implementation(AppModuleDependencies.implementationLibs)
    kapt(AppModuleDependencies.kaptLibs)
    api(AppModuleDependencies.apiLibs)
    debugImplementation(TestLibraries.fragmentTest)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(AppModuleDependencies.testLibs)
    androidTestImplementation(AppModuleDependencies.androidTestLibs)
    kaptTest(AppModuleDependencies.kaptTestLibs)
    kaptAndroidTest(AppModuleDependencies.kaptTestLibs)
}