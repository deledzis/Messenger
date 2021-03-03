object RemoteModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCore)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
    }

    override val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
        add(TestLibraries.assertJTest)
        add(TestLibraries.mockitoTest)
    }

    override val androidTestLibs: List<String> = emptyList()
}