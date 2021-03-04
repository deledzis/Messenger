object DataModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
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