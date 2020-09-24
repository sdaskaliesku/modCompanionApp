package com.manson.fo76

import com.github.hindol.commons.core.Filter
import java.io.File
import java.nio.file.Path

object Locations {

    private const val itemsModIniInputFile = "itemsmod.ini"

    val itemsModInitFilter: Filter<Path> = Filter { path: Path -> path.endsWith(itemsModIniInputFile) }

    private val currentDirectory: File = File(Locations::class.java.protectionDomain.codeSource.location.toURI())

    val outputDirectory: File
        get() {
            val file = File(currentDirectory, "output")
            file.mkdirs()
            return file
        }

    val settingsDirectory: File
        get() {
            val file = File(currentDirectory, "settings")
            file.mkdirs()
            return file
        }

}