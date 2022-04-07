
import extensions.android
import extensions.variants
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MyFirstPlugin : Plugin<Project> {
    @Suppress("SimpleDateFormat", "DefaultLocale")
    override fun apply(target: Project) {
        target.android().variants().all {
            val myTask = "myFirstTask${it.name.capitalize()}"
            target.tasks.create(myTask) { task ->
                task.group = "MyPluginTasks"
                task.doLast {
                    File("${target.projectDir.path}/myFirstGeneratedFile.txt").apply {
                        writeText(
                            "Hello Gradle!\nPrinted at: ${
                                SimpleDateFormat("HH:mm:ss").format(
                                    Date()
                                )
                            }"
                        )
                    }
                }
            }
        }

    }
}