package com.manson.fo76.settings

import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import org.slf4j.LoggerFactory

class SettingsService(private val objectMapper: ObjectMapper, private val settingsDir: File) {
    var settings: Settings
        private set
        public get
    private val settingsFile: File
        get() = File(settingsDir, SETTINGS_FILE_NAME)

    fun save() {
        try {
            objectMapper.writeValue(settingsFile, settings)
        } catch (e: Exception) {
            LOGGER.error("Error while saving settings {}", settingsFile, e)
        }
    }

    fun load(): Settings {
        try {
            if (!settingsFile.exists()) {
                return Settings()
            }
            return objectMapper.readValue(settingsFile, Settings::class.java) ?: return Settings()
        } catch (e: Exception) {
            LOGGER.error("Error while loading settings {}", settingsFile, e)
        }
        return Settings()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SettingsService::class.java)
        private const val SETTINGS_FILE_NAME: String = "settings.json"
        protected fun createNewFile(file: File) {
            try {
                file.createNewFile()
            } catch (e: Exception) {
                LOGGER.error("Error while creating new file {}", file, e)
            }
        }
    }

    init {
        settings = load()
    }

}