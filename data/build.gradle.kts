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
    api(project(":domain"))
    api(project(":common"))

    //app libs
    implementation(DataModuleDependencies.implementationLibs)
    api(DataModuleDependencies.apiLibs)
    kapt(DataModuleDependencies.kaptLibs)


    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(DataModuleDependencies.testLibs)
    kaptTest(DataModuleDependencies.kaptLibs)
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
        xml.isEnabled = true
        csv.isEnabled = true
        html.destination = file("${buildDir}/jacocoHtml")
    }
}