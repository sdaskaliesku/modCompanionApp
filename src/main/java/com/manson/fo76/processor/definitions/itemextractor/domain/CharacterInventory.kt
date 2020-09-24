package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.lang3.builder.ToStringBuilder
import org.apache.commons.lang3.builder.ToStringStyle

@JsonIgnoreProperties(ignoreUnknown = true)
class CharacterInventory {
    var playerInventory: ArrayList<ItemDescriptor> = ArrayList()
    var stashInventory: ArrayList<ItemDescriptor> = ArrayList()

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CharacterInventory) return false

        if (playerInventory != other.playerInventory) return false
        if (stashInventory != other.stashInventory) return false
        if (accountInfoData != other.accountInfoData) return false
        if (characterInfoData != other.characterInfoData) return false

        return true
    }

    override fun hashCode(): Int {
        var result = playerInventory.hashCode()
        result = 31 * result + stashInventory.hashCode()
        result = 31 * result + accountInfoData.hashCode()
        result = 31 * result + characterInfoData.hashCode()
        return result
    }
}