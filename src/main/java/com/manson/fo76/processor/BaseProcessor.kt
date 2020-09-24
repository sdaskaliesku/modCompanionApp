package com.manson.fo76.processor

import com.manson.fo76.settings.SettingsService

abstract class BaseProcessor(val modName: String, protected val settingsService: SettingsService) {

    abstract fun processFileChanges(modEntity: BaseModEntity)
    abstract fun getName(): String
}