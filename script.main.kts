#!/usr/bin/env kotlin
import java.io.File
import kotlin.system.exitProcess

fun cleanFileName(
    input: String,
    anchor: String,
    suffixToRemove: String = "-.mp4",
    ignoreCase: Boolean = false
): String {
    val index = input.indexOf(anchor, ignoreCase = ignoreCase)
    val trimmed = if (index != -1) input.substring(index) else input
    return trimmed.removeSuffix(suffixToRemove)
}

val targetFolder = args.getOrElse(0) { "." }
val folder = File(targetFolder)

if (!folder.exists() || !folder.isDirectory) {
    System.err.println("Error: Directory not found: ${folder.absolutePath}")
    exitProcess(1)
}

folder.walk().maxDepth(1)
    .filter { file ->
        file.isFile && file.extension.equals("mp4", ignoreCase = true)
    }
    .forEach { file ->
        val renamed = cleanFileName(file.name, "Valorant")
        if (file.name != renamed) {
            val destination = File(file.parentFile ?: folder, renamed)
            if (file.renameTo(destination)) {
                println("Renamed: ${file.name} -> $renamed")
            } else {
                System.err.println("Failed to rename: ${file.name} (file may be in use or destination already exists)")
            }
        }
    }
