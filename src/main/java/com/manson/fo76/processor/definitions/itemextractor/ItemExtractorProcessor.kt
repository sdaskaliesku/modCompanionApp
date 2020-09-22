package com.manson.fo76.processor.definitions.itemextractor

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Objects
import com.manson.fo76.Locations
import com.manson.fo76.TxtLogger
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.definitions.itemextractor.domain.ModData
import com.manson.fo76.settings.SettingsService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ItemExtractorProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {

    private fun getSettings() : ItemExtractorSettings {
        return settingsService.settings.itemExtractorSettings
    }

    override fun processFileChanges(path: Path) {
        if (getSettings().dumpMode == DumpMode.CREATE_NEW) {
            try {
                val value = readObject(path)
                // TODO: set char/acc name to file name
                objectMapper.writeValue(Locations.itemExtractorMultipleOutputFile, value)
                Files.deleteIfExists(path)
            } catch (e: Exception) {
                e.printStackTrace()
                TxtLogger.log("Error creating dump file: ${e.message}")
            }
        } else if (getSettings().dumpMode == DumpMode.APPEND) {
            val value = readObject(path)
            if (!Locations.itemExtractorSingleOutputFile.exists()) {
                objectMapper.writeValue(Locations.itemExtractorSingleOutputFile, value)
                Files.deleteIfExists(path)
            } else {
//                TODO:
                val oldObj: ModData = readObject(Paths.get(Locations.itemExtractorInputFile))
                if (!Objects.equal(value.user, oldObj.version)) {
                    TxtLogger.log("Different object versions!")
                    return
                }
                val charInv1 = value.characterInventories
                val charInv2 = oldObj.characterInventories

                for (entry in charInv1) {
                    if (!charInv2.containsKey(entry.key)) {
                        TxtLogger.log("Found new character items ${entry.key}")
                        charInv2[entry.key] = entry.value
                        continue
                    }

                }
                println()
            }
        }
    }

    override fun getName(): String {
        return MOD_NAME
    }

    private fun readObject(path: Path): ModData {
        return objectMapper.readValue(path.toFile(), ModData::class.java)
    }

    companion object {
        const val MOD_NAME = "ItemExtractorMod"
    }


}