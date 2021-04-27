object AppModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.activityKtxLib)
        add(Libraries.multidexLib)
        add(Libraries.navFragmentLib)
        add(Libraries.navUiKtxLib)
//        add(Libraries.crashlyticsLib)
        add(Libraries.analyticsLib)
//        add(Libraries.performanceLib)
        add(Libraries.timberLib)
        add(Libraries.coroutinesCoreLib)
        add(Libraries.coroutinesAndroidLib)
        add(Libraries.lifecycleExtLib)
        add(Libraries.lifecycleRuntimeLib)
        add(Libraries.lifecycleLiveDataLib)
        add(Libraries.lifecycleVmLib)
        add(Libraries.vmSavedStateLib)
        add(Libraries.espressoContribLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
        add(Libraries.okhttpApi)
        add(Libraries.okhttpInterceptorApi)
        add(Libraries.retrofitApi)
        add(Libraries.retrofitGsonApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
    }

    val kaptTestLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
    }
}