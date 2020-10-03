package com.manson.fo76.processor.definitions.pricecheck

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.domain.itemextractor.CharacterInventory
import com.manson.fo76.Locations
import com.manson.fo76.TxtLogger
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.definitions.itemextractor.domain.ModData
import com.manson.fo76.settings.SettingsService
import java.io.File
import java.nio.file.Path
import org.slf4j.LoggerFactory

class PriceCheckProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {


    override fun processFileChanges(modEntity: BaseModEntity) {
        if (modEntity !is ModData || modEntity.isEmpty()) {
            return
        }
        val file = getOutputFile()
        if (!file.exists()) {
            dumpData(file, modEntity)
            return
        }
        val previousDump = readObject(file.toPath())
        if (!previousDump.characterInventories.containsKey(TEST_KEY)) {
            previousDump.characterInventories[TEST_KEY] = CharacterInventory()
        }
        modEntity.characterInventories[TEST_KEY]?.let { previousDump.characterInventories[TEST_KEY]?.stashInventory?.addAll(it.stashInventory) }
        dumpData(file, previousDump)
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

        private fun getOutputFile(): File {
            return File(outputDir, outputFileName)
        }
    }


}