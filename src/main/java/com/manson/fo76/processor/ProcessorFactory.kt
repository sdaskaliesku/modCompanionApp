package com.manson.fo76.processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.processor.definitions.itemextractor.ItemExtractorController
import com.manson.fo76.processor.definitions.itemextractor.ItemExtractorProcessor
import com.manson.fo76.processor.definitions.pricecheck.PriceCheckController
import com.manson.fo76.processor.definitions.pricecheck.PriceCheckProcessor
import com.manson.fo76.processor.gui.ModGuiController
import com.manson.fo76.settings.SettingsService

object ProcessorFactory {
    // TODO: Create smth like ModDescriptor and hold Processor and Controller inside
    @JvmField
    val MOD_PROCESSORS: MutableMap<String, BaseProcessor> = LinkedHashMap()

    @JvmField
    val MOD_GUI_SETTINGS: MutableMap<String, ModGuiController> = LinkedHashMap()

    fun init(objectMapper: ObjectMapper, settingsService: SettingsService) {
        addProcessor(ItemExtractorProcessor.MOD_NAME, ItemExtractorProcessor(objectMapper, settingsService), ItemExtractorController(settingsService))
        addProcessor(PriceCheckProcessor.MOD_NAME, PriceCheckProcessor(objectMapper, settingsService), PriceCheckController(settingsService))
//        addProcessor(LoggerProcessor.MOD_NAME, LoggerProcessor(objectMapper, settingsService), LoggerController(settingsService))
    }

    fun <T : BaseProcessor> addProcessor(name: String, baseProcessor: T, modGuiController: ModGuiController) {
        MOD_PROCESSORS[name] = baseProcessor
        MOD_GUI_SETTINGS[name] = modGuiController
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseProcessor> getProcessor(modName: String): T? {
        return MOD_PROCESSORS[modName] as T
    }
}