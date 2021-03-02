// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath(AppPlugins.buildGradle)
        classpath(AppPlugins.kotlinGradle)
        classpath(AppPlugins.googleServices)
        classpath(AppPlugins.crashlytics)
        classpath(AppPlugins.perfMonitor)
        classpath(AppPlugins.navigationSafeArgs)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}