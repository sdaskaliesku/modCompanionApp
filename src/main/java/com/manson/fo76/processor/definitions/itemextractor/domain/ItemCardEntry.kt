package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class ItemCardEntry {
    var text: String? = null
    var value: String? = null
    var damageType: Int? = null
    var difference: Int? = null
    var diffRating: Int? = null
    var precision: Int? = null
    var duration: Int? = null
    var showAsDescription: Boolean? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemCardEntry) return false

        if (text != other.text) return false
        if (value != other.value) return false
        if (damageType != other.damageType) return false
        if (difference != other.difference) return false
        if (diffRating != other.diffRating) return false
        if (precision != other.precision) return false
        if (duration != other.duration) return false
        if (showAsDescription != other.showAsDescription) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (damageType ?: 0)
        result = 31 * result + (difference ?: 0)
        result = 31 * result + (diffRating ?: 0)
        result = 31 * result + (precision ?: 0)
        result = 31 * result + (duration ?: 0)
        result = 31 * result + (showAsDescription?.hashCode() ?: 0)
        return result
    }

}