package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ModCardEntry {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ModCardEntry) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}