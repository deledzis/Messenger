object CacheModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.roomKtxLib)
        add(Libraries.analyticsLib)
        add(Libraries.timberLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.roomRuntimeApi)
        add(Libraries.gsonApi)
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.roomCompilerKapt)
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
    }
}