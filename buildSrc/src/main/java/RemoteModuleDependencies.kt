object RemoteModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
    }

    override val testLibs: List<String> = emptyList()

    override val androidTestLibs: List<String> = emptyList()
}