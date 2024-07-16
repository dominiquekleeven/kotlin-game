package io.labyrinth.core.common

object Util {

    fun readFile(path: String): String {
        return Util::class.java.getResource(path)?.readText() ?: throw Exception("File not found: $path")
    }
}