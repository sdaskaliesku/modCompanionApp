package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

class CharacterInventory {
    var playerInventory: List<ItemDescriptor> = listOf()
    var stashInventory: List<ItemDescriptor> = listOf()

    @JsonProperty("AccountInfoData")
    var accountInfoData: AccountInfoData = AccountInfoData()

    @JsonProperty("CharacterInfoData")
    var characterInfoData: CharacterInfoData = CharacterInfoData()
    override fun toString(): String {
        return ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
                .append("playerInventory", playerInventory)
                .append("stashInventory", stashInventory)
                .append("accountInfoData", accountInfoData)
                .append("characterInfoData", characterInfoData)
                .toString()
    }
}