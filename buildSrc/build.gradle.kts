plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
}