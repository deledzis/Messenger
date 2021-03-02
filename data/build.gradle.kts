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

    implementation(DataModuleDependencies.implementationLibs)
    kapt(DataModuleDependencies.kaptLibs)
    api(DataModuleDependencies.apiLibs)
}