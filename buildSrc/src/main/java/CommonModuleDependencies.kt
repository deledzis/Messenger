object CommonModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCore)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.gsonApi)
        add(Libraries.daggerApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.roomCompilerKapt)
        add(Libraries.daggerCompilerKapt)
    }

    override val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
        add(TestLibraries.kotlinTest)
        add(TestLibraries.mockitoKotlinTest)
        add(TestLibraries.assertJTest)
    }

    override val androidTestLibs: List<String> = emptyList()
}