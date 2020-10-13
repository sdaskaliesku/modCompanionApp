package com.manson.fo76

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.manson.fo76.Locations.itemsModInitFilter
import com.manson.fo76.processor.ProcessorFactory
import com.manson.fo76.processor.ProcessorFactory.init
import com.manson.fo76.service.UpdateService
import com.manson.fo76.service.graphql.LatestVersion
import com.manson.fo76.settings.SettingsService
import java.awt.Desktop
import java.io.File
import java.net.URI
import javafx.application.Platform
import javafx.beans.value.ObservableValue
import javafx.concurrent.Task
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.MenuItem
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
            OM.getSerializationConfig().getDefaultVisibilityChecker()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                    .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                    .withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
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

    @FXML
    private var closeBtn: MenuItem? = null

    @FXML
    private var clearLogsBtn: MenuItem? = null

    @FXML
    private var checkForUpdatesBtn: MenuItem? = null

    @FXML
    private var gitHubBtn: MenuItem? = null

    @FXML
    private var nexusBtn: MenuItem? = null

    @FXML
    private var discordBtn: MenuItem? = null

    private val settingsService: SettingsService = SettingsService(OM, Locations.settingsDirectory)
    private val fileChangesListener: FileChangesListener = FileChangesListener(itemsModInitFilter, OM)
    private val updateService = UpdateService(OM)


    private fun hasUpdate(latestVersion: LatestVersion): Boolean {
        return Main.VERSION < latestVersion.version.toDouble()
    }


    @FXML
    fun initialize() {
        init(OM, settingsService)
        ProcessorFactory.MOD_GUI_SETTINGS.values.forEach { modSettingsTabPane?.tabs?.add(it.createModSettingsTab()) }
        logsTxt?.let { TxtLogger.init(it) }

        val task: Task<Void?> = object : Task<Void?>() {
            override fun call(): Void? {
                try {
                    val latestVersion = updateService.getLatestVersion()
                    if (hasUpdate(latestVersion)) {
                        TxtLogger.log("New version available: ${latestVersion.version}. Current version: ${Main.VERSION}")
                    }
                } catch (e: Exception) {
                    LOGGER.error("Error checking for updates", e)
                }
                return null
            }
        }
        Thread(task).start()

        closeBtn?.onAction = EventHandler {
            this.shutdown()
        }
        clearLogsBtn?.onAction = EventHandler {
            this.logsTxt?.clear()
        }
        gitHubBtn?.onAction = EventHandler {
            try {
                Desktop.getDesktop().browse(URI(Links.GITHUB))
            } catch (e: Exception) {
                TxtLogger.log("Error opening link: ${e.message}")
            }
        }
        discordBtn?.onAction = EventHandler {
            try {
                Desktop.getDesktop().browse(URI(Links.DISCORD))
            } catch (e: Exception) {
                TxtLogger.log("Error opening link: ${e.message}")
            }
        }
        nexusBtn?.onAction = EventHandler {
            try {
                Desktop.getDesktop().browse(URI(Links.NEXUS))
            } catch (e: Exception) {
                TxtLogger.log("Error opening link: ${e.message}")
            }
        }
        checkForUpdatesBtn?.onAction = EventHandler {
            try {
                val latestVersion = updateService.getLatestVersion()
                var alertType = Alert.AlertType.INFORMATION
                var alertText = "You're using most recent version!"
                if (hasUpdate(latestVersion)) {
                    alertText = "New version available: ${latestVersion.version}. Current version: ${Main.VERSION}"
                    alertType = Alert.AlertType.WARNING
                }
                TxtLogger.log(alertText)
                Alert(alertType, alertText).showAndWait()
            } catch (e: Exception) {
                LOGGER.error("Error checking for updates", e)
                TxtLogger.log("Error checking for updates: ${e.message}")
            }
        }
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