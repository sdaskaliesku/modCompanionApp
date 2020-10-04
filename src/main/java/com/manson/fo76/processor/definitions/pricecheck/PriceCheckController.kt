package com.manson.fo76.processor.definitions.pricecheck

import com.manson.fo76.processor.definitions.itemextractor.DumpMode
import com.manson.fo76.processor.gui.ModGuiController
import com.manson.fo76.settings.SettingsService
import javafx.beans.value.ObservableValue
import javafx.scene.control.RadioButton
import javafx.scene.control.Tab
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.AnchorPane

class PriceCheckController(settingsService: SettingsService) : ModGuiController(settingsService) {
    private val layoutYStep = 24.0
    private var initialLayoutY = 14.0
    private val layoutX = 16.0

    private fun createRadioButtons(): List<RadioButton> {
        val toggleGroup = ToggleGroup()
        val singleDumpRadio = RadioButton("Dump data to a single file")
        val multipleDumpRadio = RadioButton("Dump data to separate files")
        multipleDumpRadio.layoutX = layoutX
        multipleDumpRadio.layoutY = initialLayoutY
        initialLayoutY += layoutYStep
        singleDumpRadio.layoutX = layoutX
        singleDumpRadio.layoutY = initialLayoutY
        multipleDumpRadio.isSelected = true
        singleDumpRadio.toggleGroup = toggleGroup
        multipleDumpRadio.toggleGroup = toggleGroup
        initialLayoutY += layoutYStep

        multipleDumpRadio.selectedProperty().addListener { _: ObservableValue<out Boolean>, _: Boolean, _: Boolean ->
            settingsService.settings.priceCheckSettings.dumpMode = DumpMode.CREATE_NEW
            settingsService.save()
        }
        singleDumpRadio.selectedProperty().addListener { _: ObservableValue<out Boolean>, _: Boolean, _: Boolean ->
            settingsService.settings.priceCheckSettings.dumpMode = DumpMode.APPEND
            settingsService.save()
        }
        when (settingsService.settings.priceCheckSettings.dumpMode) {
            DumpMode.APPEND -> singleDumpRadio.isSelected = true
            DumpMode.CREATE_NEW -> multipleDumpRadio.isSelected = true
        }
        return listOf(multipleDumpRadio, singleDumpRadio)
    }

    override fun createModSettingsTab(): Tab {
        val tab = Tab(PriceCheckProcessor.MOD_NAME)
        tab.isClosable = false
        val anchorPane = AnchorPane()
        anchorPane.prefHeight = 171.0
        anchorPane.prefWidth = 197.0
        anchorPane.children.addAll(createRadioButtons())
        tab.content = anchorPane
        return tab
    }
}