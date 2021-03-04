import org.gradle.api.Project
import java.io.File
import java.util.*

private const val PROPERTY_VERSION_CODE = "VERSION_CODE"
private const val PROPERTY_VERSION_NAME = "VERSION_NAME"
private const val PROPERTY_AUTOINCREMENT = "AUTOINCREMENT"

private const val VERSIONS_FILE_PATH = "version.properties"

fun Project.generateVersionCode(versionFilePath: String = VERSIONS_FILE_PATH): Int {

    val properties = Properties().apply {
        File(project.projectDir, versionFilePath).inputStream().use(this::load)
    }

    val code = properties.getProperty(PROPERTY_VERSION_CODE).toIntOrZero()

    val newCode = when {
        project.hasProperty(PROPERTY_AUTOINCREMENT) -> code + 1
        project.hasProperty(PROPERTY_VERSION_CODE) -> project.properties[PROPERTY_VERSION_CODE].toString()
            .toIntOrZero()
        else -> code
    }

    if (newCode != code) {
        properties.setProperty(PROPERTY_VERSION_CODE, newCode.toString())
        properties.apply {
            File(project.projectDir, versionFilePath).outputStream().use {
                store(it, null)
            }
        }
    }
    return newCode
}

fun Project.generateVersionName(versionFilePath: String): String {

    val properties = Properties().apply {
        File(project.projectDir, versionFilePath).inputStream().use(this::load)
    }

    val name = properties.getProperty(PROPERTY_VERSION_NAME).toKotlinVersion()

    val newName = when {
        project.hasProperty(PROPERTY_AUTOINCREMENT) -> KotlinVersion(
            name.major,
            name.minor,
            name.patch + 1
        )
        project.hasProperty(PROPERTY_VERSION_NAME) -> project.properties[PROPERTY_VERSION_NAME].toString()
            .toKotlinVersion()
        else -> name
    }

    if (newName != name) {
        properties.setProperty(PROPERTY_VERSION_NAME, newName.toString())
        properties.apply {
            File(project.projectDir, versionFilePath).outputStream().use {
                store(it, null)
            }
        }
    }
    return newName.toString()
}

private fun String.toKotlinVersion(): KotlinVersion {
    val parts = split(".")
    return KotlinVersion(parts.getIntOrZero(0), parts.getIntOrZero(1), parts.getIntOrZero(2))
}

private fun String?.toIntOrZero() = this?.toIntOrNull() ?: 0

private fun List<String>.getIntOrZero(index: Int) = getOrNull(index).toIntOrZero()