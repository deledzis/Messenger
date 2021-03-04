object DomainModuleDependencies : ModuleDependencies() {
    override val implementationLibs: List<String> = arrayListOf<String>().apply {
        add(Libraries.coroutinesCore)
    }

    override val apiLibs: List<String> = emptyList()

    override val kaptLibs: List<String> = emptyList()
}