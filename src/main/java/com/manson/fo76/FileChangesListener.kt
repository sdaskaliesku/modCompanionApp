package com.manson.fo76

import com.github.hindol.commons.core.Filter
import com.github.hindol.commons.file.DirectoryWatcher
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.ProcessorFactory
import java.io.File
import java.nio.file.Path
import kotlin.math.abs
import org.slf4j.LoggerFactory

class FileChangesListener(private val filter: Filter<Path>) {
    private var watchingDirectory: File? = null
    private var watchService: DirectoryWatcher? = null
    private var lastChangeTs = System.currentTimeMillis()

    fun setWatchingDirectory(watchingDirectory: File?) {
        this.watchingDirectory = watchingDirectory
    }

    fun start() {
        try {
            watchService = DirectoryWatcher.Builder()
                    .addDirectories(watchingDirectory!!.absolutePath)
                    .setFilter(filter)
                    .build { event: DirectoryWatcher.Event, path: Path ->
                        val currentTs = System.currentTimeMillis()
                        // for some reasons library throws 2 change events in a row, add custom delay to eliminate this issue
                        if (abs(currentTs - lastChangeTs) >= 500) {
                            if (event == DirectoryWatcher.Event.ENTRY_CREATE || event == DirectoryWatcher.Event.ENTRY_MODIFY) {
                                lastChangeTs = currentTs
                                processFileChanges(path)
                            }
                        }
                    }
            watchService?.start()
        } catch (e: Exception) {
            LOGGER.error("Error while starting watch service", e)
            TxtLogger.log("Error while starting watch service: ${e.message}")
        }
    }

    private fun processFileChanges(path: Path) {
        TxtLogger.log("File changes detected - processing!")
        // TODO: read modType from ini file
        val modName = ""
        val processor: BaseProcessor? = ProcessorFactory.getProcessor(modName)
        if (processor != null) {
            TxtLogger.log("Mod processor found: ${processor.modName}!")
            processor.processFileChanges(path)
            TxtLogger.log("File changes processed!")
        } else {
            TxtLogger.log("Cannot find mod processor for input $modName!")
        }
    }

    fun stop() {
        try {
            if (watchService != null) {
                watchService!!.stop()
            }
        } catch (e: Exception) {
            LOGGER.error("Error while stopping watch service", e)
            TxtLogger.log("Error while stopping watch service: ${e.message}")
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FileChangesListener::class.java)
    }
}