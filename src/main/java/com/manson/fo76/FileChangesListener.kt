package com.manson.fo76

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.hindol.commons.core.Filter
import com.github.hindol.commons.file.DirectoryWatcher
import com.manson.fo76.processor.BaseModEntity
import com.manson.fo76.processor.BaseProcessor
import com.manson.fo76.processor.ProcessorFactory
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.math.abs
import org.slf4j.LoggerFactory

class FileChangesListener(private val filter: Filter<Path>, private val objectMapper: ObjectMapper) {
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
        val modEntity = readObject(path)
        val processor: BaseProcessor? = ProcessorFactory.getProcessor(modEntity.modName)
        if (processor == null) {
            TxtLogger.log("Cannot find mod processor for input `${modEntity.modName}`!")
            Files.deleteIfExists(path)
            return
        }
        TxtLogger.log("Mod processor found: ${processor.modName}!")
        try {
            processor.processFileChanges(modEntity)
            TxtLogger.log("File changes processed!")
        } catch (e: Exception) {
            LOGGER.error("File changes weren't processed", e)
            TxtLogger.log("File changes weren't processed: ${e.message}")
        } finally {
            Files.deleteIfExists(path)
        }
    }

    fun stop() {
        try {
            if (watchService == null) {
                return
            }
            watchService?.stop()
        } catch (e: Exception) {
            LOGGER.error("Error while stopping watch service", e)
            TxtLogger.log("Error while stopping watch service: ${e.message}")
        }
    }

    private fun readObject(path: Path): BaseModEntity {
        try {
            // TODO: for some reasons reader returns empty data for file, let's re-try reading. Needs investigation
            for (i in 1..5) {
                try {
                    return objectMapper.readValue(path.toFile(), BaseModEntity::class.java)
                } catch (ignored: Exception) {

                }
            }
            return objectMapper.readValue(path.toFile(), BaseModEntity::class.java)
        } catch (e: Exception) {
            TxtLogger.log("Error while reading input file: ${e.message}")
            LOGGER.error("Error while reading input file", e)
        }
        return BaseModEntity()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(FileChangesListener::class.java)
    }
}