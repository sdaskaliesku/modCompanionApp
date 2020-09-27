package com.manson.fo76

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.Locations.itemsModInitFilter
import com.manson.fo76.processor.ProcessorFactory
import com.manson.fo76.processor.ProcessorFactory.init
import com.manson.fo76.settings.SettingsService
import java.awt.Desktop
import java.io.File
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.scene.control.TabPane
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import org.slf4j.LoggerFactory

class Controller {
    companion object {
        val OM = ObjectMapper()
        private val LOGGER = LoggerFactory.getLogger(Controller::class.java)

        init {
            OM.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            OM.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            OM.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true)
        }
    }

    @FXML
    private var fo76Path: TextField? = null

    @FXML
    private var logsTxt: TextArea? = null

    @FXML
    private var startBtn: Button? = null

    @FXML
    private var stopBtn: Button? = null

    @FXML
    private var outputBtn: Button? = null

    @FXML
    private var progressBar: ProgressBar? = null

    @FXML
    private var modSettingsTabPane: TabPane? = null

    private val settingsService: SettingsService = SettingsService(OM, Locations.settingsDirectory)
    private val fileChangesListener: FileChangesListener = FileChangesListener(itemsModInitFilter, OM)


    @FXML
    fun initialize() {
        init(OM, settingsService)
        ProcessorFactory.MOD_GUI_SETTINGS.values.forEach { modSettingsTabPane?.tabs?.add(it.createModSettingsTab()) }
        logsTxt?.let { TxtLogger.init(it) }
        progressBar?.cursorProperty()?.value = Cursor.DEFAULT
        stopBtn!!.isDisable = true
        TxtLogger.log("Output directory is: ${Locations.outputDirectory.absolutePath}")
        outputBtn!!.onAction = EventHandler {
            try {
                Desktop.getDesktop().browse(Locations.outputDirectory.toURI())
            } catch (e: Exception) {
                TxtLogger.log("Error opening output directory: ${e.message}")
            }
        }
        startBtn!!.onAction = EventHandler {
            TxtLogger.log("Starting listening")
            fileChangesListener.setWatchingDirectory(File(settingsService.settings.fo76DataDirectory))
            fileChangesListener.start()
            stopBtn?.isDisable = false
            startBtn?.isDisable = true
            progressBar?.cursorProperty()?.value = Cursor.WAIT
            progressBar?.progressProperty()?.set(Double.NEGATIVE_INFINITY)
        }
        stopBtn?.onAction = EventHandler {
            TxtLogger.log("Stopping listening")
            fileChangesListener.stop()
            startBtn?.isDisable = false
            stopBtn?.isDisable = true
            progressBar?.cursorProperty()?.value = Cursor.DEFAULT
            progressBar?.progressProperty()?.set(0.0)
        }
        fo76Path?.text = settingsService.settings.fo76DataDirectory
        fo76Path?.textProperty()?.addListener { _: ObservableValue<out String?>?, _: String?, newValue: String? ->
            settingsService.settings.fo76DataDirectory = newValue.toString()
            settingsService.save()
        }
    }

    fun shutdown() {
        settingsService.save()
        fileChangesListener.stop()
        Platform.exit()
    }

}