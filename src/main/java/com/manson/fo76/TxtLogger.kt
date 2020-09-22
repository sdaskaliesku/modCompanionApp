package com.manson.fo76

import java.text.SimpleDateFormat
import java.util.Date
import javafx.scene.control.TextArea

class TxtLogger private constructor(private val textArea: TextArea) {
    fun append(txt: String) {
        val message = """
            [${SDF.format(Date())}] $txt
            
            """.trimIndent()
        textArea.appendText(message)
    }

    companion object {
        private val SDF = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        private var INSTANCE: TxtLogger? = null
        fun init(textArea: TextArea) {
            INSTANCE = TxtLogger(textArea)
        }

        fun log(txt: String) {
            INSTANCE!!.append(txt)
        }
    }
}