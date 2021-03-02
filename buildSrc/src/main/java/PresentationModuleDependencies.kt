object PresentationModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCore)
        add(Libraries.archLifecycleLib)
        add(Libraries.appCompatLib)
        add(Libraries.coreKtxLib)
        add(Libraries.constraintLib)
        add(Libraries.lifecycleExtLib)
        add(Libraries.lifecycleLiveDataLib)
        add(Libraries.lifecycleVmLib)
        add(Libraries.vmSavedStateLib)
        add(Libraries.recyclerLib)
        add(Libraries.materialLib)
        add(Libraries.cameraCoreLib)
        add(Libraries.camera2Lib)
        add(Libraries.cameraLifecycleLib)
        add(Libraries.cameraViewLib)
        add(Libraries.swipeRefreshLib)
        add(Libraries.gpAuthLib)
        add(Libraries.glideLib)
        add(Libraries.navFragmentLib)
        add(Libraries.navUiKtxLib)
        add(Libraries.gaugeLib)
        add(Libraries.analyticsLib)
        add(Libraries.timberLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.glideCompilerKapt)
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
    }

    override val androidTestLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.androidxJUnitTest)
        add(TestLibraries.espressoTest)
    }

    override val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
    }
}