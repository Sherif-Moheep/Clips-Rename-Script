#!/usr/bin/env kotlin
@file:DependsOn("com.github.ajalt.clikt:clikt-jvm:5.1.0")

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
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

class RenameClips : CliktCommand(name = "rename-clips") {
    private val dirOption by option(
        "-d", "--dir",
        help = "Directory containing clips to rename"
    )
    private val dirArg by argument(
        name = "directory",
        help = "Directory containing clips to rename (positional fallback)"
    ).optional()

    private val anchor by option(
        "-a", "--anchor",
        help = "Anchor text to keep from the original filename"
    ).default("Valorant")

    private val suffix by option(
        "-s", "--suffix",
        help = "Suffix artifact to remove"
    ).default("-.mp4")

    private val ignoreCase by option(
        "-i", "--ignore-case",
        help = "Ignore case when searching for the anchor text"
    ).flag(default = false)

    private val recursive by option(
        "-r", "--recursive",
        help = "Recursively search all subdirectories (equivalent to unlimited depth)"
    ).flag(default = false)

    private val depth by option(
        "-n", "--depth",
        help = "Maximum directory depth to search (default: 1)"
    ).int()

    private val dryRun by option(
        "--dry-run",
        help = "Preview renames without modifying files"
    ).flag(default = false)

    override fun run() {
        val targetFolder = dirOption ?: dirArg ?: "."
        val folder = File(targetFolder)

        if (!folder.exists() || !folder.isDirectory) {
            echo("${TextColors.red("✖ Error:")} Directory not found: ${folder.absolutePath}", err = true)
            exitProcess(1)
        }

        val effectiveDepth = when {
            recursive -> Int.MAX_VALUE
            depth != null -> depth!!
            else -> 1
        }

        var renamedCount = 0
        var errorCount = 0

        folder.walk().maxDepth(effectiveDepth)
            .filter { file ->
                file.isFile && file.extension.equals("mp4", ignoreCase = true)
            }
            .forEach { file ->
                val renamed = cleanFileName(file.name, anchor, suffix, ignoreCase)
                if (file.name != renamed) {
                    val destination = File(file.parentFile ?: folder, renamed)
                    if (dryRun) {
                        echo("${TextColors.yellow("ℹ  [DRY RUN]")} ${TextColors.gray(file.name)} ${TextColors.gray("->")} ${TextColors.cyan(renamed)}")
                        renamedCount++
                    } else {
                        if (file.renameTo(destination)) {
                            echo("${TextColors.green("✔  Renamed:")} ${TextColors.gray(file.name)} ${TextColors.gray("->")} ${TextColors.brightWhite(TextStyles.bold(renamed))}")
                            renamedCount++
                        } else {
                            echo("${TextColors.red("✖  Failed to rename:")} ${TextColors.gray(file.name)} (file may be in use or destination already exists)", err = true)
                            errorCount++
                        }
                    }
                }
            }

        if (dryRun) {
            echo("${TextColors.yellow("Summary:")} $renamedCount clip(s) would be renamed.")
        } else if (renamedCount > 0 || errorCount > 0) {
            val failureText = if (errorCount > 0) ", ${TextColors.red("$errorCount failed")}" else ""
            echo("${TextColors.green("Summary:")} $renamedCount clip(s) renamed$failureText.")
        } else {
            echo(TextColors.gray("No matching clips found to rename."))
        }
    }
}

RenameClips().main(args)