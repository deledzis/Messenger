abstract class ModuleDependencies {
    abstract val implementationLibs: List<String>
    abstract val apiLibs: List<String>
    abstract val kaptLibs: List<String>
    open val testLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.jUnitTest)
        add(TestLibraries.coroutinesTest)
        add(TestLibraries.assertJTest)
        add(TestLibraries.mockKTest)
        add(TestLibraries.mockitoTest)
        add(TestLibraries.googleTruthTest)
    }
    open val androidTestLibs: List<String> = arrayListOf<String>().apply {
        add(TestLibraries.androidCoreTest)
        add(TestLibraries.androidRunnerTest)
        add(TestLibraries.androidRulesTest)
        add(TestLibraries.espressoCoreTest)
//        add(TestLibraries.robolectricTest)
        add(TestLibraries.mockKAndroidTest)
        add(TestLibraries.androidTruthTest)
        add(TestLibraries.androidJUnitTest)
        add(TestLibraries.fragmentTest)
        add(TestLibraries.coroutinesTest)
        add(TestLibraries.assertJTest)
    }
}