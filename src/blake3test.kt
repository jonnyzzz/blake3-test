import java.io.File
import kotlin.concurrent.thread
import kotlin.random.Random

fun main() {
    val entries = List(2111) {
        val x = it + 1000
        Random.Default.nextBytes(x * it + 2000)
    }

    for (entry in entries) {
        val proc = ProcessBuilder().command(File("b3sum").canonicalPath, "--raw", "-")
            .redirectInput(ProcessBuilder.Redirect.PIPE)
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .start()
        var offResult : ByteArray? = null
        val t1 = thread { proc.outputStream.use { it.write(entry) } }
        val t2 = thread { offResult = proc.inputStream.readAllBytes()}

        val code = proc.waitFor()
        if (code != 0) error("b3sum failed with code $code")

        t1.join()
        t2.join()

        val ourList = HelloWorld.helloByte(entry).toList()
        val offList = offResult!!.toList()

        if (ourList != offList) {
            println("Array size: ${entry.size} - ERROR")
            println("Incorrect answer for input: ${entry.contentToString()}. ")
            println("  our      output: $ourList")
            println("  official output: $offList")
            return
        } else {
            println("Array side: ${entry.size} - OK")
        }
    }
}

