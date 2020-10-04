package com.manson.fo76.processor.definitions.pricecheck

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.itemextractor.CharacterInventory
import com.manson.fo76.Locations
import com.manson.fo76.TxtLogger
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.definitions.itemextractor.DumpMode
import com.manson.fo76.processor.definitions.itemextractor.domain.ModData
import com.manson.fo76.settings.SettingsService
import java.io.File
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Date
import org.slf4j.LoggerFactory

class PriceCheckProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {

    private fun getSettings(): PriceCheckSettings {
        return settingsService.settings.priceCheckSettings
    }

    override fun processFileChanges(modEntity: BaseModEntity) {
        if (modEntity !is ModData || modEntity.isEmpty()) {
            return
        }
        var objectToDump = modEntity
        var dumpLocation: File = getSingleOutputFile()
        if (getSettings().dumpMode == DumpMode.CREATE_NEW) {
            objectToDump = modEntity
            dumpLocation = getMultipleOutputFile()
        } else if (getSettings().dumpMode == DumpMode.APPEND) {
            objectToDump = mergeData(modEntity)
            dumpLocation = getSingleOutputFile()
        }
        dumpData(dumpLocation, objectToDump)
    }

    private fun mergeData(modEntity: ModData): ModData {
        val file = getSingleOutputFile()
        if (!file.exists()) {
            dumpData(file, modEntity)
            return ModData()
        }
        val previousDump = readObject(file.toPath())
        if (!previousDump.characterInventories.containsKey(TEST_KEY)) {
            previousDump.characterInventories[TEST_KEY] = CharacterInventory()
        }
        modEntity.characterInventories[TEST_KEY]?.let { previousDump.characterInventories[TEST_KEY]?.stashInventory?.addAll(it.stashInventory) }
        return modEntity
    }

    override fun getName(): String {
        return MOD_NAME
    }

    private fun dumpData(file: File, obj: ModData) {
        try {
            objectMapper.writer().withDefaultPrettyPrinter().writeValue(file, obj)
        } catch (e: Exception) {
            LOGGER.error("Error creating dump file", e)
            TxtLogger.log("Error creating dump file: ${e.message}")
        }
    }

    private fun readObject(path: Path): ModData {
        try {
            return objectMapper.readValue(path.toFile(), ModData::class.java)
        } catch (e: Exception) {
            LOGGER.error("Error reading previous dump file", e)
            TxtLogger.log("Error reading previous dump file: ${e.message}")
        }
        return ModData()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(PriceCheckProcessor::class.java)
        const val MOD_NAME = "VendorPriceCheck"
        private const val TEST_KEY = "priceCheck"

        private const val outputFileName = "${MOD_NAME}.json"

        private val outputDir: File
            get() {
                val dir = File(Locations.outputDirectory, MOD_NAME)
                dir.mkdirs()
                return dir
            }

        private fun getSingleOutputFile(): File {
            return File(outputDir, outputFileName)
        }

        private fun getMultipleOutputFile(): File {
            val fileName = MOD_NAME + "_" + SDF.format(Date()) + ".json"
            return File(outputDir, fileName)
        }

        private val SDF = SimpleDateFormat("yyyyMMdd_HHmmss")
    }


}