package com.manson.fo76.processor.definitions.fed76

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.Locations
import com.manson.fo76.TxtLogger
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.definitions.fed76.domain.Fed76ModData
import com.manson.fo76.settings.SettingsService
import java.io.File
import java.nio.file.Path
import org.slf4j.LoggerFactory

class Fed76EnhancerProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {


    override fun processFileChanges(modEntity: BaseModEntity) {
        if (modEntity !is Fed76ModData || modEntity.isEmpty()) {
            return
        }
        val file = getOutputFile()
        if (!file.exists()) {
            dumpData(file, modEntity)
            return
        }
        val previousDump = readObject(file.toPath())
        if (!previousDump.characterInventories.containsKey(TEST_KEY)) {
            previousDump.characterInventories[TEST_KEY] = ArrayList()
        }
        modEntity.characterInventories[TEST_KEY]?.let { previousDump.characterInventories[TEST_KEY]?.addAll(it) }
        dumpData(file, previousDump)
    }

    override fun getName(): String {
        return MOD_NAME
    }

    private fun dumpData(file: File, obj: Fed76ModData) {
        try {
            objectMapper.writer().withDefaultPrettyPrinter().writeValue(file, obj)
        } catch (e: Exception) {
            LOGGER.error("Error creating dump file", e)
            TxtLogger.log("Error creating dump file: ${e.message}")
        }
    }

    private fun readObject(path: Path): Fed76ModData {
        try {
            return objectMapper.readValue(path.toFile(), Fed76ModData::class.java)
        } catch (e: Exception) {
            LOGGER.error("Error reading previous dump file", e)
            TxtLogger.log("Error reading previous dump file: ${e.message}")
        }
        return Fed76ModData()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Fed76EnhancerProcessor::class.java)
        private const val MOD_NAME = "Fed76Enhancer"
        private const val TEST_KEY = "test"

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