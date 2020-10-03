package com.manson.fo76.settings

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.manson.fo76.processor.definitions.itemextractor.ItemExtractorSettings

@JsonIgnoreProperties(ignoreUnknown = true)
class Settings {
    var fo76DataDirectory: String = ""
    var itemExtractorSettings: ItemExtractorSettings = ItemExtractorSettings()
}