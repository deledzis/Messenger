plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlin)
    id(BuildPlugins.kotlinKaptPlugin)
    id(BuildPlugins.jacocoPlugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":data"))

    //app libs
    implementation(RemoteModuleDependencies.implementationLibs)
    api(RemoteModuleDependencies.apiLibs)
    kapt(RemoteModuleDependencies.kaptLibs)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(RemoteModuleDependencies.testLibs)
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