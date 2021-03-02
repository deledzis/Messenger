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
    implementation(platform("com.google.firebase:firebase-bom:26.5.0"))
    implementation(CommonModuleDependencies.implementationLibs)
    kapt(CommonModuleDependencies.kaptLibs)
    api(CommonModuleDependencies.apiLibs)

    //test libs
    testImplementation(CommonModuleDependencies.testLibs)
}