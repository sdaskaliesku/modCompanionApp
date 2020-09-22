package com.manson.fo76.processor.definitions.logger

import com.manson.fo76.processor.gui.ModGuiController
import com.manson.fo76.settings.SettingsService
import javafx.scene.control.Label
import javafx.scene.control.Tab

class LoggerController(settingsService: SettingsService?) : ModGuiController(settingsService) {
    override fun createModSettingsTab(): Tab {
        val tab = Tab("Logger mod")
        tab.isClosable = false
        tab.content = Label("Coming soon")
        return tab
    }
}