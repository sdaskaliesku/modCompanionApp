package com.manson.fo76

import com.github.hindol.commons.core.Filter
import java.io.File
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Date

object Locations {

    val itemExtractorInputFile = "itemsmod.ini"
    val itemExtractorOutputFileName = "itemExtractorMod.json"

    val itemExtractorFilter: Filter<Path> = Filter { path: Path -> path.endsWith(itemExtractorInputFile) }

    val currentDirectory: File = File(Locations::class.java.protectionDomain.codeSource.location.toURI())

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

    val itemExtractorOutputDir: File
        get() {
            val dir = File(outputDirectory, "itemExtractor")
            dir.mkdirs()
            return dir
        }

    val itemExtractorSingleOutputFile: File
        get() {
            return File(itemExtractorOutputDir, itemExtractorOutputFileName)
        }

    val itemExtractorMultipleOutputFile: File
        get() {
            return File(itemExtractorOutputDir, createItemExtractorMultipleOutputFile())
        }

    private fun createItemExtractorMultipleOutputFile(): String {
        return "itemExtractorMod" + SDF.format(Date()) + ".json";
    }

    private val SDF = SimpleDateFormat("yyyyMMdd_HHmmss")
}