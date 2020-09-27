package com.manson.fo76

import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class Main : Application() {
    @Throws(Exception::class)
    override fun start(stage: Stage) {
        val loader = javaClass.classLoader
        val fxmlLoader = FXMLLoader(loader.getResource("mainForm.fxml"))
        val root = fxmlLoader.load<Parent>()
        val controller: Controller = fxmlLoader.getController()
        stage.onHidden = EventHandler { controller.shutdown() }
        val scene = Scene(root)
        stage.scene = scene
        stage.isResizable = false
        stage.title = "Mod Companion App v$VERSION"
        val imgStream = loader.getResourceAsStream("favicon.png")
        val image = Image(imgStream)
        stage.icons.add(image)
        stage.show()
    }

    companion object {
        const val VERSION = 1.5
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}