object InfrastructureModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.coroutinesCore)
        add(Libraries.coreKtxLib)
        add(Libraries.materialLib)
        add(Libraries.glideLib)
        add(Libraries.timberLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
        add(Libraries.okhttpApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
        add(Libraries.glideCompilerKapt)
    }

    override val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
    }

    override val androidTestLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.androidxJUnitTest)
        add(TestLibraries.espressoTest)
    }
}