package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CharacterInfoData {
    var name: String = ""
    var level: Int = 0
}