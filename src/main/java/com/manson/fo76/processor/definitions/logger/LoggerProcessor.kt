package com.manson.fo76.processor.definitions.logger

import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.settings.SettingsService

class LoggerProcessor(private val objectMapper: ObjectMapper, settingsService: SettingsService) : BaseProcessor(MOD_NAME, settingsService) {


    override fun processFileChanges(modEntity: BaseModEntity) {

    }

    override fun getName(): String {
        return MOD_NAME
    }

    companion object {
        const val MOD_NAME = "LoggerMod"
    }


}