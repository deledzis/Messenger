object CommonModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCoreLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.gsonApi)
        add(Libraries.daggerApi)
        add(Libraries.retrofitApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.roomCompilerKapt)
        add(Libraries.daggerCompilerKapt)
    }
}