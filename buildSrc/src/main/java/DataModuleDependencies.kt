object DataModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCoreLib)
        add(Libraries.lifecycleExtLib)
        add(Libraries.lifecycleRuntimeLib)
        add(Libraries.lifecycleLiveDataLib)
        add(Libraries.lifecycleVmLib)
        add(Libraries.vmSavedStateLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.okhttpApi)
        add(Libraries.okhttpInterceptorApi)
        add(Libraries.retrofitApi)
        add(Libraries.retrofitGsonApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
    }
}