package com.manson.fo76.processor

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.manson.fo76.processor.definitions.itemextractor.domain.ModData
import com.manson.fo76.processor.definitions.logger.domain.LoggerModData

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "modName", visible = true)
@JsonSubTypes(
        JsonSubTypes.Type(value = ModData::class, name = "Invent-O-Matic-Extractor"),
        JsonSubTypes.Type(value = ModData::class, name = "Invent-O-Matic-Vendor-Extractor"),
        JsonSubTypes.Type(value = LoggerModData::class, name = "LoggerMod")
)
@JsonIgnoreProperties(ignoreUnknown = true)
open class BaseModEntity {
    var modName: String = ""
}