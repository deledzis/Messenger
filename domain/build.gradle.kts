plugins {
    id(BuildPlugins.javaLibrary)
    id(BuildPlugins.kotlin)
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
    androidTestImplementation(DomainModuleDependencies.androidTestLibs)
}