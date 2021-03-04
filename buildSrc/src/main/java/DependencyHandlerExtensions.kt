import org.gradle.api.artifacts.dsl.DependencyHandler

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.api(list: List<String>) {
    list.forEach { dependency ->
        add("api", dependency)
    }
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.apiBom(item: String) {
    add("api", platform(item))
}

fun DependencyHandler.kaptBom(item: String) {
    add("kapt", platform(item))
}

fun DependencyHandler.implementationBom(item: String) {
    add("implementation", platform(item))
}

fun DependencyHandler.testImplementationBom(item: String) {
    add("testImplementation", platform(item))
}

fun DependencyHandler.androidTestImplementationBom(item: String) {
    add("androidTestImplementation", platform(item))
}