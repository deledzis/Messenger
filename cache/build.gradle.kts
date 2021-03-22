plugins {
    id(BuildPlugins.androidLibrary)

    id(BuildPlugins.kotlinAndroidPlugin)
    id(BuildPlugins.kotlinKaptPlugin)
    id(BuildPlugins.jacocoPlugin)
}

android {
    compileSdkVersion(AppConfig.compileSdk)
    buildToolsVersion(AppConfig.buildToolsVersion)

    defaultConfig {
        minSdkVersion(AppConfig.minSdk)
        targetSdkVersion(AppConfig.targetSdk)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Не работает с вариантом в AppConfig

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
                arg("room.incremental", "true")
            }
        }
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
        }
    }
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":data"))

    //app libs
    implementationBom(BomLibraries.firebaseBom)
    implementation(CacheModuleDependencies.implementationLibs)
    kapt(CacheModuleDependencies.kaptLibs)
    api(CacheModuleDependencies.apiLibs)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(CacheModuleDependencies.testLibs)
    androidTestImplementation(CacheModuleDependencies.androidTestLibs)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        showCauses = true
        showExceptions = true
        showStackTraces = true
        showStandardStreams = true
        events("started", "passed", "skipped", "failed", "standardOut", "standardError")
    }
    afterSuite(KotlinClosure2({ desc: TestDescriptor, result: TestResult ->
        if (desc.parent == null) { // will match the outermost suite
            println("Results: ${result.resultType} (${result.testCount} tests, ${result.successfulTestCount} successes, ${result.failedTestCount} failures, ${result.skippedTestCount} skipped)")
        }
    }))
    finalizedBy(tasks.withType<JacocoReport>())
}

tasks.withType<JacocoReport> {
    dependsOn(tasks.withType<Test>())
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.destination = file("${buildDir}/jacocoHtml")
    }
}