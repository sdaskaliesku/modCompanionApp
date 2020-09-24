package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class VendingData {
    var vendedOnOtherMachine: Boolean? = null
    var price: Int? = null
    var machineType: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VendingData) return false

        if (vendedOnOtherMachine != other.vendedOnOtherMachine) return false
        if (price != other.price) return false
        if (machineType != other.machineType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = vendedOnOtherMachine?.hashCode() ?: 0
        result = 31 * result + (price ?: 0)
        result = 31 * result + (machineType ?: 0)
        return result
    }


}