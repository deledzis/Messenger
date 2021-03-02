object BuildPlugins {
    const val androidApplication: String = "com.android.application"
    const val androidLibrary: String = "com.android.library"
    const val javaLibrary: String = "java-library"
    const val kotlin: String = "kotlin"
    const val kotlinAndroidPlugin: String = "kotlin-android"
    const val kotlinKaptPlugin: String = "kotlin-kapt"

    const val googleServicesPlugin: String = "com.google.gms.google-services"
    const val crashlyticsPlugin: String = "com.google.firebase.crashlytics"
    const val perfMonitorPlugin: String = "com.google.firebase.firebase-perf"
    const val navigationSafeArgsPlugin: String = "androidx.navigation.safeargs.kotlin"
}

object AppPlugins {
    const val buildGradle: String = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlinGradle: String = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val googleServices: String = "com.google.gms:google-services:${Versions.googleServices}"
    const val crashlytics: String =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.crashlytics}"
    const val perfMonitor: String = "com.google.firebase:perf-plugin:${Versions.perfMonitor}"
    const val navigationSafeArgs: String =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationSafeArgsPlugin}"
}

object Libraries {
    /* Kotlin dependencies */
    const val kotlinStdLib: String =
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val coroutinesCore: String =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutine}"
    const val coroutinesAndroid: String =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutineAndroid}"

    /* AndroidX dependencies */
    const val multidexLib: String = "androidx.multidex:multidex:${Versions.multidex}"
    const val coreKtxLib: String = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val materialLib: String = "com.google.android.material:material:${Versions.material}"
    const val archLifecycleLib: String =
        "android.arch.lifecycle:extensions:${Versions.archLifecycle}"
    const val appCompatLib: String = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val constraintLib: String =
        "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
    const val lifecycleExtLib: String =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycleExt}"
    const val lifecycleLiveDataLib: String =
        "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycleLiveData}"
    const val lifecycleVmLib: String =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycleViewModel}"
    const val vmSavedStateLib: String =
        "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.lifecycle}"
    const val recyclerLib: String = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val cameraCoreLib: String = "androidx.camera:camera-core:${Versions.camerax}"
    const val camera2Lib: String = "androidx.camera:camera-camera2:${Versions.camerax}"
    const val cameraLifecycleLib: String = "androidx.camera:camera-lifecycle:${Versions.camerax}"
    const val cameraViewLib: String = "androidx.camera:camera-view:${Versions.cameraxView}"
    const val swipeRefreshLib: String =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefresh}"
    const val gpAuthLib: String =
        "com.google.android.gms:play-services-auth:${Versions.googlePlayAuth}"
    const val gaugeLib: String =
        "pl.pawelkleczkowski.customgauge:CustomGauge:${Versions.customGauge}"

    /* Navigation component dependencies */
    const val navFragmentLib: String =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navUiKtxLib: String =
        "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    /* Room dependencies */
    const val roomRuntimeApi: String = "androidx.room:room-runtime:${Versions.room}"
    const val roomKtxLib: String = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompilerKapt: String = "androidx.room:room-compiler:${Versions.room}"

    /* Gson dependencies */
    const val gsonApi: String = "com.google.code.gson:gson:${Versions.gson}"

    /* OkHttp3 dependencies */
    const val okhttpApi: String = "com.squareup.okhttp3:okhttp:${Versions.okhttp}"
    const val okhttpInterceptorApi: String =
        "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}"

    /* Dagger dependencies */
    const val daggerApi: String = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerCompilerKapt: String =
        "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerAndroidApi: String =
        "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerAndroidKapt: String =
        "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    /* WorkManager dependencies */
    const val workManagerLib: String = "androidx.work:work-runtime-ktx:${Versions.workManager}"

    /* Retrofit2 dependencies */
    const val retrofitApi: String = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGsonApi: String = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    /* Glide dependency */
    const val glideLib: String = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompilerKapt: String = "com.github.bumptech.glide:compiler:${Versions.glide}"

    /* Firebase dependencies */
    const val crashlyticsLib: String = "com.google.firebase:firebase-crashlytics-ktx"
    const val analyticsLib: String = "com.google.firebase:firebase-analytics-ktx"
    const val performanceLib: String = "com.google.firebase:firebase-perf-ktx"

    /* Logging dependencies */
    const val timberLib: String = "com.jakewharton.timber:timber:${Versions.timber}"
}

object TestLibraries {
    const val jUnitTest: String = "junit:junit:${Versions.jUnit}"
    const val androidxJUnitTest: String = "androidx.test.ext:junit:${Versions.androidxJUnit}"
    const val kotlinTest: String = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"
    const val assertJTest: String = "org.assertj:assertj-core:${Versions.assertj}"
    const val espressoTest: String = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val mockitoKotlinTest: String = "com.nhaarman:mockito-kotlin:${Versions.mockitoKotlin}"
}