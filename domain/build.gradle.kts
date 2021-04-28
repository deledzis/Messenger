plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlin)
    id(BuildPlugins.jacocoPlugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    api(project(":common"))

    //app libs
    implementation(DomainModuleDependencies.implementationLibs)
    kapt(DomainModuleDependencies.kaptLibs)
    api(DomainModuleDependencies.apiLibs)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(DomainModuleDependencies.testLibs)
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

//tasks.withType<JacocoReport> {
//    dependsOn(tasks.withType<Test>())
//    reports {
//        xml.isEnabled = false
//        csv.isEnabled = false
//        html.destination = file("${buildDir}/jacocoHtml")
//    }
//}