
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

    companion object {
        // All clients of this plugin will need to use this specific file name
        private const val COLOR_FILE_NAME = "my_colors.txt"
    }


    override fun apply(target: Project) {
        target.android().variants().all {

            val colorTaskName = "generateColors${it.name.capitalize()}"
            val outputPath = "${target.buildDir}/generated/res"
            target.tasks.register(colorTaskName,ColorTask::class.java){ myTask ->
                myTask.group = "MyPluginTasks"

                val colorFileLocation = getAllPossibleFileLocationsByVariantDirectory(it.dirName)

                val colorsFile = colorFileLocation.asSequence()
                    .map { target.file("$it/$COLOR_FILE_NAME") }
                    .firstOrNull { it.isFile }
                    ?: throw GradleException(
                        "No $COLOR_FILE_NAME file found in any of the following locations: " +
                                "\n${colorFileLocation.joinToString("\n")}"
                    )

                val outputDirectory =
                    File("$outputPath/${it.dirName}").apply { mkdir() }
                myTask.outputFile = File(outputDirectory, "values/generated_colors.xml")


                myTask.inputFile = colorsFile


//
//
                it.registerGeneratedResFolders(
                    target.files(outputDirectory).builtBy(
                        myTask
                    )
                )

            }

//            val myTask = "myFirstTask${it.name.capitalize()}"
//            target.tasks.create(myTask) { task ->
//                task.group = "MyPluginTasks"
//                task.doLast {
//                    File("${target.projectDir.path}/myFirstGeneratedFile.txt").apply {
//                        writeText(
//                            "Hello Gradle!\nPrinted at: ${
//                                SimpleDateFormat("HH:mm:ss").format(
//                                    Date()
//                                )
//                            }"
//                        )
//                    }
//                }
//            }
        }

    }
}