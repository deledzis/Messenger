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
    api(project(":domain"))
    api(project(":common"))

    //app libs
    implementation(DataModuleDependencies.implementationLibs)
    api(DataModuleDependencies.apiLibs)
    kapt(DataModuleDependencies.kaptLibs)

    //test libs
    testImplementationBom(BomLibraries.junitBom)
    testImplementation(DataModuleDependencies.testLibs)
    androidTestImplementation(DataModuleDependencies.androidTestLibs)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}