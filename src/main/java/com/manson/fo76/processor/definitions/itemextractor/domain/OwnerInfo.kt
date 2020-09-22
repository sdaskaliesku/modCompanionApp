package com.manson.fo76.processor.definitions.itemextractor.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder

@JsonIgnoreProperties(ignoreUnknown = true)
class OwnerInfo {
    var id: String? = null

    var name: String? = null

    var accountOwner: String? = null

    var characterOwner: String? = null
    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o !is OwnerInfo) {
            return false
        }
        val ownerInfo = o
        return EqualsBuilder()
                .append(id, ownerInfo.id)
                .append(name, ownerInfo.name)
                .append(accountOwner, ownerInfo.accountOwner)
                .append(characterOwner, ownerInfo.characterOwner)
                .isEquals
    }

    override fun hashCode(): Int {
        return HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(accountOwner)
                .append(characterOwner)
                .toHashCode()
    }
}