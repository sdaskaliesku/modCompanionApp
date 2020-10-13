package com.manson.fo76.processor.definitions.itemextractor

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.Controller
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.settings.Settings
import com.manson.fo76.settings.SettingsService
import java.io.File
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class ItemExtractorProcessorTest {

    @Disabled
    @Test
    internal fun testSingleFileOutput() {
        val objectMapper: ObjectMapper = Controller.OM;
        val settings = ItemExtractorSettings()
        settings.dumpMode = DumpMode.APPEND
        val settingsService = SettingsService(objectMapper, File(""))
        settingsService.settings = Settings()
        settingsService.settings.itemExtractorSettings = settings
        val itemExtractorProcessor: ItemExtractorProcessor = ItemExtractorProcessor(objectMapper, settingsService)
        val baseModEntity: BaseModEntity = objectMapper.readValue(File("D:\\workspace\\modCompanionApp\\src\\test\\resources\\testItemExtractor.json"), BaseModEntity::class.java);
        itemExtractorProcessor.processFileChanges(baseModEntity)
    }
}