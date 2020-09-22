package com.manson.fo76.processor

import com.manson.fo76.settings.SettingsService
import java.nio.file.Path

abstract class BaseProcessor(val modName: String, protected val settingsService: SettingsService) {

    abstract fun processFileChanges(path: Path)
    abstract fun getName() : String
}