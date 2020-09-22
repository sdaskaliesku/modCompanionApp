package com.manson.fo76.processor.definitions.itemextractor.domain

class ItemCardEntry : AbstractObject() {
    var text: String? = null
    var value: String? = null
    var damageType: Int? = null
    var difference: Int? = null
    var diffRating: Int? = null
    var precision: Int? = null
    var duration: Int? = null
    var showAsDescription: Boolean? = null
    var components: List<ItemCardEntryComponent> = listOf()

}