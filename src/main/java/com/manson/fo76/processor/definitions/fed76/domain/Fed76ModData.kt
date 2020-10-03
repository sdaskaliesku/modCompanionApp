package com.manson.fo76.processor.definitions.fed76.domain

import com.fasterxml.jackson.annotation.JsonTypeName
import com.manson.domain.fo76.items.ItemDescriptor
import com.manson.fo76.processor.BaseModEntity
import java.util.Date
import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

@JsonTypeName("Fed76Enhancer")
class Fed76ModData : BaseModEntity() {
    var version: Double = 0.0
    var dumpDate: Date = Date()
    var characterInventories: MutableMap<String, MutableList<ItemDescriptor>> = HashMap()
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("characterInventories", characterInventories)
                .toString()
    }

    fun isEmpty(): Boolean {
        return MapUtils.isEmpty(characterInventories)
    }
}