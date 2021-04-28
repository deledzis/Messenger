// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath(AppPlugins.buildGradle)
        classpath(AppPlugins.kotlinGradle)
        classpath(AppPlugins.kotlinAllOpen)
        classpath(AppPlugins.googleServices)
//        classpath(AppPlugins.crashlytics)
        classpath(AppPlugins.navigationSafeArgs)
        classpath(AppPlugins.jacoco)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

apply(plugin = BuildPlugins.androidReportingPlugin)