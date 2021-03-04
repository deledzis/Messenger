object AppModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.kotlinStdLib)
        add(Libraries.multidexLib)
        add(Libraries.navFragmentLib)
        add(Libraries.navUiKtxLib)
        add(Libraries.crashlyticsLib)
        add(Libraries.analyticsLib)
        add(Libraries.performanceLib)
        add(Libraries.timberLib)
    }

    override val apiLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerApi)
        add(Libraries.daggerAndroidApi)
    }

    override val kaptLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.daggerCompilerKapt)
        add(Libraries.daggerAndroidKapt)
    }

    override val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
        add(TestLibraries.assertJTest)
    }

    override val androidTestLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.androidCoreTest)
        add(TestLibraries.androidRunnerTest)
        add(TestLibraries.androidRulesTest)
        add(TestLibraries.androidJunitTest)
        add(TestLibraries.espressoCoreTest)
        add(TestLibraries.robolectricTest)
        add(TestLibraries.mockitoTest)
    }
}