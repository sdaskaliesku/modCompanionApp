package com.manson.fo76.processor

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.processor.definitions.fed76.Fed76EnhancerController
import com.manson.fo76.processor.definitions.fed76.Fed76EnhancerProcessor
import com.manson.fo76.processor.definitions.itemextractor.ItemExtractorController
import com.manson.fo76.processor.definitions.itemextractor.ItemExtractorProcessor
import com.manson.fo76.processor.definitions.logger.LoggerController
import com.manson.fo76.processor.definitions.logger.LoggerProcessor
import com.manson.fo76.processor.gui.ModGuiController
import com.manson.fo76.settings.SettingsService
import java.util.HashMap
import java.util.HashSet

object ProcessorFactory {
    @JvmField
    val MOD_PROCESSORS: MutableMap<String, BaseProcessor> = HashMap()
    @JvmField
    val MOD_TYPES: MutableSet<String> = HashSet()
    @JvmField
    val MOD_GUI_SETTINGS: MutableMap<String, ModGuiController> = HashMap()

    fun init(objectMapper: ObjectMapper, settingsService: SettingsService) {
        addProcessor(ItemExtractorProcessor.MOD_NAME, ItemExtractorProcessor(objectMapper, settingsService), ItemExtractorController(settingsService))
        addProcessor(LoggerProcessor.MOD_NAME, LoggerProcessor(objectMapper, settingsService), LoggerController(settingsService))
        addProcessor(Fed76EnhancerProcessor.MOD_NAME, Fed76EnhancerProcessor(objectMapper, settingsService), Fed76EnhancerController(settingsService))
    }

    fun <T : BaseProcessor> addProcessor(name: String, baseProcessor: T, modGuiController: ModGuiController) {
        MOD_TYPES.add(name)
        MOD_PROCESSORS[name] = baseProcessor
        MOD_GUI_SETTINGS[name] = modGuiController
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : BaseProcessor> getProcessor(modName: String): T? {
        return MOD_PROCESSORS[modName] as T
    }
}