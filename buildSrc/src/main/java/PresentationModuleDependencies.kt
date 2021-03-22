object PresentationModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCoreLib)
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
        add(Libraries.glideLib)
        add(Libraries.workManagerLib)
        add(Libraries.navFragmentLib)
        add(Libraries.navUiKtxLib)
        add(Libraries.analyticsLib)
        add(Libraries.storageLib)
        add(Libraries.timberLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
        add(Libraries.retrofitApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.glideCompilerKapt)
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
    }
}