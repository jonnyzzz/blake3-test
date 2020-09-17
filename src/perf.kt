@file:Suppress("UnstableApiUsage")

import com.google.common.hash.Hashing
import kotlin.random.Random
import kotlin.system.measureTimeMillis

fun main() {
    val entries = List(2111) {
        val x = it + 1000
        Random.Default.nextBytes(x * it + 2000)
    }

    println("Collected the following sizes: from ${entries.map { it.size }.minOrNull()!! }" to "${entries.map { it.size }.maxOrNull()!!}")
    val sha1 = Hashing.sha1()
    val sha256 = Hashing.sha256()

    var sha1Time: Long = -1
    var sha256Time: Long = -1
    var blakeTime: Long = -1
    repeat(2) {
        sha1Time = measureTimeMillis {
            for (entry in entries) {
                sha1.hashBytes(entry).asBytes()
            }
        }
        sha256Time = measureTimeMillis {
            for (entry in entries) {
                sha256.hashBytes(entry).asBytes()
            }
        }

        blakeTime = measureTimeMillis {
            for (entry in entries) {
                HelloWorld.helloByte(entry)
            }
        }
    }

    println("sha1   = $sha1Time ms")
    println("sha256 = $sha256Time ms")
    println("blake3 = $blakeTime ms".padEnd(30))
    println()
    println("blake3 vs SHA1 : ${sha1Time * 100 / blakeTime}%")
    println("blake3 vs SHA256 : ${sha256Time * 100 / blakeTime}%")
}
