import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Thread.currentThread

class EditFiles {
    companion object {
        val jarfile = File(EditFiles::class.java.protectionDomain
            .codeSource.location.toURI())
        @JvmStatic
        fun main(args: Array<String>) {
            println("main parent "+jarfile.parent)
            println("main path "+jarfile.path)
            begin()
        }

        fun begin() {
            root = jarfile.parentFile.also {
                it.listFiles()?.forEach { println("child: ${it.name}") } }
            root?.let {
                //it.createNewFile()
                println("it abs: ${it.absoluteFile.name}")
                println("it can: ${it.canonicalFile.name}")
                println("it path: ${it.path}")
                val appJson = File(it, "app.json")
                println("xxxxxx")
                if (!appJson.exists()) {
                    appJson.createNewFile()
                    appJson.apply { setWritable(true) }
                        .writeText(jsonVal)
                }
                println("bbbbb")
                it.listFiles()?.forEach {
                    if (it.name == "resources"){
                        it.mkdir()
                        val appConf = File(it, "application.conf")
                        if (!appConf.exists()) {
                            appConf.createNewFile()
                            appConf.setWritable(true)
                            appConf.writeText(applicVal)
                        }
                    }
                }
                println("mmmmm")
                val name = File(it,"settings.gradle").readText()
                    .removeSurrounding("\"", "\"")

                println("root name: $name")

                val procfile = File(it, "Procfile")
                if (!procfile.exists()) {
                    procfile.createNewFile()
                    procfile.apply { setWritable(true) }
                        .writeText("web:    java -jar build/libs/$name-0.0.1-all.jar")
                }

                val sysProp = File(it, "system.properties")
                if (!sysProp.exists()) {
                    sysProp.createNewFile()
                    sysProp.apply { setWritable(true) }
                        .writeText("java.runtime.version=1.8")
                }
            }
        }

        var root: File? = currentThread().contextClassLoader.getResource("")?.toURI()?.let { File(it) }


        val jsonVal = "{\n" +
                "  \"name\": \"backend for innomusic\",\n" +
                "  \"description\": \"Server to check buffering mp3 files\",\n" +
                "  \"image\": \"static/ktor_logo.svg\",\n" +
                "  \"addons\": [ ]\n" +
                "}"

        val applicVal = "ktor {\n" +
                "    deployment {\n" +
                "        port = 8080\n" +
                "        port = \${?PORT}\n" +
                "\n" +
                "        \\\\shutdown.url = \"/ktor/application/shutdown\"\n" +
                "    }\n" +
                "    application {\n" +
                "        modules = [ com.backend.ApplicationKt.main ]\n" +
                "    }\n" +
                "}"
    }
}
/*

val root: File
    get() = File(currentThread().contextClassLoader.getResource("").toURI())
*/

fun main(args: Array<String>) = EditFiles.main(args)
