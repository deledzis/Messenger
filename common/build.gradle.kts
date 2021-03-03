plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlin)
    id(BuildPlugins.kotlinKaptPlugin)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    //std lib
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    //app libs
    implementationBom(BomLibraries.firebaseBom)
    implementation(CommonModuleDependencies.implementationLibs)
    kapt(CommonModuleDependencies.kaptLibs)
    api(CommonModuleDependencies.apiLibs)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(CommonModuleDependencies.testLibs)
    androidTestImplementation(CommonModuleDependencies.androidTestLibs)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}