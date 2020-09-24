package com.manson.fo76.processor.gui

import com.manson.fo76.settings.SettingsService
import javafx.scene.control.Tab

abstract class ModGuiController(protected var settingsService: SettingsService) {
    abstract fun createModSettingsTab(): Tab?
}