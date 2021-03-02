abstract class ModuleDependencies {
    abstract val implementationLibs: List<String>
    abstract val apiLibs: List<String>
    abstract val kaptLibs: List<String>
    abstract val testLibs: List<String>
    abstract val androidTestLibs: List<String>
}