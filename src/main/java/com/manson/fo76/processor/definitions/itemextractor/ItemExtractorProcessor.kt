package com.manson.fo76.processor.definitions.itemextractor

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Objects
import com.google.common.hash.Hashing
import com.manson.domain.fo76.ItemDescriptor
import com.manson.domain.itemextractor.CharacterInventory
import com.manson.fo76.Locations
import com.manson.fo76.TxtLogger
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.definitions.itemextractor.domain.ModData
import com.manson.fo76.settings.SettingsService
import java.io.File
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.util.Date
import org.slf4j.LoggerFactory

class ItemExtractorProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {

    private fun getSettings(): ItemExtractorSettings {
        return settingsService.settings.itemExtractorSettings
    }

    private fun shouldAnonymize(): Boolean {
        return getSettings().anonymizeItems || getSettings().anonymizeAccount || getSettings().anonymizeCharacter
    }

    @Suppress("UnstableApiUsage")
    private fun hash(input: String): String {
        return Hashing.sha256().hashString(input, Charsets.UTF_8).toString()
    }

    @Suppress("UnstableApiUsage")
    private fun hash(input: Long): Long {
        return Hashing.sha256().hashLong(input).asLong()
    }

    private fun anonymizeData(modData: ModData): ModData {
        for (entry in modData.characterInventories) {
            val characterInventory = entry.value
            if (getSettings().anonymizeAccount) {
                characterInventory.accountInfoData.name = characterInventory.accountInfoData.name.let { hash(it) }
            }
            if (getSettings().anonymizeCharacter) {
                characterInventory.characterInfoData.name = characterInventory.characterInfoData.name.let { hash(it) }
            }
            if (getSettings().anonymizeItems) {
                characterInventory.playerInventory.forEach { it.serverHandleId = hash(it.serverHandleId) }
                characterInventory.stashInventory.forEach { it.serverHandleId = hash(it.serverHandleId) }
            }
        }
        return modData
    }

    class UserInfo {
        var charName: String = ""
        var accName: String = ""
    }

    private fun getUserInfo(modData: ModData): UserInfo {
        val userInfo = UserInfo()
        val inventory = modData.characterInventories.values.first()
        userInfo.accName = inventory.accountInfoData.name
        userInfo.charName = inventory.characterInfoData.name
        return userInfo
    }

    override fun processFileChanges(modEntity: BaseModEntity) {
        if (modEntity !is ModData || modEntity.isEmpty()) {
            return
        }
        try {
            val userInfo = getUserInfo(modEntity)
            TxtLogger.log("Processing items for character: ${userInfo.charName} from account: ${userInfo.accName}")
        } catch (e: Exception) {
            TxtLogger.log("Error while getting acc/char info: $e")
            LOGGER.error("Error while getting acc/char info", e)
        }
        var objectToDump = modEntity
        var dumpLocation: File = getSingleOutputFile()
        if (getSettings().dumpMode == DumpMode.CREATE_NEW) {
            objectToDump = modEntity
            dumpLocation = retrieveMultipleOutputFile(modEntity)
        } else if (getSettings().dumpMode == DumpMode.APPEND) {
            objectToDump = mergeData(modEntity)
            dumpLocation = getSingleOutputFile()
        }
        if (objectToDump.isEmpty()) {
            return
        }
        dumpData(dumpLocation, objectToDump)
    }

    private fun retrieveMultipleOutputFile(modEntity: ModData): File {
        val inventory = modEntity.characterInventories.values.first()
        val charName = inventory.characterInfoData.name
        val accName = inventory.accountInfoData.name
        return getMultipleOutputFile(charName, accName)
    }

    private fun mergeData(modEntity: ModData): ModData {
        val outputFile = getSingleOutputFile()
        if (!outputFile.exists()) {
            return modEntity
        }
        val oldObj: ModData = readObject(outputFile.toPath())
        if (!Objects.equal(modEntity.version, oldObj.version)) {
            TxtLogger.log("Different object versions, processing aborted!")
            return ModData()
        }
        val newInventories: MutableMap<String, CharacterInventory> = modEntity.characterInventories
        val oldInventories: MutableMap<String, CharacterInventory> = oldObj.characterInventories

        for (entry in newInventories) {
            val newInventory: CharacterInventory = entry.value
            if (oldInventories.containsKey(entry.key)) {
                val oldInventory: CharacterInventory = oldInventories[entry.key]!!
                oldObj.characterInventories[entry.key]!!.playerInventory = mergeItems(oldInventory.playerInventory, newInventory.playerInventory)
                oldObj.characterInventories[entry.key]!!.stashInventory = mergeItems(oldInventory.stashInventory, newInventory.stashInventory)
            } else {
                TxtLogger.log("Found new character items ${entry.key}")
                oldInventories[entry.key] = newInventory
            }
        }
        return oldObj
    }

    private fun dumpData(file: File, obj: ModData) {
        try {
            if (shouldAnonymize()) {
                anonymizeData(obj)
            }
            objectMapper.writer().withDefaultPrettyPrinter().writeValue(file, obj)
        } catch (e: Exception) {
            LOGGER.error("Error creating dump file", e)
            TxtLogger.log("Error creating dump file: ${e.message}")
        }
    }

    private fun mergeItems(oldItems: MutableList<ItemDescriptor>, newItems: MutableList<ItemDescriptor>): ArrayList<ItemDescriptor> {
        val newInventoryItems: HashSet<ItemDescriptor> = HashSet()
        oldItems.let { newInventoryItems.addAll(it) }
        newItems.let { newInventoryItems.addAll(it) }
        val list: ArrayList<ItemDescriptor> = ArrayList()
        list.addAll(newInventoryItems)
        return list
    }

    override fun getName(): String {
        return MOD_NAME
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
        private val LOGGER = LoggerFactory.getLogger(ItemExtractorProcessor::class.java)
        const val MOD_NAME = "Invent-O-Matic-Extractor"

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

        private fun getMultipleOutputFile(charName: String, accName: String): File {
            val fileName = charName + "_" + accName + "_" + SDF.format(Date()) + ".json"
            return File(outputDir, fileName)
        }

        private val SDF = SimpleDateFormat("yyyyMMdd_HHmmss")
    }


}