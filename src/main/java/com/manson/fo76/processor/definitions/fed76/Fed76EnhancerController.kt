package com.manson.fo76.processor.definitions.fed76

import com.manson.fo76.processor.gui.ModGuiController
import com.manson.fo76.settings.SettingsService
import javafx.scene.control.Label
import javafx.scene.control.Tab

class Fed76EnhancerController(settingsService: SettingsService) : ModGuiController(settingsService) {
    override fun createModSettingsTab(): Tab {
        val tab = Tab("Fed76 enhancer mod")
        tab.isClosable = false
        tab.content = Label("No settings available for this mod")
        return tab
    }
}