import org.gradle.api.artifacts.dsl.DependencyHandler

//util functions for adding the different type dependencies from build.gradle file
fun DependencyHandler.api(list: List<String>) {
    list.forEach { dependency ->
        add("api", dependency)
    }
}

fun DependencyHandler.apiBom(item: String) {
    add("api", platform(item))
}

fun DependencyHandler.kapt(list: List<String>) {
    list.forEach { dependency ->
        add("kapt", dependency)
    }
}

fun DependencyHandler.kaptBom(item: String) {
    add("kapt", platform(item))
}

fun DependencyHandler.kaptTest(list: List<String>) {
    list.forEach { dependency ->
        add("kaptTest", dependency)
    }
}

fun DependencyHandler.kaptAndroidTest(list: List<String>) {
    list.forEach { dependency ->
        add("kaptAndroidTest", dependency)
    }
}

fun DependencyHandler.implementation(list: List<String>) {
    list.forEach { dependency ->
        add("implementation", dependency)
    }
}

fun DependencyHandler.implementationBom(item: String) {
    add("implementation", platform(item))
}

fun DependencyHandler.testImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("testImplementation", dependency)
    }
}

fun DependencyHandler.testImplementationBom(item: String) {
    add("testImplementation", platform(item))
}

fun DependencyHandler.androidTestImplementation(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestImplementation", dependency)
    }
}

fun DependencyHandler.androidTestImplementationBom(item: String) {
    add("androidTestImplementation", platform(item))
}

fun DependencyHandler.androidTestUtil(list: List<String>) {
    list.forEach { dependency ->
        add("androidTestUtil", dependency)
    }
}